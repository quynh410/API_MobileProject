package com.ra.api_project.dto.response;

import lombok.*;
import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartItemResponse {

    private Integer cartItemId;
    private Integer productId;
    private String productName;
    private String imageUrl;
    private Integer quantity;
    private BigDecimal price;
    private BigDecimal subtotal;
}