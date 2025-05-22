// JSON file read/write

package com.admin.quickmart.service.impl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.admin.quickmart.model.PrimeMember;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
    private static final Logger logger = LoggerFactory.getLogger(JsonFileStorageService.class);
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
                boolean created = parentDir.mkdirs();
                if (!created) {
                    logger.error("Failed to create parent directories for data file: {}", dataFile.getAbsolutePath());
                    throw new RuntimeException("Failed to create parent directories for data file");
                }
            }
            
            // Initialize file with empty members array if it doesn't exist
            if (!dataFile.exists()) {
                logger.info("Creating new data file at: {}", dataFile.getAbsolutePath());
                objectMapper.writeValue(dataFile, new MembersWrapper(new ArrayList<>()));
            } else {
                logger.info("Using existing data file at: {}", dataFile.getAbsolutePath());
            }
        } catch (IOException e) {
            logger.error("Failed to initialize data file: {}", dataFile.getAbsolutePath(), e);
            throw new RuntimeException("Failed to initialize data file: " + dataFile.getAbsolutePath(), e);
        }
    }

    public List<PrimeMember> readAll() {
        try {
            logger.debug("Reading all members from data file");
            if (!dataFile.exists()) {
                logger.warn("Data file does not exist at: {}", dataFile.getAbsolutePath());
                return new ArrayList<>();
            }
            MembersWrapper wrapper = objectMapper.readValue(dataFile, MembersWrapper.class);
            if (wrapper == null || wrapper.getMembers() == null) {
                logger.warn("No members found in data file");
                return new ArrayList<>();
            }
            return wrapper.getMembers();
        } catch (IOException e) {
            logger.error("Failed to read data file: {}", dataFile.getAbsolutePath(), e);
            throw new RuntimeException("Failed to read data file", e);
        }
    }

    public void writeAll(List<PrimeMember> members) {
        try {
            logger.debug("Writing {} members to data file", members.size());
            objectMapper.writeValue(dataFile, new MembersWrapper(members));
        } catch (IOException e) {
            logger.error("Failed to write data file", e);
            throw new RuntimeException("Failed to write data file", e);
        }
    }

    public Optional<PrimeMember> findById(String id) {
        logger.debug("Finding member by ID: {}", id);
        return readAll().stream()
                .filter(member -> member.getId().equals(id))
                .findFirst();
    }

    public boolean existsByEmail(String email) {
        logger.debug("Checking if member exists with email: {}", email);
        return readAll().stream()
                .anyMatch(member -> member.getEmail().equals(email));
    }

    public boolean existsByContactNumber(String contactNumber) {
        logger.debug("Checking if member exists with contact number: {}", contactNumber);
        return readAll().stream()
                .anyMatch(member -> member.getContactNumber().equals(contactNumber));
    }

    public PrimeMember save(PrimeMember member) {
        logger.debug("Saving member: {}", member.getId());
        List<PrimeMember> members = readAll();
        
        if (member.getId() == null || member.getId().trim().isEmpty()) {
            // Generate new ID for new member
            String newId = generateNewId(members);
            member.setId(newId);
            members.add(member);
            logger.info("Created new member with ID: {}", newId);
        } else {
            // Update existing member
            int index = findMemberIndexById(members, member.getId());
            if (index != -1) {
                members.set(index, member);
                logger.info("Updated existing member with ID: {}", member.getId());
            } else {
                members.add(member);
                logger.info("Added member with ID: {}", member.getId());
            }
        }
        
        writeAll(members);
        return member;
    }

    public void delete(String id) {
        logger.debug("Deleting member with ID: {}", id);
        List<PrimeMember> members = readAll();
        members.removeIf(member -> member.getId().equals(id));
        writeAll(members);
        logger.info("Deleted member with ID: {}", id);
    }

    private String generateNewId(List<PrimeMember> members) {
        int maxId = members.stream()
                .map(member -> {
                    try {
                        return Integer.parseInt(member.getId().replace("PM", ""));
                    } catch (NumberFormatException e) {
                        return 0;
                    }
                })
                .max(Integer::compareTo)
                .orElse(0);
        
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

    // Wrapper class for JSON structure
    private static class MembersWrapper {
        private List<PrimeMember> members;

        public MembersWrapper() {
            this.members = new ArrayList<>();
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