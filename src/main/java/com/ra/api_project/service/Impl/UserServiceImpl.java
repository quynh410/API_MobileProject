package com.ra.api_project.service.Impl;

import com.cloudinary.Cloudinary;
import com.cloudinary.utils.ObjectUtils;
import com.ra.api_project.config.JwtUtil;
import com.ra.api_project.dto.request.LoginRequest;
import com.ra.api_project.dto.request.RegisterRequest;
import com.ra.api_project.dto.request.UpdateProfileRequest;
import com.ra.api_project.entity.User;
import com.ra.api_project.repository.UserRepository;
import com.ra.api_project.service.UserService;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Map;

@Service
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final AuthenticationManager authenticationManager;
    private final JwtUtil jwtUtil;

    @Autowired
    private Cloudinary cloudinary;

    public UserServiceImpl(UserRepository userRepository,
                           PasswordEncoder passwordEncoder,
                           AuthenticationManager authenticationManager,
                           JwtUtil jwtUtil) {
        this.userRepository = userRepository;
        this.passwordEncoder = passwordEncoder;
        this.authenticationManager = authenticationManager;
        this.jwtUtil = jwtUtil;
    }

    @Override
    public void register(RegisterRequest req) {
        if (userRepository.existsByEmail(req.getEmail())) {
            throw new IllegalArgumentException("Email đã được sử dụng");
        }

        if (userRepository.existsByPhoneNumber(req.getPhoneNumber())) {
            throw new IllegalArgumentException("Số điện thoại đã được sử dụng");
        }

        User user = new User();
        user.setEmail(req.getEmail());
        user.setPassword(passwordEncoder.encode(req.getPassword()));
        user.setLastName(req.getLastName());
        user.setFirstName(req.getFirstName());
        user.setPhoneNumber(req.getPhoneNumber());
        user.setGender(req.getGender());
        user.setAddress(req.getAddress());
        user.setAvatarUrl(req.getAvatarUrl());

        // Tự động tạo fullName từ lastName + firstName
        user.setFullName(req.getLastName() + " " + req.getFirstName());

        user.setRole("ROLE_USER");

        userRepository.save(user);
    }

    @Override
    public User login(LoginRequest req) {
        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(req.getEmail(), req.getPassword())
        );

        SecurityContextHolder.getContext().setAuthentication(authentication);

        // Trả về User entity
        return userRepository.findByEmail(req.getEmail())
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));
    }

    @Override
    public String generateToken(User user) {
        // Tạo UserDetails từ User entity
        UserDetails userDetails = org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .roles(user.getRole().replace("ROLE_", ""))
                .build();

        return jwtUtil.generateToken(userDetails);
    }

    @Override
    public User getCurrentUser() {
        Object principal = SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        if (principal instanceof UserDetails) {
            String email = ((UserDetails) principal).getUsername();
            return userRepository.findByEmail(email).orElse(null);
        }
        return null;
    }

    @Override
    public User updateProfile(Long userId, UpdateProfileRequest req) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new IllegalArgumentException("Không tìm thấy người dùng"));

        if (req.getFullName() != null && !req.getFullName().isBlank()) {
            user.setFullName(req.getFullName());
        }

        if (req.getFirstName() != null && !req.getFirstName().isBlank()) {
            user.setFirstName(req.getFirstName());
        }

        if (req.getLastName() != null && !req.getLastName().isBlank()) {
            user.setLastName(req.getLastName());
        }

        if (req.getGender() != null) {
            user.setGender(req.getGender());
        }

        if (req.getPhoneNumber() != null && !req.getPhoneNumber().isBlank()) {
            user.setPhoneNumber(req.getPhoneNumber());
        }

        if (req.getAddress() != null && !req.getAddress().isBlank()) {
            user.setAddress(req.getAddress());
        }

        //  SỬA: Upload avatar mới lên Cloudinary nếu có
        String avatarUrl = user.getAvatarUrl();
        MultipartFile avatar = req.getAvatar();
        if (avatar != null && !avatar.isEmpty()) {
            try {
                if (user.getAvatarUrl() != null && !user.getAvatarUrl().isEmpty()) {
                    String publicId = extractPublicIdFromUrl(user.getAvatarUrl());
                    if (publicId != null) {
                        cloudinary.uploader().destroy(publicId, ObjectUtils.emptyMap());
                    }
                }

                Map uploadResult = cloudinary.uploader().upload(avatar.getBytes(),
                        ObjectUtils.asMap(
                                "folder", "user_avatars",
                                "resource_type", "auto"
                        ));
                avatarUrl = uploadResult.get("secure_url").toString();
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException("Failed to upload avatar: " + e.getMessage());
            }
        }

        user.setAvatarUrl(avatarUrl);

        return userRepository.save(user);
    }

    private String extractPublicIdFromUrl(String imageUrl) {
        try {
            String[] parts = imageUrl.split("/upload/");
            if (parts.length > 1) {
                String pathAfterUpload = parts[1];
                String[] pathParts = pathAfterUpload.split("/", 2);
                if (pathParts.length > 1) {
                    String publicIdWithExtension = pathParts[1];
                    int lastDotIndex = publicIdWithExtension.lastIndexOf('.');
                    if (lastDotIndex > 0) {
                        return publicIdWithExtension.substring(0, lastDotIndex);
                    }
                    return publicIdWithExtension;
                }
            }
        } catch (Exception e) {
            System.err.println("Failed to extract public_id from URL: " + e.getMessage());
        }
        return null;
    }
}