package org.example.dto;

import lombok.Data;

@Data
public class GetMembersRequest {
    private String type;
    private MembersData data;

    @Data
    public static class MembersData {
        private int start;
        private int limit;
        private Integer status;
    }
}