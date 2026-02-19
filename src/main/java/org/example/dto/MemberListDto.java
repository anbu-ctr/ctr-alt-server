package org.example.dto;

import lombok.Data;
import org.example.entity.Member;
import java.time.LocalDate;

@Data
public class MemberListDto {

    private Long id;
    private String fullName;
    private String phoneNumber;
    private String plan;
    private String status;

    public MemberListDto(final Member member) {
        this.id = member.getId();
        this.fullName = member.getFullName();
        this.phoneNumber = member.getPhoneNumber();
        this.plan = member.getPlan().name();
        this.status = member.getExpiryDate().isBefore(LocalDate.now())
                ? "EXPIRED"
                : "ACTIVE";
    }
}