package com.ra.api_project.service.Impl;

import com.ra.api_project.dto.request.CartItemRequest;
import com.ra.api_project.dto.response.CartItemResponse;
import com.ra.api_project.dto.response.CartResponse;
import com.ra.api_project.entity.Cart;
import com.ra.api_project.entity.CartItem;
import com.ra.api_project.entity.Product;
import com.ra.api_project.entity.User;
import com.ra.api_project.repository.CartItemRepository;
import com.ra.api_project.repository.CartRepository;
import com.ra.api_project.repository.ProductRepository;
import com.ra.api_project.repository.UserRepository;
import com.ra.api_project.service.CartService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CartServiceImpl implements CartService {

    private final CartRepository cartRepository;
    private final CartItemRepository cartItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public CartResponse getCartByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> createNewCart(user));

        return convertToResponse(cart);
    }

    @Override
    @Transactional
    public CartResponse addItemToCart(Long userId, CartItemRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        // Kiểm tra số lượng tồn kho
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
        }

        Cart cart = cartRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> createNewCart(user));

        // Kiểm tra sản phẩm đã có trong giỏ hàng chưa
        CartItem existingItem = cartItemRepository
                .findByCartCartIdAndProductProductId(cart.getCartId(), product.getProductId())
                .orElse(null);

        if (existingItem != null) {
            // Cập nhật số lượng
            int newQuantity = existingItem.getQuantity() + request.getQuantity();
            if (product.getStockQuantity() < newQuantity) {
                throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
            }
            existingItem.setQuantity(newQuantity);
            cartItemRepository.save(existingItem);
        } else {
            // Thêm sản phẩm mới
            CartItem cartItem = CartItem.builder()
                    .cart(cart)
                    .product(product)
                    .quantity(request.getQuantity())
                    .price(product.getPrice())
                    .build();
            cartItemRepository.save(cartItem);
        }

        return getCartByUserId(userId);
    }

    @Override
    @Transactional
    public CartResponse updateCartItem(Long userId, Integer cartItemId, CartItemRequest request) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        Product product = cartItem.getProduct();
        if (product.getStockQuantity() < request.getQuantity()) {
            throw new RuntimeException("Insufficient stock for product: " + product.getProductName());
        }

        cartItem.setQuantity(request.getQuantity());
        cartItemRepository.save(cartItem);

        return getCartByUserId(userId);
    }

    @Override
    @Transactional
    public void removeItemFromCart(Long userId, Integer cartItemId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        CartItem cartItem = cartItemRepository.findById(cartItemId)
                .orElseThrow(() -> new RuntimeException("Cart item not found with id: " + cartItemId));

        if (!cartItem.getCart().getCartId().equals(cart.getCartId())) {
            throw new RuntimeException("Cart item does not belong to user");
        }

        cartItemRepository.delete(cartItem);
    }

    @Override
    @Transactional
    public void clearCart(Long userId) {
        Cart cart = cartRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Cart not found for user: " + userId));

        cartItemRepository.deleteByCartCartId(cart.getCartId());
    }

    private Cart createNewCart(User user) {
        Cart cart = Cart.builder()
                .user(user)
                .cartItems(new ArrayList<>())
                .build();
        return cartRepository.save(cart);
    }

    private CartResponse convertToResponse(Cart cart) {
        List<CartItemResponse> itemResponses = cart.getCartItems() != null
                ? cart.getCartItems().stream()
                .map(this::convertToCartItemResponse)
                .collect(Collectors.toList())
                : new ArrayList<>();

        BigDecimal totalAmount = itemResponses.stream()
                .map(CartItemResponse::getSubtotal)
                .reduce(BigDecimal.ZERO, BigDecimal::add);

        return CartResponse.builder()
                .cartId(cart.getCartId())
                .userId(cart.getUser().getId())
                .username(cart.getUser().getUsername())
                .createdAt(cart.getCreatedAt())
                .cartItems(itemResponses)
                .totalAmount(totalAmount)
                .build();
    }

    private CartItemResponse convertToCartItemResponse(CartItem item) {
        BigDecimal subtotal = item.getPrice().multiply(BigDecimal.valueOf(item.getQuantity()));

        return CartItemResponse.builder()
                .cartItemId(item.getCartItemId())
                .productId(item.getProduct().getProductId())
                .productName(item.getProduct().getProductName())
                .imageUrl(item.getProduct().getImageUrl())
                .quantity(item.getQuantity())
                .price(item.getPrice())
                .subtotal(subtotal)
                .build();
    }
}