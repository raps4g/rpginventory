package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemCategory;
import com.raps4g.rpginventory.domain.entities.ItemRarity;
import com.raps4g.rpginventory.domain.entities.Slot;
import com.raps4g.rpginventory.domain.entities.dto.ItemCategoryDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemRarityDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemRequestDto;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.repositories.ItemCategoryRepository;
import com.raps4g.rpginventory.repositories.ItemRarityRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.services.ItemService;


@Service
public class ItemServiceImpl implements ItemService{

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    
    @Autowired
    private ItemRarityRepository itemRarityRepository;
    
    @Autowired
    private ItemRepository itemRepository;
    
    @Autowired
    private SlotRepository slotRepository;
    
    @Autowired
    private ModelMapper modelMapper;
   

    // Mappers

    @Override
    public ItemCategory mapFromItemCategoryDto(ItemCategoryDto itemCategoryDto) {
        return modelMapper.map(itemCategoryDto, ItemCategory.class);
    }
    
    @Override
    public ItemCategoryDto mapToItemCategoryDto(ItemCategory itemCategory) {
        return modelMapper.map(itemCategory, ItemCategoryDto.class);
    }
    
    @Override
    public ItemRarity mapFromItemRarityDto(ItemRarityDto itemRarityDto) {
        return modelMapper.map(itemRarityDto, ItemRarity.class);
    }

    @Override
    public ItemRarityDto mapToItemRarityDto(ItemRarity itemRarity) {
        return modelMapper.map(itemRarity, ItemRarityDto.class);
    }
    
    @Override
    public Item mapFromItemRequestDto(ItemRequestDto itemRequestDto) {

        ItemCategory itemCategory = itemCategoryRepository.findById(itemRequestDto.getItemCategoryId())
            .orElseThrow(() -> new ResourceNotFoundException("Item category with id " + itemRequestDto.getItemCategoryId() + " not found."));

        ItemRarity itemRarity = itemRarityRepository.findById(itemRequestDto.getItemRarityId())
            .orElseThrow(() -> new ResourceNotFoundException("Item rarity with id " + itemRequestDto.getItemRarityId() + " not found."));
        
        Slot validSlot = slotRepository.findById(itemRequestDto.getValidSlotId())
            .orElseThrow(() -> new ResourceNotFoundException("Slot with id " + itemRequestDto.getValidSlotId() + " not found."));

        Item item = Item.builder()
            .name(itemRequestDto.getName())
            .description(itemRequestDto.getDescription())
            .itemCategory(itemCategory)
            .itemRarity(itemRarity)
            .validSlot(validSlot)
            .value(itemRequestDto.getValue())
            .build();

        return item;        
    }

    @Override
    public ItemDto mapToItemDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }


    // Add

    @Override
    public ItemCategory saveItemCategory(ItemCategory itemCategory) {

        if (itemCategoryRepository.findByName(itemCategory.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Item category named \""+ itemCategory.getName() + "\" already exists.");
        }
        return itemCategoryRepository.save(itemCategory);

    }

    @Override
    public ItemRarity saveItemRarity(ItemRarity itemRarity) {

        if (itemRarityRepository.findByName(itemRarity.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Item rarity named \"" + itemRarity.getName() + "\" already exists.");
        }
        return itemRarityRepository.save(itemRarity);

    }
   
    @Override
    public Item saveItem(Item item) {
        if (itemRepository.findByName(item.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Item named \"" + item.getName() + "\" already exists.");
        }

        if (item.getItemCategory().getId() == null) {
            itemCategoryRepository.save(item.getItemCategory());
        }

        if (item.getItemRarity().getId() == null) {
            itemRarityRepository.save(item.getItemRarity());
        }

        if (item.getValidSlot().getId() == null) {
            slotRepository.save(item.getValidSlot());
        }

        return itemRepository.save(item);
    }


    // Get

    @Override
    public List<ItemCategory> getAllItemCategories() {
        return StreamSupport
            .stream(
                itemCategoryRepository.findAll().spliterator(),
                false)
        .collect(Collectors.toList());
    }

    @Override
    public List<ItemRarity> getAllItemRarities() {
        return StreamSupport
            .stream(
                itemRarityRepository.findAll().spliterator(),
                false)
        .collect(Collectors.toList());
    }

    @Override
    public Item getItem(Long itemId) {
        Item foundItem = itemRepository.findById(itemId)
            .orElseThrow(() -> new ResourceNotFoundException("Item with id " + itemId + " not found."));
        return foundItem;
    }

    @Override
    public List<Item> getAllItems() {
        return StreamSupport
            .stream(
                itemRepository.findAll().spliterator(),
                false)
        .collect(Collectors.toList());
    }

    @Override
    public Page<Item> getAllItems(Pageable pageable) {
        return itemRepository.findAll(pageable);
    }

    @Override
    public Page<Item> getAllItemsByCategory(Pageable pageable, Long categoryId) {
        return itemRepository.findByItemCategoryId(categoryId, pageable);
    }

    @Override
    public Page<Item> getAllItemsByRarity(Pageable pageable, Long rarityId) {
        return itemRepository.findByItemRarityId(rarityId, pageable);
    }

    @Override
    public Page<Item> getAllItemsByCategoryAndRarity(Pageable pageable, Long categoryId, Long rarityId) {
        return itemRepository.findByItemCategoryIdAndItemRarityId(categoryId, rarityId, pageable);
    }


    // Delete

    @Override
    public void deleteItemCategory(Long itemId) {
        itemCategoryRepository.deleteById(itemId);
    }
    
    @Override
    public void deleteItemRarity(Long itemId) {
        itemRarityRepository.deleteById(itemId);
    }

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
}
