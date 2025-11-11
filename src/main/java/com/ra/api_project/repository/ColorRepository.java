package com.ra.api_project.repository;

import com.ra.api_project.entity.Color;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ColorRepository extends JpaRepository<Color, Integer> {

    List<Color> findByProductProductId(Integer productId);

    boolean existsByColorNameAndProductProductId(String colorName, Integer productId);
}