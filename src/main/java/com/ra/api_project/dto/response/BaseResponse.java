package com.ra.api_project.dto.response;

import lombok.*;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class BaseResponse<T> {
    private int statusCode;
    private String message;
    private T data;
    private String error;

    // Response thành công
    public static <T> BaseResponse<T> success(int statusCode, String message, T data) {
        return BaseResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .data(data)
                .build();
    }

    // Response lỗi
    public static <T> BaseResponse<T> error(int statusCode, String message, String error) {
        return BaseResponse.<T>builder()
                .statusCode(statusCode)
                .message(message)
                .error(error)
                .build();
    }
}