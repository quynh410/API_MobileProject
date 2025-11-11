package com.ra.api_project.service;

import com.ra.api_project.dto.request.SizeRequest;
import com.ra.api_project.dto.response.SizeResponse;

import java.util.List;

public interface SizeService {

    List<SizeResponse> getAllSizes();

    SizeResponse getSizeById(Integer id);

    List<SizeResponse> getSizesByProductId(Integer productId);

    SizeResponse createSize(SizeRequest request);

    SizeResponse updateSize(Integer id, SizeRequest request);

    void deleteSize(Integer id);
}