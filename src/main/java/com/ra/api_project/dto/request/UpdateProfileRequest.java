package com.ra.api_project.dto.request;

import com.ra.api_project.enums.Gender;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.web.multipart.MultipartFile;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Schema(description = "Request cập nhật thông tin cá nhân")
public class UpdateProfileRequest {

    @Schema(description = "Họ và tên đầy đủ", example = "Nguyen Van A")
    @Size(min = 2, max = 100, message = "Họ tên phải từ 2-100 ký tự")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$",
            message = "Họ tên chỉ được chứa chữ cái và khoảng trắng")
    private String fullName;

    @Schema(description = "Tên", example = "A")
    @Size(min = 2, max = 50, message = "Tên phải từ 2-50 ký tự")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$",
            message = "Tên chỉ được chứa chữ cái và khoảng trắng")
    private String firstName;

    @Schema(description = "Họ", example = "Nguyen Van")
    @Size(min = 2, max = 50, message = "Họ phải từ 2-50 ký tự")
    @Pattern(regexp = "^[a-zA-ZÀ-ỹ\\s]+$",
            message = "Họ chỉ được chứa chữ cái và khoảng trắng")
    private String lastName;

    @Schema(description = "Giới tính", example = "MALE")
    private Gender gender;

    @Schema(description = "Số điện thoại", example = "0987654321")
    @Pattern(regexp = "^[0-9]{10,11}$",
            message = "Số điện thoại phải có 10-11 chữ số")
    private String phoneNumber;

    @Schema(description = "Địa chỉ", example = "456 Le Loi, Ho Chi Minh")
    @Size(max = 500, message = "Địa chỉ không được vượt quá 500 ký tự")
    private String address;

    @Schema(description = "Avatar URL (nếu không upload file)", example = "https://example.com/new-avatar.jpg")
    @Size(max = 255, message = "Avatar URL không được vượt quá 255 ký tự")
    private String avatarUrl;

    // ✅ QUAN TRỌNG: Field này để nhận file upload từ form-data
    @Schema(description = "File avatar để upload lên Cloudinary", type = "string", format = "binary")
    private MultipartFile avatar;
}