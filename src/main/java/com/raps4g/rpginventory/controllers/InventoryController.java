package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.model.Inventory;
import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.dto.InventoryDto;
import com.raps4g.rpginventory.dto.InventoryItemRequestDto;
import com.raps4g.rpginventory.dto.InventoryItemResponseDto;
import com.raps4g.rpginventory.dto.PlayerDto;
import com.raps4g.rpginventory.services.InventoryService;
import com.raps4g.rpginventory.services.PlayerService;
import com.raps4g.rpginventory.validation.InventoryItemValidationGroup;
import com.raps4g.rpginventory.validation.ItemValidationGroup;

@RestController
public class InventoryController {

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private PlayerService playerService;

    // POST
    @PostMapping(path = "/players/{playerId}/inventory")
    public ResponseEntity<InventoryDto> createPlayerInventory(@PathVariable Long playerId) {

        Inventory createdInventory = inventoryService.createPlayerInventory(playerId);
        InventoryDto createdInventoryDto = inventoryService.mapToInventoryDto(createdInventory);

        return new ResponseEntity<>(createdInventoryDto, HttpStatus.CREATED);
    }
   
    @PostMapping(path = "/admin/players/{playerId}/inventory/items")
    public ResponseEntity<InventoryItemResponseDto> addItemToInventory(
        @PathVariable Long playerId, 
        @Validated(ItemValidationGroup.class)
            @RequestBody InventoryItemRequestDto inventoryItemDto
    ) {
        Long itemId = inventoryItemDto.getItemId();
        InventoryItem addedInventoryItem = inventoryService.addItemToInventory(playerId, itemId);
        InventoryItemResponseDto addedInventoryItemDto = inventoryService.mapToInventoryItemResponseDto(addedInventoryItem);

        return new ResponseEntity<>(addedInventoryItemDto, HttpStatus.CREATED);
    }

    @PostMapping(path = "/players/{playerId}/inventory/items/buy")
    public ResponseEntity<InventoryItemResponseDto> buyItem(
        @PathVariable Long playerId, 
        @Validated(ItemValidationGroup.class)
            @RequestBody InventoryItemRequestDto inventoryItemDto
    ) {

        Long itemId = inventoryItemDto.getItemId();
        InventoryItem boughtInventoryItem = inventoryService.buyItem(playerId, itemId);
        InventoryItemResponseDto boughtInventoryItemDto = inventoryService.mapToInventoryItemResponseDto(boughtInventoryItem);

        return new ResponseEntity<>(boughtInventoryItemDto, HttpStatus.CREATED);
    }
   
    @PostMapping(path = "/players/{playerId}/inventory/items/sell")
    public ResponseEntity<PlayerDto> sellItem(
        @PathVariable Long playerId, 
        @Validated(InventoryItemValidationGroup.class)
            @RequestBody InventoryItemRequestDto inventoryItemDto
    ) throws IllegalAccessException {
        
        Long inventoryItemId = inventoryItemDto.getInventoryItemId();
        Player player = inventoryService.sellItem(playerId, inventoryItemId);
        PlayerDto playerDto = playerService.mapToPlayerDto(player);
        return new ResponseEntity<>(playerDto, HttpStatus.OK);
    }
    
    // GET
    
    @GetMapping(path = "/players/{playerId}/inventory")
    public ResponseEntity<InventoryDto> getPlayerInventory(@PathVariable Long playerId) {

        Inventory createdInventory = inventoryService.getPlayerInventory(playerId);
        InventoryDto createdInventoryDto = inventoryService.mapToInventoryDto(createdInventory);

        return new ResponseEntity<>(createdInventoryDto, HttpStatus.OK);
    }

    @GetMapping(path = "/players/{playerId}/inventory/items")
    public List<InventoryItemResponseDto> getAllItemsInPlayerInventory(
        @PathVariable Long playerId) {

        List<InventoryItem> inventoryItems = inventoryService.getAllItemsInPlayerInventory(playerId);
        List<InventoryItemResponseDto> inventoryItemsDto = inventoryItems.stream()
            .map(inventoryService::mapToInventoryItemResponseDto)
            .collect(Collectors.toList());
        
        return inventoryItemsDto;
    }

    // DELETE
    
    @DeleteMapping(path = "/admin/players/{playerId}/inventory/items")
    public ResponseEntity removeItemFromInventory(
        @PathVariable Long playerId, 
        @Validated(InventoryItemValidationGroup.class)
            @RequestBody InventoryItemRequestDto inventoryItemDto
    ) throws IllegalAccessException {

        Long inventoryItemId = inventoryItemDto.getInventoryItemId();
        inventoryService.removeItemFromInventory(playerId, inventoryItemId);

        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/admin/players/{playerId}/inventory")
    public ResponseEntity deletePlayerInventory(
        @PathVariable Long playerId 
    ) {
        inventoryService.deletePlayerInventory(playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
    
    @DeleteMapping(path = "/admin/players/{playerId}/inventory/items/clear")
    public ResponseEntity clearPlayerInventoryItems(
        @PathVariable Long playerId 
    ) {
        inventoryService.clearPlayerInventoryItems(playerId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }
}
