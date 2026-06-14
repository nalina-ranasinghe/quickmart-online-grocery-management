package com.admin_order.controller;

import com.admin_order.model.PrimeMember;
import com.admin_order.repository.PrimeMemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/api/prime-members")
@CrossOrigin
public class PrimeMemberController {

    @Autowired
    private PrimeMemberRepository primeMemberRepository;

    // GET all prime members
    @GetMapping
    public List<PrimeMember> getAllMembers() {
        return primeMemberRepository.findAll();
    }

    // GET a single member
    @GetMapping("/{id}")
    public ResponseEntity<PrimeMember> getMember(@PathVariable Long id) {
        return primeMemberRepository.findById(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST: Add a new prime member
    @PostMapping
    public ResponseEntity<?> addMember(@RequestBody PrimeMember member) {
        if (primeMemberRepository.existsByUsername(member.getUsername())) {
            return ResponseEntity.badRequest().body(Map.of("error", "User is already a Prime Member."));
        }
        // Ensure tier-based discount is set
        member.setTier(member.getTier() != null ? member.getTier() : "SILVER");
        if (member.getPoints() == null) member.setPoints(0);
        if (member.getStatus() == null) member.setStatus("ACTIVE");

        PrimeMember saved = primeMemberRepository.save(member);
        return ResponseEntity.ok(saved);
    }

    // PUT: Update tier, points, or status
    @PutMapping("/{id}")
    public ResponseEntity<?> updateMember(@PathVariable Long id, @RequestBody PrimeMember updated) {
        return primeMemberRepository.findById(id).map(existing -> {
            if (updated.getTier() != null) {
                existing.setTier(updated.getTier()); // auto-sets discountRate via setTier()
            }
            if (updated.getPoints() != null) {
                existing.setPoints(updated.getPoints());
            }
            if (updated.getStatus() != null) {
                existing.setStatus(updated.getStatus());
            }
            if (updated.getEmail() != null) {
                existing.setEmail(updated.getEmail());
            }
            return ResponseEntity.ok(primeMemberRepository.save(existing));
        }).orElse(ResponseEntity.notFound().build());
    }

    // DELETE: Remove a prime member
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteMember(@PathVariable Long id) {
        if (!primeMemberRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        primeMemberRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }

    // GET: Stats summary
    @GetMapping("/stats")
    public ResponseEntity<Map<String, Object>> getStats() {
        List<PrimeMember> all = primeMemberRepository.findAll();
        long total = all.size();
        long platinum = all.stream().filter(m -> "PLATINUM".equalsIgnoreCase(m.getTier())).count();
        long gold = all.stream().filter(m -> "GOLD".equalsIgnoreCase(m.getTier())).count();
        long silver = all.stream().filter(m -> "SILVER".equalsIgnoreCase(m.getTier())).count();
        long active = all.stream().filter(m -> "ACTIVE".equalsIgnoreCase(m.getStatus())).count();
        int totalPoints = all.stream().mapToInt(m -> m.getPoints() != null ? m.getPoints() : 0).sum();

        return ResponseEntity.ok(Map.of(
                "total", total,
                "platinum", platinum,
                "gold", gold,
                "silver", silver,
                "active", active,
                "totalPoints", totalPoints
        ));
    }
}
