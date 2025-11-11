package com.ra.api_project.controller;

import com.ra.api_project.dto.request.UpdateProfileRequest;
import com.ra.api_project.entity.User;
import com.ra.api_project.repository.UserRepository;
import com.ra.api_project.service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/account")
@Tag(name = "Account", description = "API quản lý tài khoản người dùng")
@SecurityRequirement(name = "Bearer Authentication")
public class AccountController {
    private final UserService userService;
    private final UserRepository userRepository;

    public AccountController(UserService userService, UserRepository userRepository) {
        this.userService = userService;
        this.userRepository = userRepository;
    }

    @GetMapping("/me")
    @Operation(summary = "Lấy thông tin tài khoản", description = "Lấy thông tin tài khoản người dùng hiện tại")
    public ResponseEntity<?> me(@AuthenticationPrincipal UserDetails userDetails) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        User u = userRepository.findByEmail(userDetails.getUsername()).orElse(null);
        if (u == null) return ResponseEntity.notFound().build();
        u.setPassword(null);
        return ResponseEntity.ok(u);
    }

    @PutMapping("/me")
    @Operation(summary = "Cập nhật thông tin", description = "Cập nhật thông tin cá nhân người dùng")
    public ResponseEntity<?> updateProfile(@AuthenticationPrincipal UserDetails userDetails,
                                           @Valid @RequestBody UpdateProfileRequest req) {
        if (userDetails == null) return ResponseEntity.status(401).build();
        User u = userRepository.findByEmail(userDetails.getUsername()).orElseThrow();
        User updated = userService.updateProfile(u.getId(), req);
        updated.setPassword(null);
        return ResponseEntity.ok(updated);
    }
}