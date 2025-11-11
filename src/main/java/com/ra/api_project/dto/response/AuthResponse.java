package com.ra.api_project.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Response đăng nhập")
public class AuthResponse {

    @Schema(description = "JWT Token", example = "eyJhbGciOiJIUzI1NiIsInR5cCI6IkpXVCJ9...")
    private String token;

    @Schema(description = "Loại token", example = "Bearer")
    private String tokenType = "Bearer";

    @Schema(description = "Thông tin người dùng")
    private UserDTO user;

    public AuthResponse(String token) {
        this.token = token;
        this.tokenType = "Bearer";
    }

    public AuthResponse(String token, UserDTO user) {
        this.token = token;
        this.tokenType = "Bearer";
        this.user = user;
    }
}