package com.ra.api_project.service;

import com.ra.api_project.dto.request.*;
import com.ra.api_project.entity.User;

public interface UserService {
    void register(RegisterRequest req);
    User login(LoginRequest req);
    String generateToken(User user);
    User getCurrentUser();
    User updateProfile(Long userId, UpdateProfileRequest req);
}