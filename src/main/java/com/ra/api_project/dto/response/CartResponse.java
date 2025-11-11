package com.ra.api_project.dto.response;

import lombok.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class CartResponse {

    private Integer cartId;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private List<CartItemResponse> cartItems;
    private BigDecimal totalAmount;
}