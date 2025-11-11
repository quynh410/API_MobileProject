package com.ra.api_project.service;

import com.ra.api_project.dto.request.WishListRequest;
import com.ra.api_project.dto.response.WishListResponse;

public interface WishListService {

    WishListResponse getWishListByUserId(Long userId);

    WishListResponse addItemToWishList(Long userId, WishListRequest request);

    void removeItemFromWishList(Long userId, Integer wishListItemId);

    void clearWishList(Long userId);
}