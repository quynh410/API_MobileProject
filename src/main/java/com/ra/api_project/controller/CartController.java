package com.ra.api_project.controller;

import com.ra.api_project.dto.request.CartItemRequest;
import com.ra.api_project.dto.response.BaseResponse;
import com.ra.api_project.dto.response.CartResponse;
import com.ra.api_project.service.CartService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/carts")
@RequiredArgsConstructor
public class CartController {

    private final CartService cartService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<CartResponse>> getCartByUserId(@PathVariable Long userId) {  // ✅ Đổi Integer -> Long
        CartResponse cart = cartService.getCartByUserId(userId);
        return ResponseEntity.ok(BaseResponse.<CartResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get cart successfully")
                .data(cart)
                .build());
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<BaseResponse<CartResponse>> addItemToCart(
            @PathVariable Long userId,  // ✅ Đổi Integer -> Long
            @Valid @RequestBody CartItemRequest request) {

        CartResponse cart = cartService.addItemToCart(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<CartResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Item added to cart successfully")
                        .data(cart)
                        .build());
    }

    @PutMapping("/user/{userId}/items/{cartItemId}")
    public ResponseEntity<BaseResponse<CartResponse>> updateCartItem(
            @PathVariable Long userId,  // ✅ Đổi Integer -> Long
            @PathVariable Integer cartItemId,
            @Valid @RequestBody CartItemRequest request) {

        CartResponse cart = cartService.updateCartItem(userId, cartItemId, request);
        return ResponseEntity.ok(BaseResponse.<CartResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cart item updated successfully")
                .data(cart)
                .build());
    }

    @DeleteMapping("/user/{userId}/items/{cartItemId}")
    public ResponseEntity<BaseResponse<Void>> removeItemFromCart(
            @PathVariable Long userId,  // ✅ Đổi Integer -> Long
            @PathVariable Integer cartItemId) {

        cartService.removeItemFromCart(userId, cartItemId);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item removed from cart successfully")
                .build());
    }

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<BaseResponse<Void>> clearCart(@PathVariable Long userId) {  // ✅ Đổi Integer -> Long
        cartService.clearCart(userId);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Cart cleared successfully")
                .build());
    }
}