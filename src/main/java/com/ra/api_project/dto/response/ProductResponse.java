package com.ra.api_project.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ProductResponse {

    private Integer productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private Integer stockQuantity;
    private String imageUrl;
    private Integer categoryId;
    private String categoryName;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}