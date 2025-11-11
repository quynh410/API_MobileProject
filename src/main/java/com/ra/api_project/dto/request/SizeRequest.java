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
public class SizeRequest {

    @NotBlank(message = "Size name is required")
    @Size(min = 1, max = 50, message = "Size name must be between 1 and 50 characters")
    private String sizeName;

    @NotNull(message = "Product ID is required")
    @Positive(message = "Product ID must be positive")
    private Integer productId;
}