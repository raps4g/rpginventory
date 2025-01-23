package com.raps4g.rpginventory.services;

import java.util.List;

import com.raps4g.rpginventory.model.EquipmentItem;
import com.raps4g.rpginventory.dto.EquipmentItemDto;

public interface EquipmentService {

    // Mappers
    
    EquipmentItemDto mapToEquipmentItemDto(EquipmentItem equipmentItem);
   
    // Add

    EquipmentItem equipItem(Long playerId, Long inventoryItemId) throws IllegalAccessException;

    // Get

    List<EquipmentItem> getEquippedItems(Long playerId);
    
    // Delete

    void unequipItem(Long playerId, Long inventoryItemId) throws IllegalAccessException;
    
}
