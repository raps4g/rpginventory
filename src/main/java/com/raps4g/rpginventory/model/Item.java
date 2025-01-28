package com.raps4g.rpginventory.model;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
@Entity
public class Item {
    
    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    private Long id;
    
    @Column(unique = true, nullable = false)
    private String name;
    
    private String description;

    @ManyToOne
    @JoinColumn(name = "category_id", nullable = false)
    private ItemCategory itemCategory;
    
    @ManyToOne
    @JoinColumn(name = "rarity_id", nullable = false)
    private ItemRarity itemRarity;
   
    @ManyToOne
    private Slot validSlot;

    @Column(name = "item_value", nullable = false)
    private Integer value;

}
