package com.ra.api_project.dto.response;

import com.fasterxml.jackson.annotation.JsonInclude;
import com.ra.api_project.entity.User;
import com.ra.api_project.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@JsonInclude(JsonInclude.Include.NON_NULL)
@Schema(description = "Thông tin cơ bản người dùng")
public class UserDTO {

    @Schema(description = "ID người dùng", example = "1")
    private Long id;

    @Schema(description = "Họ và tên đầy đủ", example = "Nguyen Van A")
    private String name;

    @Schema(description = "Email", example = "example@gmail.com")
    private String email;

    @Schema(description = "Số điện thoại", example = "0123456789")
    private String phoneNumber;

    @Schema(description = "Giới tính", example = "MALE")
    private Gender gender;

    @Schema(description = "Avatar URL")
    private String avatarUrl;

    // Method chuyển từ User Entity sang UserDTO
    public static UserDTO fromUser(User user) {
        if (user == null) return null;

        return UserDTO.builder()
                .id(user.getId())
                .name(user.getFullName())
                .email(user.getEmail())
                .phoneNumber(user.getPhoneNumber())
                .gender(user.getGender())
                .avatarUrl(user.getAvatarUrl())
                .build();
    }
}