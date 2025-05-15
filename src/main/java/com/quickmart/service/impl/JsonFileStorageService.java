package com.quickmart.service.impl;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.quickmart.model.PrimeMember;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicInteger;

@Service
public class JsonFileStorageService {
    private final ObjectMapper objectMapper;
    private final AtomicInteger idCounter;
    private final File dataFile;

    public JsonFileStorageService(@Value("${app.storage.json.path}") String jsonFilePath) {
        this.objectMapper = new ObjectMapper();
        this.idCounter = new AtomicInteger(1);
        this.dataFile = new File(jsonFilePath);
        initializeDataFile();
    }

    private void initializeDataFile() {
        try {
            // Create parent directories if they don't exist
            File parentDir = dataFile.getParentFile();
            if (parentDir != null && !parentDir.exists()) {
                parentDir.mkdirs();
            }
            
            // Initialize file with empty members array if it doesn't exist
            if (!dataFile.exists()) {
                objectMapper.writeValue(dataFile, new MembersWrapper(new ArrayList<>()));
            }
        } catch (IOException e) {
            throw new RuntimeException("Failed to initialize data file: " + dataFile.getAbsolutePath(), e);
        }
    }

    public List<PrimeMember> readAll() {
        try {
            MembersWrapper wrapper = objectMapper.readValue(dataFile, MembersWrapper.class);
            return wrapper.getMembers();
        } catch (IOException e) {
            throw new RuntimeException("Failed to read data file", e);
        }
    }

    public void writeAll(List<PrimeMember> members) {
        try {
            objectMapper.writeValue(dataFile, new MembersWrapper(members));
        } catch (IOException e) {
            throw new RuntimeException("Failed to write data file", e);
        }
    }

    public Optional<PrimeMember> findById(String id) {
        return readAll().stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        return readAll().stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }

    public boolean existsByContactNumber(String contactNumber) {
        return readAll().stream()
                .anyMatch(member -> member.getContactNumber().equals(contactNumber));
    }

    public PrimeMember save(PrimeMember member) {
        List<PrimeMember> members = readAll();
        
        if (member.getId() == null || member.getId().trim().isEmpty()) {
            // Generate new ID for new member
            String newId = generateNewId(members);
            member.setId(newId);
            members.add(member);
        } else {
            // Update existing member
            int index = findMemberIndexById(members, member.getId());
            if (index != -1) {
                members.set(index, member);
            } else {
                members.add(member);
            }
        }
        
        writeAll(members);
        return member;
    }

    private String generateNewId(List<PrimeMember> members) {
        // Find the highest existing ID number
        int maxId = 0;
        for (PrimeMember member : members) {
            if (member.getId() != null && member.getId().startsWith("PM")) {
                try {
                    int idNum = Integer.parseInt(member.getId().substring(2));
                    maxId = Math.max(maxId, idNum);
                } catch (NumberFormatException e) {
                    // Skip invalid IDs
                }
            }
        }
        
        // Generate next ID
        return String.format("PM%03d", maxId + 1);
    }

    private int findMemberIndexById(List<PrimeMember> members, String id) {
        for (int i = 0; i < members.size(); i++) {
            if (members.get(i).getId().equals(id)) {
                return i;
            }
        }
        return -1;
    }

    public void delete(String id) {
        List<PrimeMember> members = readAll();
        members.removeIf(member -> member.getId().equals(id));
        writeAll(members);
    }

    // Wrapper class for JSON structure
    private static class MembersWrapper {
        private List<PrimeMember> members;

        public MembersWrapper() {
        }

        public MembersWrapper(List<PrimeMember> members) {
            this.members = members;
        }

        public List<PrimeMember> getMembers() {
            return members;
        }

        public void setMembers(List<PrimeMember> members) {
            this.members = members;
        }
    }
} 