package com.ra.api_project.controller;

import com.ra.api_project.dto.request.LoginRequest;
import com.ra.api_project.dto.request.RegisterRequest;
import com.ra.api_project.dto.response.AuthResponse;
import com.ra.api_project.dto.response.BaseResponse;
import com.ra.api_project.dto.response.UserDTO;
import com.ra.api_project.entity.User;
import com.ra.api_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/auth")
@Tag(name = "Authentication", description = "API xác thực người dùng - Đăng ký, Đăng nhập")
public class AuthController {

    private final UserService userService;

    public AuthController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping("/register")
    @Operation(
            summary = "Đăng ký tài khoản mới",
            description = "Tạo tài khoản người dùng mới với đầy đủ thông tin"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "Đăng ký thành công",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Dữ liệu không hợp lệ hoặc email đã tồn tại",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    })
    public ResponseEntity<BaseResponse<String>> register(@Valid @RequestBody RegisterRequest request) {
        try {
            userService.register(request);

            BaseResponse<String> response = BaseResponse.success(
                    HttpStatus.CREATED.value(),
                    "Đăng ký thành công",
                    null
            );

            return new ResponseEntity<>(response, HttpStatus.CREATED);
        } catch (IllegalArgumentException e) {
            BaseResponse<String> response = BaseResponse.error(
                    HttpStatus.BAD_REQUEST.value(),
                    "Đăng ký thất bại",
                    e.getMessage()
            );

            return new ResponseEntity<>(response, HttpStatus.BAD_REQUEST);
        }
    }

    @PostMapping("/login")
    @Operation(
            summary = "Đăng nhập",
            description = "Đăng nhập với email và password để nhận JWT token và thông tin người dùng"
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "Đăng nhập thành công",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "401",
                    description = "Email hoặc password không đúng",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = BaseResponse.class)
                    )
            )
    })
    public ResponseEntity<BaseResponse<AuthResponse>> login(@Valid @RequestBody LoginRequest request) {
        try {
            // Login và nhận User entity
            User user = userService.login(request);

            // Tạo token cho user
            String token = userService.generateToken(user);

            // Tạo UserDTO từ User entity
            UserDTO userDTO = UserDTO.fromUser(user);

            // Tạo AuthResponse với token và user
            AuthResponse authResponse = new AuthResponse(token, userDTO);

            BaseResponse<AuthResponse> response = BaseResponse.success(
                    HttpStatus.OK.value(),
                    "Đăng nhập thành công",
                    authResponse
            );

            return new ResponseEntity<>(response, HttpStatus.OK);
        } catch (Exception e) {
            BaseResponse<AuthResponse> response = BaseResponse.error(
                    HttpStatus.UNAUTHORIZED.value(),
                    "Đăng nhập thất bại",
                    "Email hoặc password không đúng"
            );

            return new ResponseEntity<>(response, HttpStatus.UNAUTHORIZED);
        }
    }
}