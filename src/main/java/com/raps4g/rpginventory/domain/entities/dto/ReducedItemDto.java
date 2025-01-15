package com.raps4g.rpginventory.domain.entities.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ReducedItemDto {
    private Long id;
    private String name;
    private String description;
    private Long categoryId;
    private Long rarityId;
    private Long validSlotId;
    private Integer value;
}
