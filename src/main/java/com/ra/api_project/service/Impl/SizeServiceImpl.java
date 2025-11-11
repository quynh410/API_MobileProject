package com.ra.api_project.service.Impl;

import com.ra.api_project.dto.request.SizeRequest;
import com.ra.api_project.dto.response.SizeResponse;
import com.ra.api_project.entity.Product;
import com.ra.api_project.entity.Size;
import com.ra.api_project.repository.ProductRepository;
import com.ra.api_project.repository.SizeRepository;
import com.ra.api_project.service.SizeService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SizeServiceImpl implements SizeService {

    private final SizeRepository sizeRepository;
    private final ProductRepository productRepository;

    @Override
    public List<SizeResponse> getAllSizes() {
        return sizeRepository.findAll().stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    public SizeResponse getSizeById(Integer id) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found with id: " + id));
        return convertToResponse(size);
    }

    @Override
    public List<SizeResponse> getSizesByProductId(Integer productId) {
        if (!productRepository.existsById(productId)) {
            throw new RuntimeException("Product not found with id: " + productId);
        }
        return sizeRepository.findByProductProductId(productId).stream()
                .map(this::convertToResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public SizeResponse createSize(SizeRequest request) {
        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        if (sizeRepository.existsBySizeNameAndProductProductId(request.getSizeName(), request.getProductId())) {
            throw new RuntimeException("Size already exists for this product: " + request.getSizeName());
        }

        Size size = Size.builder()
                .sizeName(request.getSizeName())
                .product(product)
                .build();

        Size savedSize = sizeRepository.save(size);
        return convertToResponse(savedSize);
    }

    @Override
    @Transactional
    public SizeResponse updateSize(Integer id, SizeRequest request) {
        Size size = sizeRepository.findById(id)
                .orElseThrow(() -> new RuntimeException("Size not found with id: " + id));

        Product product = productRepository.findById(request.getProductId())
                .orElseThrow(() -> new RuntimeException("Product not found with id: " + request.getProductId()));

        // Kiểm tra tên size trùng (ngoại trừ chính nó)
        if (!size.getSizeName().equals(request.getSizeName()) &&
                sizeRepository.existsBySizeNameAndProductProductId(request.getSizeName(), request.getProductId())) {
            throw new RuntimeException("Size already exists for this product: " + request.getSizeName());
        }

        size.setSizeName(request.getSizeName());
        size.setProduct(product);

        Size updatedSize = sizeRepository.save(size);
        return convertToResponse(updatedSize);
    }

    @Override
    @Transactional
    public void deleteSize(Integer id) {
        if (!sizeRepository.existsById(id)) {
            throw new RuntimeException("Size not found with id: " + id);
        }
        sizeRepository.deleteById(id);
    }

    private SizeResponse convertToResponse(Size size) {
        return SizeResponse.builder()
                .sizeId(size.getSizeId())
                .sizeName(size.getSizeName())
                .productId(size.getProduct() != null ? size.getProduct().getProductId() : null)
                .productName(size.getProduct() != null ? size.getProduct().getProductName() : null)
                .build();
    }
}