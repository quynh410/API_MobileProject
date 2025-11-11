package com.ra.api_project.dto.response;

import com.ra.api_project.enums.OrderStatus;
import lombok.*;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class OrderResponse {

    private Long orderId;
    private Long userId;
    private String userName;
    private BigDecimal totalPrice;
    private OrderStatus orderStatus;
    private String shippingAddress;
    private List<OrderItemResponse> orderItems;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}