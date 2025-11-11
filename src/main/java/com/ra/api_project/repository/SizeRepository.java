package com.ra.api_project.repository;

import com.ra.api_project.entity.Size;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SizeRepository extends JpaRepository<Size, Integer> {

    List<Size> findByProductProductId(Integer productId);

    boolean existsBySizeNameAndProductProductId(String sizeName, Integer productId);
}