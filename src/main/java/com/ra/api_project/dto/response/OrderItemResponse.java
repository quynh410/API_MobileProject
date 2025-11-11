package com.ra.api_project.dto.response;

import lombok.*;

import java.math.BigDecimal;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderItemResponse {

    private Integer orderItemId;
    private Integer productId;
    private String productName;
    private Integer quantity;
    private BigDecimal price;
}