package com.raps4g.rpginventory.domain.entities.dto;

import com.raps4g.rpginventory.validation.InventoryItemValidationGroup;
import com.raps4g.rpginventory.validation.ItemValidationGroup;

import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class InventoryItemRequestDto {
    @NotNull(groups = InventoryItemValidationGroup.class, message = "'inventoryItemId' field is required.")
    private Long inventoryItemId;
    private Long inventoryId;
    @NotNull(groups = ItemValidationGroup.class, message = "'itemId' field is required.")
    private Long itemId;
}
