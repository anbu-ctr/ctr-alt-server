package org.example.dto;

import lombok.Data;

@Data
public class GetPaymentsRequest {
    private String type;
    private FilterData data;

    @Data
    public static class FilterData {
        private Long memberId;
        private Integer start;
        private Integer limit;
    }
}

