package org.example.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetMembersRequest extends APIRequest {
    private MembersData data;

    @Data
    public static class MembersData {
        private int start;
        private int limit;
        private Integer status;
    }
}