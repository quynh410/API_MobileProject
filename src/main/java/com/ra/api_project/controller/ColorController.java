package com.ra.api_project.controller;

import com.ra.api_project.dto.request.ColorRequest;
import com.ra.api_project.dto.response.BaseResponse;
import com.ra.api_project.dto.response.ColorResponse;
import com.ra.api_project.service.ColorService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/v1/colors")
@RequiredArgsConstructor
public class ColorController {

    private final ColorService colorService;

    @GetMapping
    public ResponseEntity<BaseResponse<List<ColorResponse>>> getAllColors() {
        List<ColorResponse> colors = colorService.getAllColors();
        return ResponseEntity.ok(BaseResponse.<List<ColorResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get all colors successfully")
                .data(colors)
                .build());
    }

    @GetMapping("/{id}")
    public ResponseEntity<BaseResponse<ColorResponse>> getColorById(@PathVariable Integer id) {
        ColorResponse color = colorService.getColorById(id);
        return ResponseEntity.ok(BaseResponse.<ColorResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get color successfully")
                .data(color)
                .build());
    }

    @GetMapping("/product/{productId}")
    public ResponseEntity<BaseResponse<List<ColorResponse>>> getColorsByProductId(@PathVariable Integer productId) {
        List<ColorResponse> colors = colorService.getColorsByProductId(productId);
        return ResponseEntity.ok(BaseResponse.<List<ColorResponse>>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Get colors by product successfully")
                .data(colors)
                .build());
    }

    @PostMapping
    public ResponseEntity<BaseResponse<ColorResponse>> createColor(@Valid @RequestBody ColorRequest request) {
        ColorResponse color = colorService.createColor(request);
        return ResponseEntity.status(HttpStatus.CREATED)
                .body(BaseResponse.<ColorResponse>builder()
                        .statusCode(HttpStatus.CREATED.value())
                        .message("Color created successfully")
                        .data(color)
                        .build());
    }

    @PutMapping("/{id}")
    public ResponseEntity<BaseResponse<ColorResponse>> updateColor(
            @PathVariable Integer id,
            @Valid @RequestBody ColorRequest request) {

        ColorResponse color = colorService.updateColor(id, request);
        return ResponseEntity.ok(BaseResponse.<ColorResponse>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Color updated successfully")
                .data(color)
                .build());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<BaseResponse<Void>> deleteColor(@PathVariable Integer id) {
        colorService.deleteColor(id);
        return ResponseEntity.ok(BaseResponse.<Void>builder()
                .statusCode(HttpStatus.OK.value())
                .message("Color deleted successfully")
                .build());
    }
}