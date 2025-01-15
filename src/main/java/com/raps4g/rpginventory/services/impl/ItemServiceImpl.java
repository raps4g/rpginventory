package com.raps4g.rpginventory.services.impl;

import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemCategory;
import com.raps4g.rpginventory.domain.entities.ItemRarity;
import com.raps4g.rpginventory.domain.entities.Slot;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.domain.entities.dto.ReducedItemDto;
import com.raps4g.rpginventory.repositories.ItemCategoryRepository;
import com.raps4g.rpginventory.repositories.ItemRarityRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.services.ItemService;

import jakarta.persistence.EntityNotFoundException;

@Service
public class ItemServiceImpl implements ItemService{
   
    private ModelMapper modelMapper;
    private ItemRarityRepository itemRarityRepository;
    private ItemCategoryRepository itemCategoryRepository;
    private SlotRepository slotRepository;
    private ItemRepository itemRepository;


    public ItemServiceImpl( ItemRepository itemRepository,
                SlotRepository slotRepository,
                ItemCategoryRepository itemCategoryRepository,
                ItemRarityRepository itemRarityRepository,
                ModelMapper modelMapper) {
        this.modelMapper = modelMapper;
        this.itemRepository = itemRepository;
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemRarityRepository = itemRarityRepository;
        this.slotRepository = slotRepository;
    }

    @Override
    public ItemRarity addItemRarity(String name) {
        ItemRarity itemRarity = new ItemRarity();
        itemRarity.setName(name);
        return itemRarityRepository.save(itemRarity);
    }
    
    @Override
    public Item addItem(Item item) {
        return itemRepository.save(item);
    }

    @Override
    public ItemDto convertToItemDto(Item item) {

        return modelMapper.map(item, ItemDto.class);
    }

    @Override
    public Item convertFromReducedItemDto(ReducedItemDto itemDto) {
        ItemCategory itemCategory = itemCategoryRepository.findById(itemDto.getCategoryId())
            .orElseThrow(() -> new EntityNotFoundException("ItemCategory not found"));
        ItemRarity itemRarity = itemRarityRepository.findById(itemDto.getRarityId())
            .orElseThrow(() -> new EntityNotFoundException("ItemCategory not found"));
        Slot validSlot = slotRepository.findById(itemDto.getValidSlotId())
            .orElseThrow(() -> new EntityNotFoundException("Slot not found"));

        Item item = Item.builder()
            .name(itemDto.getName())
            .description(itemDto.getDescription())
            .itemCategory(itemCategory)
            .itemRarity(itemRarity)
            .validSlot(validSlot)
            .value(itemDto.getValue())
            .build();

        return item;        
    }

    @Override
    public Optional<Item> getItem(Long itemId) {
        Optional<Item> foundItem = itemRepository.findById(itemId);
        return foundItem;
    }

}
