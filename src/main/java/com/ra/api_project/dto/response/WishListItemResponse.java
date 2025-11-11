package com.ra.api_project.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListItemResponse {

    private Integer wishListItemId;
    private Integer productId;
    private String productName;
    private String description;
    private BigDecimal price;
    private String imageUrl;
    private Integer stockQuantity;
    private LocalDateTime addedAt;
}