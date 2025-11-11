package com.ra.api_project.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class SizeResponse {

    private Integer sizeId;
    private String sizeName;
    private Integer productId;
    private String productName;
}