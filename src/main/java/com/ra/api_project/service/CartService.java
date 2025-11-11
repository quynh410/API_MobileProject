package com.ra.api_project.service;

import com.ra.api_project.dto.request.CartItemRequest;
import com.ra.api_project.dto.response.CartResponse;

public interface CartService {

    CartResponse getCartByUserId(Long userId);  // ✅ Đổi Integer -> Long

    CartResponse addItemToCart(Long userId, CartItemRequest request);  // ✅ Đổi Integer -> Long

    CartResponse updateCartItem(Long userId, Integer cartItemId, CartItemRequest request);  // ✅ Đổi Integer -> Long

    void removeItemFromCart(Long userId, Integer cartItemId);  // ✅ Đổi Integer -> Long

    void clearCart(Long userId);  // ✅ Đổi Integer -> Long
}