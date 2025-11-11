package com.ra.api_project.entity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "colors")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Color {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "color_id")
    private Integer colorId;

    @Column(name = "color_name", nullable = false, length = 50)
    private String colorName;

    @ManyToOne
    @JoinColumn(name = "product_id")
    private Product product;
}