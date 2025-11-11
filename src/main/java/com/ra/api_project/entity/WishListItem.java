package com.ra.api_project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "wish_list_items")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishListItem {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_item_id")
    private Integer wishListItemId;

    @ManyToOne
    @JoinColumn(name = "wish_list_id")
    private WishList wishList;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;

    @Column(name = "added_at", updatable = false)
    private LocalDateTime addedAt;

    @PrePersist
    protected void onCreate() {
        addedAt = LocalDateTime.now();
    }
}

