package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.dto.SlotDto;
import com.raps4g.rpginventory.services.SlotService;

import jakarta.validation.Valid;

@RestController
public class SlotController {

    @Autowired
    private SlotService slotService;

    // POST

    @PostMapping(path = "/admin/slots")
    public ResponseEntity<SlotDto> createSlot(@Valid @RequestBody SlotDto slotDto) {
        Slot slot = slotService.convertFromSlotDto(slotDto);
        Slot savedSlot = slotService.saveSlot(slot);
        SlotDto savedSlotDto = slotService.convertToSlotDto(savedSlot);
        return new ResponseEntity<>(savedSlotDto, HttpStatus.CREATED);
    }
    

    // PUT
   
    @PutMapping(path = "/admin/slots/{slotId}")
    public ResponseEntity<SlotDto> uptadeSlot(
        @PathVariable Long slotId, 
        @Valid @RequestBody SlotDto slotDto
    ) {
        Slot slot = slotService.convertFromSlotDto(slotDto);
        slot.setId(slotId);
        Slot savedSlot = slotService.saveSlot(slot);
        SlotDto savedSlotDto = slotService.convertToSlotDto(savedSlot);
        return new ResponseEntity<>(savedSlotDto, HttpStatus.OK);
    }


    // GET

    @GetMapping(path = {"/slots"})
    public ResponseEntity<List<SlotDto>> getAllSlots() {
        List<Slot> slots = slotService.getAllSlots();
        List<SlotDto> slotsDto = slots.stream()
        .map(slotService::convertToSlotDto)
        .collect(Collectors.toList());
        return new ResponseEntity<>(slotsDto, HttpStatus.OK);
    }

    @DeleteMapping(path = "/admin/slots/{slotId}")
    public ResponseEntity deleteSlot(@PathVariable Long slotId) {
        slotService.deleteSlot(slotId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
