package com.ra.api_project.service.Impl;

import com.ra.api_project.dto.request.WishListRequest;
import com.ra.api_project.dto.response.WishListItemResponse;
import com.ra.api_project.dto.response.WishListResponse;
import com.ra.api_project.entity.Product;
import com.ra.api_project.entity.User;
import com.ra.api_project.entity.WishList;
import com.ra.api_project.entity.WishListItem;
import com.ra.api_project.repository.ProductRepository;
import com.ra.api_project.repository.UserRepository;
import com.ra.api_project.repository.WishListItemRepository;
import com.ra.api_project.repository.WishListRepository;
import com.ra.api_project.service.WishListService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class WishListServiceImpl implements WishListService {

    private final WishListRepository wishListRepository;
    private final WishListItemRepository wishListItemRepository;
    private final ProductRepository productRepository;
    private final UserRepository userRepository;

    @Override
    public WishListResponse getWishListByUserId(Long userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        WishList wishList = wishListRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> createNewWishList(user));

        return convertToResponse(wishList);
    }

    @Override
    @Transactional
    public WishListResponse addItemToWishList(Long userId, WishListRequest request) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        WishList wishList = wishListRepository.findByUserIdWithItems(userId)
                .orElseGet(() -> createNewWishList(user));

        // Kiểm tra sản phẩm đã có trong wishlist chưa
        boolean exists = wishListItemRepository
                .existsByWishListWishListIdAndProductProductId(wishList.getWishListId(), product.getProductId());

        if (exists) {
            throw new RuntimeException("Product already exists in wishlist");
        }

        WishListItem wishListItem = WishListItem.builder()
                .wishList(wishList)
                .product(product)
                .build();
        wishListItemRepository.save(wishListItem);

        return getWishListByUserId(userId);
    }

    @Override
    @Transactional
    public void removeItemFromWishList(Long userId, Integer wishListItemId) {
        WishList wishList = wishListRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found for user: " + userId));

        WishListItem wishListItem = wishListItemRepository.findById(wishListItemId)
                .orElseThrow(() -> new RuntimeException("Wishlist item not found with id: " + wishListItemId));

        if (!wishListItem.getWishList().getWishListId().equals(wishList.getWishListId())) {
            throw new RuntimeException("Wishlist item does not belong to user");
        }

        wishListItemRepository.delete(wishListItem);
    }

    @Override
    @Transactional
    public void clearWishList(Long userId) {
        WishList wishList = wishListRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("Wishlist not found for user: " + userId));

        wishListItemRepository.deleteByWishListWishListId(wishList.getWishListId());
    }

    private WishList createNewWishList(User user) {
        WishList wishList = WishList.builder()
                .user(user)
                .wishListItems(new ArrayList<>())
                .build();
        return wishListRepository.save(wishList);
    }

    private WishListResponse convertToResponse(WishList wishList) {
        List<WishListItemResponse> itemResponses = wishList.getWishListItems() != null
                ? wishList.getWishListItems().stream()
                .map(this::convertToWishListItemResponse)
                .collect(Collectors.toList())
                : new ArrayList<>();

        return WishListResponse.builder()
                .wishListId(wishList.getWishListId())
                .userId(wishList.getUser().getUserId())
                .username(wishList.getUser().getUsername())
                .createdAt(wishList.getCreatedAt())
                .wishListItems(itemResponses)
                .build();
    }

    private WishListItemResponse convertToWishListItemResponse(WishListItem item) {
        Product product = item.getProduct();
        return WishListItemResponse.builder()
                .wishListItemId(item.getWishListItemId())
                .productId(product.getProductId())
                .productName(product.getProductName())
                .description(product.getDescription())
                .price(product.getPrice())
                .imageUrl(product.getImageUrl())
                .stockQuantity(product.getStockQuantity())
                .addedAt(item.getAddedAt())
                .build();
    }
}