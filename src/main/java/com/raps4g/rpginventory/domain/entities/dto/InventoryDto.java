package com.raps4g.rpginventory.domain.entities.dto;

import java.util.ArrayList;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class InventoryDto {
    
    private Long id;
    private Long playerId;
    private ArrayList<Long> inventoryItemIds;

}
