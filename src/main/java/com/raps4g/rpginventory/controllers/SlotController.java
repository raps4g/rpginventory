package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.domain.entities.Slot;
import com.raps4g.rpginventory.domain.entities.dto.SlotDto;
import com.raps4g.rpginventory.services.SlotService;

import jakarta.persistence.EntityNotFoundException;

@RestController
public class SlotController {
    private SlotService slotService;

    public SlotController(SlotService slotService) {
        this.slotService = slotService;
    }

    // POST

    @PostMapping(path = "/slots")
    public ResponseEntity<SlotDto> createSlot(@RequestBody SlotDto slotDto) {
        try {
            Slot slot = slotService.convertFromSlotDto(slotDto);
            Slot savedSlot = slotService.saveSlot(slot);
            SlotDto savedSlotDto = slotService.convertToSlotDto(savedSlot);
            return new ResponseEntity<>(savedSlotDto, HttpStatus.CREATED);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }
    

    // PUT
   
    @PutMapping(path = "/slots/{slotId}")
    public ResponseEntity<SlotDto> uptadeSlot(@PathVariable Long slotId, 
        @RequestBody SlotDto slotDto) {
        try {
            Slot slot = slotService.convertFromSlotDto(slotDto);
            slot.setId(slotId);
            Slot savedSlot = slotService.saveSlot(slot);
            SlotDto savedSlotDto = slotService.convertToSlotDto(savedSlot);
            return new ResponseEntity<>(savedSlotDto, HttpStatus.OK);
        } catch (EntityNotFoundException e) {
            return new ResponseEntity<>(HttpStatus.BAD_REQUEST); 
        }
    }


    // GET

    @GetMapping(path = "/slots")
    public List<SlotDto> getAllSlots() {
        List<Slot> slots = slotService.getAllSlots();
        return slots.stream()
        .map(slotService::convertToSlotDto)
        .collect(Collectors.toList());
    }

    @DeleteMapping(path = "/slots/{slotId}")
    public ResponseEntity deleteSlot(@PathVariable Long slotId) {
        slotService.deleteSlot(slotId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
