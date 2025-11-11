package com.ra.api_project.repository;

import com.ra.api_project.entity.WishListItem;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface WishListItemRepository extends JpaRepository<WishListItem, Integer> {

    List<WishListItem> findByWishListWishListId(Integer wishListId);

    Optional<WishListItem> findByWishListWishListIdAndProductProductId(Integer wishListId, Integer productId);

    boolean existsByWishListWishListIdAndProductProductId(Integer wishListId, Integer productId);

    void deleteByWishListWishListId(Integer wishListId);
}