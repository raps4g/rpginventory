package com.raps4g.rpginventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class InventoryItemResponseDto {
    private Long inventoryItemId;
    private Long inventoryId;
    private ItemDto item;
}
