package com.ra.api_project.dto.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorRequest {

    @NotBlank(message = "Color name is required")
    @Size(min = 2, max = 50, message = "Color name must be between 2 and 50 characters")
    private String colorName;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Integer productId;
}