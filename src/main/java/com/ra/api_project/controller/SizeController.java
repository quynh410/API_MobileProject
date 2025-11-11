package com.ra.api_project.controller;

import com.ra.api_project.dto.request.SizeRequest;
import com.ra.api_project.dto.response.BaseResponse;
import com.ra.api_project.dto.response.SizeResponse;
import com.ra.api_project.service.SizeService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/sizes")
@RequiredArgsConstructor
public class SizeController {

    private final SizeService sizeService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<SizeResponse>>> getAllSizes() {
        List<SizeResponse> sizes = sizeService.getAllSizes();
        return ResponseEntity.ok(BaseResponse.<List<SizeResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all sizes successfully")
                .data(sizes)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<SizeResponse>> getSizeById(@PathVariable Integer id) {
        SizeResponse size = sizeService.getSizeById(id);
        return ResponseEntity.ok(BaseResponse.<SizeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get size successfully")
                .data(size)
                .build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<BaseResponse<List<SizeResponse>>> getSizesByProductId(@PathVariable Integer productId) {
        List<SizeResponse> sizes = sizeService.getSizesByProductId(productId);
        return ResponseEntity.ok(BaseResponse.<List<SizeResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get sizes by product successfully")
                .data(sizes)
                .build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<SizeResponse>> createSize(@Valid @RequestBody SizeRequest request) {
        SizeResponse size = sizeService.createSize(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<SizeResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Size created successfully")
                        .data(size)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<SizeResponse>> updateSize(
            @PathVariable Integer id,
            @Valid @RequestBody SizeRequest request) {

        SizeResponse size = sizeService.updateSize(id, request);
        return ResponseEntity.ok(BaseResponse.<SizeResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Size updated successfully")
                .data(size)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteSize(@PathVariable Integer id) {
        sizeService.deleteSize(id);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Size deleted successfully")
                .build());
    }
}