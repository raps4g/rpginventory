package com.raps4g.rpginventory.services;

import java.util.List;

import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.dto.SlotDto;

public interface SlotService {

    // Mappers
   
    Slot convertFromSlotDto(SlotDto itemDto);

    SlotDto convertToSlotDto(Slot item);

    // Add

    Slot saveSlot(Slot item);

    // Get

    List<Slot> getAllSlots();
    
    // Delete

    void deleteSlot(Long itemId);
    
}
