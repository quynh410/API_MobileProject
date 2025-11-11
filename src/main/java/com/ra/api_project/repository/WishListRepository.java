package com.ra.api_project.repository;

import com.ra.api_project.entity.WishList;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface WishListRepository extends JpaRepository<WishList, Integer> {

    Optional<WishList> findByUserId(Long userId);

    @Query("SELECT w FROM WishList w LEFT JOIN FETCH w.wishListItems WHERE w.user.id = :userId")
    Optional<WishList> findByUserIdWithItems(@Param("userId") Long userId);

    boolean existsByUserId(Long userId);
}