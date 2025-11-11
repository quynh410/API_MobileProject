package com.ra.api_project.service;

import com.ra.api_project.dto.request.OrderRequest;
import com.ra.api_project.dto.response.OrderResponse;
import com.ra.api_project.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {
    Page<OrderResponse> getOrdersByUser(Long userId, Pageable pageable);

    OrderResponse getOrderById(Integer id);

    Page<OrderResponse> getAllOrders(Pageable pageable);

    OrderResponse createOrder(OrderRequest request);

    OrderResponse updateOrderStatus(Integer id, OrderStatus status);

    void cancelOrder(Integer id);
}