package com.ra.api_project.dto.request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartRequest {

    @NotNull(message = "User ID is required")
    @Positive(message = "User ID must be positive")
    private Integer userId;
}