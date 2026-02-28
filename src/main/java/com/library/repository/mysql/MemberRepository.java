package com.library.repository.mysql;

import com.library.model.mysql.Member;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MemberRepository extends JpaRepository<Member, Long> {

    // Find member by email
    Optional<Member> findByEmail(String email);

    // Find member by membershipId
    Optional<Member> findByMembershipId(String membershipId);

    // Find members by status
    List<Member> findByStatus(Member.MemberStatus status);

    // Search by name
    List<Member> findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
            String firstName, String lastName);

    // Check if email exists
    boolean existsByEmail(String email);
}