package org.example.service;

import org.example.dto.AddMemberRequest;
import org.example.dto.MemberListDto;
import org.example.entity.Member;
import org.example.entity.Member.Plan;
import org.example.repository.MemberRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class MemberService {

    @Autowired
    private MemberRepository memberRepository;

    public Member addMember(final AddMemberRequest request) {

        if (!"create".equals(request.getType())) {
            throw new RuntimeException("Invalid request type");
        }

        final AddMemberRequest.MemberData data = request.getData();
        final Optional<Member> existing = memberRepository.findByPhoneNumber(data.getPhoneNumber());

        if (existing.isPresent()) {
            throw new RuntimeException("Member with this phone number already exists");
        }
        final Member member = new Member();

        member.setFullName(data.getFullName());
        member.setPhoneNumber(data.getPhoneNumber());
        member.setGender(data.getGender());
        member.setPlan(data.getPlan());
        member.setFeeAmount(data.getFeeAmount());
        member.setNotes(data.getNotes());
        final LocalDate joinDate = data.getJoinDate() != null ? data.getJoinDate() : LocalDate.now();

        member.setJoinDate(joinDate);

        if (data.getExpiryDate() != null) {
            member.setExpiryDate(data.getExpiryDate());
        } else {
            member.setExpiryDate(calculateExpiry(joinDate, data.getPlan()));
        }

        return memberRepository.save(member);
    }

    private LocalDate calculateExpiry(final LocalDate joinDate, final Plan plan) {
        return switch (plan) {
            case MONTHLY     -> joinDate.plusMonths(1);
            case QUARTERLY   -> joinDate.plusMonths(3);
            case HALF_YEARLY -> joinDate.plusMonths(6);
            case YEARLY      -> joinDate.plusYears(1);
        };
    }

    public List<MemberListDto> getAllMembers(final int start, final int limit, final Integer status) {
        final Pageable pageable = PageRequest.of((start - 1) / limit, limit,
                Sort.by("createdAt").descending());

        List<Member> members;

        if (status == null || status == 0) {
            members = memberRepository.findAll(pageable).getContent();
        } else if (status == 1) {
            members = memberRepository.findActiveMembers(
                    LocalDate.now(), pageable).getContent();
        } else if (status == 2) {
            members = memberRepository.findExpiredMembers(
                    LocalDate.now(), pageable).getContent();
        } else {
            throw new RuntimeException("Invalid status. Use 1 for active, 2 for expired");
        }

        return members.stream()
                .map(MemberListDto::new)
                .collect(Collectors.toList());
    }
    public long getTotalMembersCount(final Integer status) {
        if (status == null || status == 0) {
            return memberRepository.count();
        } else if (status == 1) {
            return memberRepository.countActiveMembers(LocalDate.now());
        } else if (status == 2) {
            return memberRepository.countExpiredMembers(LocalDate.now());
        }
        return 0;
    }
}