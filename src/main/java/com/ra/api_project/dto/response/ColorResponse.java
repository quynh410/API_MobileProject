package com.ra.api_project.dto.response;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class ColorResponse {

    private Integer colorId;
    private String colorName;
    private Integer productId;
    private String productName;
}