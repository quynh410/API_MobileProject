package com.ra.api_project.entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "sizes")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Size {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "size_id")
    private Integer sizeId;

    @Column(name = "size_name", nullable = false, length = 50)
    private String sizeName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}