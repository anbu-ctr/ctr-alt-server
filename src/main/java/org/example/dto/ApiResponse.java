package org.example.dto;

import com.fasterxml.jackson.annotation.JsonInclude;
import lombok.Data;

@Data
@JsonInclude(JsonInclude.Include.NON_NULL)
public class ApiResponse {

    private int code;
    private Object data;

    public static ApiResponse success(final Object data) {
        final ApiResponse response = new ApiResponse();

        response.setCode(0);
        response.setData(data);

        return response;
    }
    public static ApiResponse error(final String errorMessage) {
        final ApiResponse response = new ApiResponse();

        response.setCode(1);
        final java.util.Map<String, String> errorData = new java.util.HashMap<>();

        errorData.put("error", errorMessage);
        response.setData(errorData);

        return response;
    }
}