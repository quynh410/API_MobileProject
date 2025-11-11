package com.ra.api_project.controller;

import com.ra.api_project.dto.request.OrderRequest;
import com.ra.api_project.dto.request.UpdateOrderStatusRequest;
import com.ra.api_project.dto.response.BaseResponse;
import com.ra.api_project.dto.response.OrderResponse;
import com.ra.api_project.service.OrderService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/orders")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<Page<OrderResponse>>> getOrdersByUser(
            @PathVariable Long userId,  // Đổi từ Integer sang Long
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = orderService.getOrdersByUser(userId, pageable);
        return ResponseEntity.ok(BaseResponse.<Page<OrderResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get user orders successfully")
                .data(orders)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<OrderResponse>> getOrderById(@PathVariable Integer id) {
        OrderResponse order = orderService.getOrderById(id);
        return ResponseEntity.ok(BaseResponse.<OrderResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get order successfully")
                .data(order)
                .build());
    }

    @GetMapping
    public ResponseEntity<BaseResponse<Page<OrderResponse>>> getAllOrders(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        Pageable pageable = PageRequest.of(page, size);
        Page<OrderResponse> orders = orderService.getAllOrders(pageable);
        return ResponseEntity.ok(BaseResponse.<Page<OrderResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all orders successfully")
                .data(orders)
                .build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<OrderResponse>> createOrder(
            @Valid @RequestBody OrderRequest request) {

        OrderResponse order = orderService.createOrder(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<OrderResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Order created successfully")
                        .data(order)
                        .build());
    }

    @PatchMapping("/{id}/status")
    public ResponseEntity<BaseResponse<OrderResponse>> updateOrderStatus(
            @PathVariable Integer id,
            @Valid @RequestBody UpdateOrderStatusRequest request) {

        OrderResponse order = orderService.updateOrderStatus(id, request.getOrderStatus());
        return ResponseEntity.ok(BaseResponse.<OrderResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Order status updated successfully")
                .data(order)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> cancelOrder(@PathVariable Integer id) {
        orderService.cancelOrder(id);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Order cancelled successfully")
                .build());
    }
}