package com.ra.api_project.service.Impl;

import com.ra.api_project.dto.request.OrderItemRequest;
import com.ra.api_project.dto.request.OrderRequest;
import com.ra.api_project.dto.response.OrderResponse;
import com.ra.api_project.dto.response.OrderItemResponse;
import com.ra.api_project.entity.Order;
import com.ra.api_project.entity.OrderItem;
import com.ra.api_project.entity.Product;
import com.ra.api_project.entity.User;
import com.ra.api_project.enums.OrderStatus;
import com.ra.api_project.repository.OrderRepository;
import com.ra.api_project.repository.ProductRepository;
import com.ra.api_project.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements com.ra.api_project.service.OrderService {

    private final OrderRepository orderRepository;
    private final UserRepository userRepository;
    private final ProductRepository productRepository;

    @Override
    public Page<OrderResponse> getOrdersByUser(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(this::convertToResponse);
    }

    @Override
    public OrderResponse getOrderById(Integer id) {
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));
        return convertToResponse(order);
    }

    @Override
    public Page<OrderResponse> getAllOrders(Pageable pageable) {
        return orderRepository.findAll(pageable)
                .map(this::convertToResponse);
    }

    @Override
    @Transactional
    public OrderResponse createOrder(OrderRequest request) {
        User user = userRepository.findById(request.getUserId())
                .orElseThrow(() -> new RuntimeException("User not found with id: " + request.getUserId()));

        Order order = Order.builder()
                .user(user)
                .shippingAddress(request.getShippingAddress())
                .orderStatus(OrderStatus.PENDING)
                .orderItems(new ArrayList<>())
                .build();

        BigDecimal totalPrice = BigDecimal.ZERO;
        List<OrderItem> orderItems = new ArrayList<>();

        for (OrderItemRequest itemRequest : request.getOrderItems()) {
            Product product = productRepository.findById(itemRequest.getProductId())
                    .orElseThrow(() -> new RuntimeException("Product not found with id: " + itemRequest.getProductId()));

            if (product.getStockQuantity() < itemRequest.getQuantity()) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
            }

            OrderItem orderItem = OrderItem.builder()
                    .order(order)
                    .product(product)
                    .quantity(itemRequest.getQuantity())
                    .price(product.getPrice())
                    .build();

            orderItems.add(orderItem);
            totalPrice = totalPrice.add(product.getPrice().multiply(BigDecimal.valueOf(itemRequest.getQuantity())));

            product.setStockQuantity(product.getStockQuantity() - itemRequest.getQuantity());
            productRepository.save(product);
        }

        order.setOrderItems(orderItems);
        order.setTotalPrice(totalPrice);

        Order savedOrder = orderRepository.save(order);
        return convertToResponse(savedOrder);
    }

    @Override
    @Transactional
    public OrderResponse updateOrderStatus(Integer id, OrderStatus status) {
        Order order = orderRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        order.setOrderStatus(status);
        Order updatedOrder = orderRepository.save(order);
        return convertToResponse(updatedOrder);
    }

    @Override
    @Transactional
    public void cancelOrder(Integer id) {
        Order order = orderRepository.findByIdWithItems(id)
                .orElseThrow(() -> new RuntimeException("Order not found with id: " + id));

        if (order.getOrderStatus() != OrderStatus.PENDING) {
            throw new RuntimeException("Can only cancel pending orders");
        }

        for (OrderItem item : order.getOrderItems()) {
            Product product = item.getProduct();
            product.setStockQuantity(product.getStockQuantity() + item.getQuantity());
            productRepository.save(product);
        }

        order.setOrderStatus(OrderStatus.CANCELLED);
        orderRepository.save(order);
    }

    private OrderResponse convertToResponse(Order order) {
        List<OrderItemResponse> itemResponses = order.getOrderItems() != null
                ? order.getOrderItems().stream()
                .map(this::convertToItemResponse)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return OrderResponse.builder()
                .orderId(order.getOrderId())
                .userId(order.getUser().getId())
                .userName(order.getUser().getFullName())
                .totalPrice(order.getTotalPrice())
                .orderStatus(order.getOrderStatus())
                .shippingAddress(order.getShippingAddress())
                .orderItems(itemResponses)
                .createdAt(order.getCreatedAt())
                .updatedAt(order.getUpdatedAt())
                .build();
    }

    private OrderItemResponse convertToItemResponse(OrderItem item) {
        return OrderItemResponse.builder()
                .orderItemId(item.getOrderItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getProductName())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .build();
    }
}