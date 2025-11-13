package com.ra.api_project.controller;

import com.ra.api_project.dto.request.UpdateProfileRequest;
import com.ra.api_project.dto.response.UserDTO;
import com.ra.api_project.entity.User;
import com.ra.api_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/user")
@RequiredArgsConstructor
@Tag(name = "User", description = "API quản lý người dùng")
public class UserController {

    private final UserService userService;

    @PutMapping(value = "/profile/{userId}", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    @Operation(
            summary = "Cập nhật thông tin người dùng",
            description = "Cập nhật profile bao gồm cả upload avatar lên Cloudinary",
            responses = {
                    @ApiResponse(
                            responseCode = "200",
                            description = "Cập nhật thành công",
                            content = @Content(schema = @Schema(implementation = UserDTO.class))
                    ),
                    @ApiResponse(
                            responseCode = "404",
                            description = "Không tìm thấy người dùng"
                    )
            }
    )
    public ResponseEntity<?> updateProfile(
            @Parameter(description = "ID người dùng", required = true)
            @PathVariable Long userId,

            @ModelAttribute UpdateProfileRequest request
    ) {
        try {
            User updatedUser = userService.updateProfile(userId, request);
            UserDTO userDTO = UserDTO.fromUser(updatedUser);

            return ResponseEntity.ok(userDTO);
        } catch (IllegalArgumentException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        } catch (RuntimeException e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }

    @GetMapping("/profile")
    @Operation(summary = "Lấy thông tin người dùng hiện tại")
    public ResponseEntity<?> getCurrentProfile() {
        try {
            User currentUser = userService.getCurrentUser();
            if (currentUser == null) {
                return ResponseEntity.status(401).body("Chưa đăng nhập");
            }
            UserDTO userDTO = UserDTO.fromUser(currentUser);
            return ResponseEntity.ok(userDTO);
        } catch (Exception e) {
            return ResponseEntity.internalServerError().body("Lỗi server: " + e.getMessage());
        }
    }
}