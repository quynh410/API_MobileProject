package com.ra.api_project.repository;

import com.ra.api_project.entity.Order;
import com.ra.api_project.enums.OrderStatus;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {
    Page<Order> findByUserId(Long userId, Pageable pageable);

    Page<Order> findByOrderStatus(OrderStatus orderStatus, Pageable pageable);

    @Query("SELECT o FROM Order o LEFT JOIN FETCH o.orderItems WHERE o.orderId = :id")
    Optional<Order> findByIdWithItems(@Param("id") Integer id);
}