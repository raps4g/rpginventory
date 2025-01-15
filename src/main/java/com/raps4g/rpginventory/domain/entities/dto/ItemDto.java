package com.raps4g.rpginventory.domain.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ItemDto {
    private Long id;
    private String name;
    private String description;
    private ItemCategoryDto ItemCategory;
    private ItemRarityDto itemRarity;
    private SlotDto validSlot;
    private Integer value;
}
