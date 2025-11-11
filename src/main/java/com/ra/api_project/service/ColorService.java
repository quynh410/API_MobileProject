package com.ra.api_project.service;

import com.ra.api_project.dto.request.ColorRequest;
import com.ra.api_project.dto.response.ColorResponse;

import java.util.List;

public interface ColorService {

    List<ColorResponse> getAllColors();

    ColorResponse getColorById(Integer id);

    List<ColorResponse> getColorsByProductId(Integer productId);

    ColorResponse createColor(ColorRequest request);

    ColorResponse updateColor(Integer id, ColorRequest request);

    void deleteColor(Integer id);
}