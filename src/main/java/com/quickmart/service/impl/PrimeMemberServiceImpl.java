package com.quickmart.service.impl;

import com.quickmart.model.PrimeMember;
import com.quickmart.service.PrimeMemberService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class PrimeMemberServiceImpl implements PrimeMemberService {

    private final JsonFileStorageService storageService;

    @Autowired
    public PrimeMemberServiceImpl(JsonFileStorageService storageService) {
        this.storageService = storageService;
    }

    @Override
    public List<PrimeMember> getAllMembers() {
        List<PrimeMember> members = storageService.readAll();
        members.sort((a, b) -> {
            try {
                int idA = Integer.parseInt(a.getId().replaceAll("\\D", ""));
                int idB = Integer.parseInt(b.getId().replaceAll("\\D", ""));
                return Integer.compare(idA, idB);
            } catch (Exception e) {
                return a.getId().compareTo(b.getId());
            }
        });
        return members;
    }

    @Override
    public Optional<PrimeMember> getMemberById(String id) {
        return storageService.findById(id);
    }

    @Override
    public PrimeMember createMember(PrimeMember member) {
        if (member.getPoints() < 3000) {
            throw new IllegalArgumentException("Points must be at least 3000 for prime members");
        }
        // Duplicate email check
        if (existsByEmail(member.getEmail())) {
            throw new IllegalArgumentException("A member with this email already exists");
        }
        // Duplicate contact number check
        if (existsByContactNumber(member.getContactNumber())) {
            throw new IllegalArgumentException("A member with this contact number already exists");
        }
        // Validate contact number format
        String contactNumber = member.getContactNumber();
        if (contactNumber == null || !contactNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Contact number must be exactly 10 digits and start with 0");
        }
        member.updateTierBasedOnPoints();
        return storageService.save(member);
    }

    @Override
    public PrimeMember updateMember(String id, PrimeMember member) {
        if (member.getPoints() < 3000) {
            throw new IllegalArgumentException("Points must be at least 3000 for prime members");
        }
        // Duplicate email check (ignore self)
        List<PrimeMember> allMembers = storageService.readAll();
        for (PrimeMember m : allMembers) {
            if (!m.getId().equals(id) && m.getEmail().equals(member.getEmail())) {
                throw new IllegalArgumentException("A member with this email already exists");
            }
            if (!m.getId().equals(id) && m.getContactNumber().equals(member.getContactNumber())) {
                throw new IllegalArgumentException("A member with this contact number already exists");
            }
        }
        // Validate contact number format
        String contactNumber = member.getContactNumber();
        if (contactNumber == null || !contactNumber.matches("^0\\d{9}$")) {
            throw new IllegalArgumentException("Contact number must be exactly 10 digits and start with 0");
        }
        if (id == null || id.trim().isEmpty()) {
            // This is a new member, create it
            return createMember(member);
        }
        // This is an existing member, update it
        PrimeMember existingMember = getMemberById(id)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + id));
        existingMember.setName(member.getName());
        existingMember.setEmail(member.getEmail());
        existingMember.setContactNumber(member.getContactNumber());
        existingMember.setPoints(member.getPoints());
        existingMember.updateTierBasedOnPoints();
        return storageService.save(existingMember);
    }

    @Override
    public void deleteMember(String id) {
        if (!storageService.findById(id).isPresent()) {
            throw new RuntimeException("Member not found with id: " + id);
        }
        storageService.delete(id);
    }

    @Override
    public boolean existsByEmail(String email) {
        return storageService.existsByEmail(email);
    }

    @Override
    public boolean existsByContactNumber(String contactNumber) {
        return storageService.existsByContactNumber(contactNumber);
    }

    @Override
    public void addPoints(String memberId, int points) {
        PrimeMember member = storageService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
        
        member.setPoints(member.getPoints() + points);
        member.updateTierBasedOnPoints();
        storageService.save(member);
    }

    @Override
    public void updateTier(String memberId) {
        PrimeMember member = storageService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
        
        member.updateTierBasedOnPoints();
        storageService.save(member);
    }

    @Override
    public PrimeMember addPointsFromPurchase(String memberId, double amount) {
        PrimeMember member = storageService.findById(memberId)
                .orElseThrow(() -> new RuntimeException("Member not found with id: " + memberId));
        
        member.addPointsFromPurchase(amount);
        return storageService.save(member);
    }
} 