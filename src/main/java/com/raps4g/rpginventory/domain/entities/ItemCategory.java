package com.raps4g.rpginventory.domain.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class ItemCategory {
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "item_category_seq")
    private Long id;
    @Column(unique = true, nullable = false)
    private String name;
}
