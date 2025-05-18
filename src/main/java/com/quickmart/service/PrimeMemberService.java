// Service implementation
package com.quickmart.service;

import com.quickmart.model.PrimeMember;
import java.util.List;
import java.util.Optional;

public interface PrimeMemberService {
    List<PrimeMember> getAllMembers();
    Optional<PrimeMember> getMemberById(String id);
    PrimeMember createMember(PrimeMember member);
    PrimeMember updateMember(String id, PrimeMember member);
    void deleteMember(String id);
    boolean existsByEmail(String email);
    boolean existsByContactNumber(String contactNumber);
    void addPoints(String memberId, int points);
    void updateTier(String memberId);
    PrimeMember addPointsFromPurchase(String memberId, double amount);
}