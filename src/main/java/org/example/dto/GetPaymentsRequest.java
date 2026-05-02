package org.example.dto;

import lombok.Data;
import lombok.EqualsAndHashCode;

@EqualsAndHashCode(callSuper = true)
@Data
public class GetPaymentsRequest extends APIRequest {
    private FilterData data;

    @Data
    public static class FilterData {
        private Long memberId;
        private Integer start;
        private Integer limit;
    }
}

