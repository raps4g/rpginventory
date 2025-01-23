package com.raps4g.rpginventory.services.impl;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.model.EquipmentItem;
import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.dto.EquipmentItemDto;
import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.exceptions.SlotAlreadyInUseException;
import com.raps4g.rpginventory.repositories.EquipmentItemRepository;
import com.raps4g.rpginventory.repositories.InventoryItemRepository;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.services.EquipmentService;
import com.raps4g.rpginventory.services.InventoryService;
import com.raps4g.rpginventory.services.PlayerService;
import com.raps4g.rpginventory.services.SlotService;


@Service
public class EquipmentServiceImpl implements EquipmentService{
  
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private InventoryService inventoryService;
   
    @Autowired
    private SlotService slotService;

    @Autowired
    private PlayerService playerService;

    // Mappers

    @Override
    public EquipmentItemDto mapToEquipmentItemDto(EquipmentItem equipmentItem) {

        EquipmentItemDto equipmentItemDto = EquipmentItemDto.builder()
            .id(equipmentItem.getId())
            .slot(slotService.convertToSlotDto(equipmentItem.getSlot()))
            .player(playerService.mapToPlayerDto(equipmentItem.getPlayer()))
            .inventoryItem(inventoryService.mapToInventoryItemResponseDto(equipmentItem.getInventoryItem()))
            .build();

        return equipmentItemDto;
    }

    // Add
    
    @Override
    public EquipmentItem equipItem(Long playerId, Long inventoryItemId) throws IllegalAccessException {
        
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }

        InventoryItem inventoryItem = inventoryItemRepository.findById(inventoryItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item with id " + inventoryItemId + " not found."));

        if (playerId != inventoryItem.getInventory().getPlayer().getId()) {
            throw new IllegalAccessException("Inventory item with id " + inventoryItemId + " cannot be equipped by player with id " + playerId + ".");
        }

        if (equipmentItemRepository.findBySlotId(inventoryItem.getItem().getValidSlot().getId()).isPresent()) {
            throw new SlotAlreadyInUseException("Inventory item with id " + inventoryItemId + " cannot be equipped because the corresponding slot is already in use.");
        }

        EquipmentItem equipmentItem = EquipmentItem.builder()
            .player(inventoryItem.getInventory().getPlayer())
            .slot(inventoryItem.getItem().getValidSlot())
            .inventoryItem(inventoryItem)
            .build();

        return equipmentItemRepository.save(equipmentItem);
    }


    // Get

    @Override
    public List<EquipmentItem> getEquippedItems(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }

        List<EquipmentItem> equippedItems = equipmentItemRepository.findAllByPlayerId(playerId);
        return equippedItems;
    }

    // Delete
    
    @Override
    public void unequipItem(Long playerId, Long inventoryItemId) throws IllegalAccessException {
        
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }

        EquipmentItem equipmentItem = equipmentItemRepository.findByInventoryItemId(inventoryItemId).
            orElseThrow(() -> new ResourceNotFoundException("Inventory item with id " + inventoryItemId + " not found in the players equipment."));

        if (playerId != equipmentItem.getPlayer().getId()) {
            throw new IllegalAccessException("Inventory item with id " + inventoryItemId + " cannot be unequipped by player with id " + playerId + ".");
        }
        
        equipmentItemRepository.delete(equipmentItem);

        
    }

}
