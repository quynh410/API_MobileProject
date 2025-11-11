package com.ra.api_project.dto.request;

import com.ra.api_project.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Request đăng ký tài khoản mới")
public class RegisterRequest {

    @Schema(description = "Email đăng ký", example = "example@gmail.com")
    @NotBlank(message = "Email không được để trống")
    @Email(message = "Email không đúng định dạng")
    @Size(max = 255, message = "Email không được vượt quá 255 ký tự")
    @Pattern(regexp = "^[a-zA-Z0-9._%+-]+@[a-zA-Z0-9.-]+\\.[a-zA-Z]{2,}$",
            message = "Email phải có định dạng hợp lệ")
    private String email;

    @Schema(description = "Mật khẩu (ít nhất 1 chữ hoa, 1 chữ thường, 1 số)", example = "Password123")
    @NotBlank(message = "Password không được để trống")
    @Size(min = 6, max = 100, message = "Password phải từ 6-100 ký tự")
    @Pattern(regexp = "^(?=.*[a-z])(?=.*[A-Z])(?=.*\\d).*$",
            message = "Password phải chứa ít nhất 1 chữ hoa, 1 chữ thường và 1 số")
    private String password;

    @Schema(description = "Họ", example = "Nguyen")
    @NotBlank(message = "Họ không được để trống")
    @Size(min = 2, max = 50, message = "Họ phải từ 2-50 ký tự")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$",
            message = "Họ chỉ được chứa chữ cái và khoảng trắng")
    private String lastName;

    @Schema(description = "Tên", example = "Van A")
    @NotBlank(message = "Tên không được để trống")
    @Size(min = 2, max = 50, message = "Tên phải từ 2-50 ký tự")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$",
            message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String firstName;

    @Schema(description = "Số điện thoại", example = "0123456789")
    @NotBlank(message = "Số điện thoại không được để trống")
    @Pattern(regexp = "^[0-9]{10,11}$",
            message = "Số điện thoại phải có 10-11 chữ số")
    private String phoneNumber;

    @Schema(description = "Giới tính", example = "MALE", allowableValues = {"MALE", "FEMALE"})
    @NotNull(message = "Giới tính không được để trống")
    private Gender gender;

    @Schema(description = "Địa chỉ", example = "123 Nguyen Trai, Ha Noi")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @Schema(description = "Avatar URL", example = "https://example.com/avatar.jpg")
    @Size(max = 255, message = "Avatar URL không được vượt quá 255 ký tự")

    private String avatarUrl;
}