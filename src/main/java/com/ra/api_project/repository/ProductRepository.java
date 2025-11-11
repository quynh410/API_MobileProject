package com.ra.api_project.repository;

import com.ra.api_project.entity.Product;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProductRepository extends JpaRepository<Product, Integer> {

    Page<Product> findByCategoryCategoryId(Integer categoryId, Pageable pageable);

    @Query("SELECT p FROM Product p WHERE LOWER(p.productName) LIKE LOWER(CONCAT('%', :keyword, '%')) " +
            "OR LOWER(p.description) LIKE LOWER(CONCAT('%', :keyword, '%'))")
    Page<Product> searchByKeyword(@Param("keyword") String keyword, Pageable pageable);

    Optional<Product> findByProductName(String productName);

    @Query("SELECT p FROM Product p LEFT JOIN FETCH p.category LEFT JOIN FETCH p.sizes LEFT JOIN FETCH p.colors WHERE p.productId = :id")
    Optional<Product> findByIdWithDetails(@Param("id") Integer id);

    boolean existsByProductName(String productName);
}