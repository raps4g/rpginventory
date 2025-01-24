package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.model.EquipmentItem;
import com.raps4g.rpginventory.dto.EquipmentItemDto;
import com.raps4g.rpginventory.dto.InventoryItemRequestDto;
import com.raps4g.rpginventory.services.EquipmentService;
import com.raps4g.rpginventory.validation.InventoryItemValidationGroup;

@RestController
public class EquipmentController {
    
    @Autowired
    private EquipmentService equipmentService;


    // POST

    @PostMapping(path = "/players/{playerId}/equipment")
    public ResponseEntity<EquipmentItemDto> equipItem (
        @PathVariable Long playerId,
        @Validated(InventoryItemValidationGroup.class) 
            @RequestBody InventoryItemRequestDto inventoryItemRequestDto
    ) throws IllegalAccessException {

        Long inventoryItemId = inventoryItemRequestDto.getInventoryItemId();
        EquipmentItem equipmentItem = equipmentService.equipItem(playerId, inventoryItemId);
        EquipmentItemDto equipmentItemDto = equipmentService.mapToEquipmentItemDto(equipmentItem);

        return new ResponseEntity<>(equipmentItemDto, HttpStatus.OK);
    }

    @PostMapping(path = "/players/{playerId}/equipment/unequip")
    public ResponseEntity unequipItem(
        @PathVariable Long playerId,
        @Validated(InventoryItemValidationGroup.class)
            @RequestBody InventoryItemRequestDto inventoryItemRequestDto
    ) throws IllegalAccessException {

        Long inventoryItemId = inventoryItemRequestDto.getInventoryItemId();
        equipmentService.unequipItem(playerId, inventoryItemId);

        return new ResponseEntity(HttpStatus.OK);
    }

    // GET
    
    @GetMapping(path = "/players/{playerId}/equipment")
    public ResponseEntity<List<EquipmentItemDto>> getEquippedItems(@PathVariable Long playerId) {

        List<EquipmentItem> equippedItems = equipmentService.getEquippedItems(playerId);
        List<EquipmentItemDto> equippedItemsDto = equippedItems.stream()
            .map(equipmentService::mapToEquipmentItemDto)
            .collect(Collectors.toList());

        return new ResponseEntity<>(equippedItemsDto,HttpStatus.OK);
    }
}
