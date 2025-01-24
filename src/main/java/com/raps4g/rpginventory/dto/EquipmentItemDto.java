package com.raps4g.rpginventory.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class EquipmentItemDto {
    private Long id;
    private PlayerDto player;
    private InventoryItemResponseDto inventoryItem;
    private SlotDto slot;
}
