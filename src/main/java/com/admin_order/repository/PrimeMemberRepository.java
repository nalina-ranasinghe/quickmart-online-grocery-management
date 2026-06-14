package com.admin_order.repository;

import com.admin_order.model.PrimeMember;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface PrimeMemberRepository extends JpaRepository<PrimeMember, Long> {
    Optional<PrimeMember> findByUsername(String username);
    Optional<PrimeMember> findByEmail(String email);
    List<PrimeMember> findByTier(String tier);
    boolean existsByUsername(String username);
    boolean existsByEmail(String email);
}
