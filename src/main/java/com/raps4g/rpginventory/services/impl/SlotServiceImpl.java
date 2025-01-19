package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.domain.entities.Slot;
import com.raps4g.rpginventory.domain.entities.dto.SlotDto;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.services.SlotService;


@Service
public class SlotServiceImpl implements SlotService{
   
    private ModelMapper modelMapper;
    private SlotRepository slotRepository;


    public SlotServiceImpl( SlotRepository slotRepository,
                ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.slotRepository = slotRepository;
    }
    
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
        if (slotRepository.findByName(slot.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Slot named \"" + slot.getName() + "\" already exists.");
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
    // placeholders, this funcitons should manage cases where slots, categories,
    // or rarities are referenced in other objects.

    @Override
    public void deleteSlot(Long slotId) {
        slotRepository.deleteById(slotId);
    }
    
}
