package com.library.service;

import com.library.model.mysql.Member;
import com.library.repository.mysql.MemberRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class MemberService {

    private final MemberRepository memberRepository;

    // Get all members
    public List<Member> getAllMembers() {
        return memberRepository.findAll();
    }

    // Get member by ID
    public Optional<Member> getMemberById(Long id) {
        return memberRepository.findById(id);
    }

    // Add new member
    public Member addMember(Member member) {
        // Check if email already exists
        if (memberRepository.existsByEmail(member.getEmail())) {
            throw new RuntimeException("Email already registered: "
                    + member.getEmail());
        }

        // Generate unique membership ID
        member.setMembershipId("LIB-" +
                UUID.randomUUID().toString().substring(0, 8).toUpperCase());

        // Set membership dates
        member.setMembershipStartDate(LocalDate.now());
        member.setMembershipEndDate(LocalDate.now().plusYears(1));

        // Set default status
        member.setStatus(Member.MemberStatus.ACTIVE);

        return memberRepository.save(member);
    }

    // Update member
    public Member updateMember(Long id, Member memberDetails) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Member not found with id: " + id));

        member.setFirstName(memberDetails.getFirstName());
        member.setLastName(memberDetails.getLastName());
        member.setPhone(memberDetails.getPhone());
        member.setAddress(memberDetails.getAddress());
        member.setStatus(memberDetails.getStatus());

        return memberRepository.save(member);
    }

    // Delete member
    public void deleteMember(Long id) {
        Member member = memberRepository.findById(id)
                .orElseThrow(() ->
                        new RuntimeException("Member not found with id: " + id));
        memberRepository.delete(member);
    }

    // Search members by name
    public List<Member> searchMembers(String keyword) {
        return memberRepository
                .findByFirstNameContainingIgnoreCaseOrLastNameContainingIgnoreCase(
                        keyword, keyword);
    }

    // Get active members
    public List<Member> getActiveMembers() {
        return memberRepository.findByStatus(Member.MemberStatus.ACTIVE);
    }

    // Get member by membershipId
    public Optional<Member> getMemberByMembershipId(String membershipId) {
        return memberRepository.findByMembershipId(membershipId);
    }
}