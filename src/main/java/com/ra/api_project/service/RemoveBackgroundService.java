package com.ra.api_project.service;

import org.springframework.web.multipart.MultipartFile;

public interface RemoveBackgroundService {
    byte[] removeBackground(MultipartFile file);
}