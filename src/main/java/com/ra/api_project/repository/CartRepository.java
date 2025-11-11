package com.ra.api_project.repository;

import com.ra.api_project.entity.Cart;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface CartRepository extends JpaRepository<Cart, Integer> {

    Optional<Cart> findByUserId(Long userId);

    @Query("SELECT c FROM Cart c LEFT JOIN FETCH c.cartItems WHERE c.user.id = :userId")
    Optional<Cart> findByUserIdWithItems(@Param("userId") Long userId);

    boolean existsByUserId(Long userId);
}