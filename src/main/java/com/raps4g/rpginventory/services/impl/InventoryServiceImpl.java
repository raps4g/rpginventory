package com.raps4g.rpginventory.services.impl;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.domain.entities.Inventory;
import com.raps4g.rpginventory.domain.entities.InventoryItem;
import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.Player;
import com.raps4g.rpginventory.domain.entities.dto.InventoryDto;
import com.raps4g.rpginventory.domain.entities.dto.InventoryItemResponseDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.exceptions.InsufficientGoldException;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.repositories.EquipmentItemRepository;
import com.raps4g.rpginventory.repositories.InventoryItemRepository;
import com.raps4g.rpginventory.repositories.InventoryRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.services.InventoryService;

import jakarta.transaction.Transactional;


@Service
public class InventoryServiceImpl implements InventoryService{
    
    @Autowired
    private ModelMapper modelMapper;

    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    
    @Autowired
    private PlayerRepository playerRepository; 
   
    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    // Mappers

    @Override
    public InventoryItemResponseDto mapToInventoryItemResponseDto(InventoryItem inventoryItem) {
        InventoryItemResponseDto inventoryItemResponseDto = InventoryItemResponseDto.builder()
            .inventoryItemId(inventoryItem.getId())
            .inventoryId(inventoryItem.getInventory().getId())
            .item(modelMapper.map(inventoryItem.getItem(), ItemDto.class))
            .build();
        return inventoryItemResponseDto;
    }

    @Override
    public InventoryDto mapToInventoryDto(Inventory inventory) {
        InventoryDto inventoryDto = InventoryDto.builder()
        .id(inventory.getId())
        .playerId(inventory.getPlayer().getId())
        .inventoryItemIds( inventory.getInventoryItems() == null ? new ArrayList<>() :
            inventory.getInventoryItems()
                    .stream()
                    .map(InventoryItem::getId)
                    .collect(Collectors.toCollection(ArrayList::new))
        )
        .build();

        return inventoryDto;
    }


    // Add

    @Override
    public InventoryItem addItemToInventory(Long playerId, Long itemId) {
       
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }

        Inventory playerInventory = inventoryRepository.findByPlayerId(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory for playerId " + playerId + " not found."));

        Item item = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item with id " + itemId + " not found."));
        
        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setInventory(playerInventory);
        inventoryItem.setItem(item);

        playerInventory.getInventoryItems().add(inventoryItem);
        
        return inventoryItemRepository.save(inventoryItem);
    }

    @Override
    public InventoryItem buyItem(Long playerId, Long itemId) {

        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Player with id " + playerId + " not found."));

        Item item = itemRepository.findById(itemId)
        .orElseThrow(() -> new ResourceNotFoundException("Item with id " + itemId + " not found."));

        if (player.getGold() < item.getValue()) {
            throw new InsufficientGoldException("Player with id " + playerId + "does not have sufficient gold for item with id " + item.getId() + ".");
        }
        
        Inventory playerInventory = inventoryRepository.findByPlayerId(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory for playerId " + playerId + " not found."));

        InventoryItem inventoryItem = new InventoryItem();
        inventoryItem.setInventory(playerInventory);
        inventoryItem.setItem(item);

        playerInventory.getInventoryItems().add(inventoryItem);
        player.setGold(player.getGold() - item.getValue());
        
        playerRepository.save(player);
        return inventoryItemRepository.save(inventoryItem);
    }

    @Override
    public Inventory createPlayerInventory(Long playerId) {
        Inventory inventory = new Inventory();

        if (inventoryRepository.existsByPlayerId(playerId)) {
            throw new ResourceAlreadyExistsException("An inventory already exists for player id " + playerId + ".");
        }

        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Player with id " + playerId + " not found."));
        inventory.setPlayer(player);

        return inventoryRepository.save(inventory);
    }


    // Get

    @Override
    public List<InventoryItem> getAllItemsInPlayerInventory(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }

        Inventory foundInventory = inventoryRepository.findByPlayerId(playerId)
        .orElseThrow(() -> new ResourceNotFoundException("Inventory for playerId " + playerId + " not found."));

        return foundInventory.getInventoryItems();

    }

    @Override
    public Inventory getPlayerInventory(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }

        Inventory foundInventory = inventoryRepository.findByPlayerId(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory for playerId " + playerId + " not found."));

        return foundInventory;
    }


    // Delete

    @Override
    public void removeItemFromInventory(Long playerId, Long inventoryItemId) throws IllegalAccessException {

        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }
        
        InventoryItem itemToRemove = inventoryItemRepository.findById(inventoryItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item with id " + inventoryItemId + " not found."));

        if (playerId != itemToRemove.getInventory().getPlayer().getId()) {
            throw new IllegalAccessException("Inventory item with id " + inventoryItemId + " cannot be removed by player with id " + playerId + ".");
        }

        if(equipmentItemRepository.existsByInventoryItemId(inventoryItemId)) {
            throw new DataIntegrityViolationException("Inventory item with id " + inventoryItemId + " cannot be removed because it is equiped by the player.");
        }

        inventoryItemRepository.delete(itemToRemove);
    }

    @Override
    @Transactional
    public void clearPlayerInventoryItems(Long playerId) {

        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }
        
        Inventory foundInventory = inventoryRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory for playerId " + playerId + " not found."));
            inventoryItemRepository.deleteByInventoryId(foundInventory.getId());
        }
    
    @Override
    public Player sellItem(Long playerId, Long inventoryItemId) throws IllegalAccessException {
        
        Player player = playerRepository.findById(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Player with id " + playerId + " not found."));
        
        InventoryItem itemToRemove = inventoryItemRepository.findById(inventoryItemId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory item with id " + inventoryItemId + " not found."));

        if (playerId != itemToRemove.getInventory().getPlayer().getId()) {
            throw new IllegalAccessException("Inventory item with id " + inventoryItemId + " cannot be sold by player with id " + playerId + ".");
        }

        if(equipmentItemRepository.existsByInventoryItemId(inventoryItemId)) {
            throw new DataIntegrityViolationException("Inventory item with id " + inventoryItemId + " cannot be sold because it is equiped by the player.");
        }
        
        player.setGold(player.getGold() + itemToRemove.getItem().getValue());
        
        playerRepository.save(player);
        inventoryItemRepository.delete(itemToRemove);
        
        return player;
    }
    
    @Override
    public void deletePlayerInventory(Long playerId) {
        if (!playerRepository.existsById(playerId)) {
            throw new ResourceNotFoundException("Player with id " + playerId + " not found.");
        }
        
        Inventory playerInventory = inventoryRepository.findByPlayerId(playerId)
            .orElseThrow(() -> new ResourceNotFoundException("Inventory for playerId " + playerId + " not found."));
        
        if (inventoryItemRepository.existsByInventoryId(playerInventory.getId())) {
            throw new DataIntegrityViolationException("Inventory for player with id " + playerId + " cannot be deleted because it has one or more items in it.");
        }

        inventoryRepository.delete(playerInventory); 
    }
}
