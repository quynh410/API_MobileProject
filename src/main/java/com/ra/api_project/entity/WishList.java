package com.ra.api_project.entity;

import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "wish_list")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WishList {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "wish_list_id")
    private Integer wishListId;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @OneToMany(mappedBy = "wishList", cascade = CascadeType.ALL)
    private List<WishListItem> wishListItems;

    @PrePersist
    protected void onCreate() {
        createdAt = LocalDateTime.now();
    }
}
