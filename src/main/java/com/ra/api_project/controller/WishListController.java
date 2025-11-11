package com.ra.api_project.controller;

import com.ra.api_project.dto.request.WishListRequest;
import com.ra.api_project.dto.response.BaseResponse;
import com.ra.api_project.dto.response.WishListResponse;
import com.ra.api_project.service.WishListService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/wishlists")
@RequiredArgsConstructor
public class WishListController {

    private final WishListService wishListService;

    @GetMapping("/user/{userId}")
    public ResponseEntity<BaseResponse<WishListResponse>> getWishListByUserId(@PathVariable Long userId) {
        WishListResponse wishList = wishListService.getWishListByUserId(userId);
        return ResponseEntity.ok(BaseResponse.<WishListResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get wishlist successfully")
                .data(wishList)
                .build());
    }

    @PostMapping("/user/{userId}/items")
    public ResponseEntity<BaseResponse<WishListResponse>> addItemToWishList(
            @PathVariable Long userId,
            @Valid @RequestBody WishListRequest request) {

        WishListResponse wishList = wishListService.addItemToWishList(userId, request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<WishListResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Item added to wishlist successfully")
                        .data(wishList)
                        .build());
    }

    @DeleteMapping("/user/{userId}/items/{wishListItemId}")
    public ResponseEntity<BaseResponse<Void>> removeItemFromWishList(
            @PathVariable Long userId,
            @PathVariable Integer wishListItemId) {

        wishListService.removeItemFromWishList(userId, wishListItemId);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Item removed from wishlist successfully")
                .build());
    }

    @DeleteMapping("/user/{userId}/clear")
    public ResponseEntity<BaseResponse<Void>> clearWishList(@PathVariable Long userId) {
        wishListService.clearWishList(userId);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Wishlist cleared successfully")
                .build());
    }
}