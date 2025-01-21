package com.raps4g.rpginventory.domain.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AddItemDto {
    private Long id;
    private String name;
    private String description;
    private Long itemCategoryId;
    private Long itemRarityId;
    private Long validSlotId;
    private Integer value;
}
