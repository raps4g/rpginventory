package com.raps4g.rpginventory.services;

import java.util.List;

import com.raps4g.rpginventory.model.Inventory;
import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.dto.InventoryDto;
import com.raps4g.rpginventory.dto.InventoryItemResponseDto;

public interface InventoryService {

    // Mappers
   
    InventoryItemResponseDto mapToInventoryItemResponseDto(InventoryItem inventoryItem);
    
    InventoryDto mapToInventoryDto(Inventory inventory);


    // Add

    InventoryItem addItemToInventory(Long playerId, Long itemId);
    
    InventoryItem buyItem(Long playerId, Long itemId);
    
    Inventory createPlayerInventory(Long playerId);


    // Get

    List<InventoryItem> getAllItemsInPlayerInventory(Long playerId);
   
    Inventory getPlayerInventory(Long playerId);

    
    // Delete

    void removeItemFromInventory(Long playerId, Long inventoryItemId) throws IllegalAccessException;
    
    Player sellItem(Long playerId, Long inventoryItemId) throws IllegalAccessException;
    
    void clearPlayerInventoryItems(Long playerId);
    
    void deletePlayerInventory(Long itemId);
}
