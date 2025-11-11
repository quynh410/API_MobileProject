package com.ra.api_project.dto.response;

import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListResponse {

    private Integer wishListId;
    private Long userId;
    private String username;
    private LocalDateTime createdAt;
    private List<WishListItemResponse> wishListItems;
}