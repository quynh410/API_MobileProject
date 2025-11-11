package com.ra.api_project.repository;

import com.ra.api_project.entity.CartItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface CartItemRepository extends JpaRepository<CartItem, Integer> {

    List<CartItem> findByCartCartId(Integer cartId);

    Optional<CartItem> findByCartCartIdAndProductProductId(Integer cartId, Integer productId);

    void deleteByCartCartId(Integer cartId);
}