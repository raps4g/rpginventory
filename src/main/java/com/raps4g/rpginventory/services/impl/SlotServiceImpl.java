package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.dto.SlotDto;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.services.SlotService;


@Service
public class SlotServiceImpl implements SlotService{
  
    @Autowired
    private ModelMapper modelMapper;
    
    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private ItemRepository itemRepository;


    // Mappers

    @Override
    public Slot convertFromSlotDto(SlotDto slotDto) {
        return modelMapper.map(slotDto, Slot.class);
    }

    @Override
    public SlotDto convertToSlotDto(Slot slot) {
        return modelMapper.map(slot, SlotDto.class);
    }

    // Add

    @Override
    public Slot saveSlot(Slot slot) {
        if (slotRepository.existsByName(slot.getName()) && slot.getId() == null) {
            throw new ResourceAlreadyExistsException("Slot with name '" + slot.getName() + "' already exists.");
        }
        return slotRepository.save(slot);
    }

    // Get

    @Override
    public List<Slot> getAllSlots() {
        return StreamSupport
            .stream(
                slotRepository.findAll().spliterator(),
                false)
        .collect(Collectors.toList());
    }

    // Delete

    @Override
    public void deleteSlot(Long slotId) {
        if (itemRepository.existsByValidSlotId(slotId)) {
            throw new DataIntegrityViolationException("Slot with id " 
                + slotId + " cannot be deleted because it is referenced by one or more items.");
        }
        slotRepository.deleteById(slotId);
    }
    
}
