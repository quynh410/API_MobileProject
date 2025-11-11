package com.ra.api_project.service.Impl;

import com.ra.api_project.service.RemoveBackgroundService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.*;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.multipart.MultipartFile;

@Service
@Slf4j
public class RemoveBackgroundServiceImpl implements RemoveBackgroundService {

    @Value("${removebg.api.key}")
    private String apiKey;

    @Override
    public byte[] removeBackground(MultipartFile file) {
        try {
            RestTemplate restTemplate = new RestTemplate();
            String url = "https://api.remove.bg/v1.0/removebg";

            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.MULTIPART_FORM_DATA);
            headers.set("X-Api-Key", apiKey);

            MultiValueMap<String, Object> body = new LinkedMultiValueMap<>();
            body.add("image_file", new ByteArrayResource(file.getBytes()) {
                @Override
                public String getFilename() {
                    return file.getOriginalFilename();
                }
            });
            body.add("size", "auto");

            HttpEntity<MultiValueMap<String, Object>> requestEntity =
                    new HttpEntity<>(body, headers);

            ResponseEntity<byte[]> response = restTemplate.exchange(
                    url,
                    HttpMethod.POST,
                    requestEntity,
                    byte[].class
            );

            log.info("Background removed successfully for file: {}", file.getOriginalFilename());
            return response.getBody();

        } catch (Exception e) {
            log.error("Error removing background: {}", e.getMessage());
            throw new RuntimeException("Lỗi khi tách nền ảnh: " + e.getMessage());
        }
    }
}