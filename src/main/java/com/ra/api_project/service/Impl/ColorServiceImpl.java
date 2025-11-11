package com.ra.api_project.service.Impl;

import com.ra.api_project.dto.request.ColorRequest;
import com.ra.api_project.dto.response.ColorResponse;
import com.ra.api_project.entity.Color;
import com.ra.api_project.entity.Product;
import com.ra.api_project.repository.ColorRepository;
import com.ra.api_project.repository.ProductRepository;
import com.ra.api_project.service.ColorService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ColorServiceImpl implements ColorService {

    private final ColorRepository colorRepository;
    private final ProductRepository productRepository;

    @Override
    public List<ColorResponse> getAllColors() {
        return colorRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ColorResponse getColorById(Integer id) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found with id: " + id));
        return convertToResponse(color);
    }

    @Override
    public List<ColorResponse> getColorsByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        return colorRepository.findByProductProductId(productId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public ColorResponse createColor(ColorRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        if (colorRepository.existsByColorNameAndProductProductId(request.getColorName(), request.getProductId())) {
            throw new RuntimeException("Color already exists for this product: " + request.getColorName());
        }

        Color color = Color.builder()
                .colorName(request.getColorName())
                .product(product)
                .build();

        Color savedColor = colorRepository.save(color);
        return convertToResponse(savedColor);
    }

    @Override
    @Transactional
    public ColorResponse updateColor(Integer id, ColorRequest request) {
        Color color = colorRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Color not found with id: " + id));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        // Kiểm tra tên màu trùng (ngoại trừ chính nó)
        if (!color.getColorName().equals(request.getColorName()) &&
                colorRepository.existsByColorNameAndProductProductId(request.getColorName(), request.getProductId())) {
            throw new RuntimeException("Color already exists for this product: " + request.getColorName());
        }

        color.setColorName(request.getColorName());
        color.setProduct(product);

        Color updatedColor = colorRepository.save(color);
        return convertToResponse(updatedColor);
    }

    @Override
    @Transactional
    public void deleteColor(Integer id) {
        if (!colorRepository.existsById(id)) {
            throw new RuntimeException("Color not found with id: " + id);
        }
        colorRepository.deleteById(id);
    }

    private ColorResponse convertToResponse(Color color) {
        return ColorResponse.builder()
                .colorId(color.getColorId())
                .colorName(color.getColorName())
                .productId(color.getProduct() != null ? color.getProduct().getProductId() : null)
                .productName(color.getProduct() != null ? color.getProduct().getProductName() : null)
                .build();
    }
}