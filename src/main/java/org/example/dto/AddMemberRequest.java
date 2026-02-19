package org.example.dto;

import lombok.Data;
import org.example.entity.Member.Gender;
import org.example.entity.Member.Plan;
import java.time.LocalDate;

@Data
public class AddMemberRequest {
    private String type;
    private MemberData data;

    @Data
    public static class MemberData {
        private String fullName;
        private String phoneNumber;
        private Gender gender;
        private Plan plan;
        private LocalDate joinDate;
        private LocalDate expiryDate;
        private Double feeAmount;
        private String notes;
    }
}