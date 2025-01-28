package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.model.Item;
import com.raps4g.rpginventory.model.ItemCategory;
import com.raps4g.rpginventory.model.ItemRarity;
import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.dto.ItemCategoryDto;
import com.raps4g.rpginventory.dto.ItemDto;
import com.raps4g.rpginventory.dto.ItemRarityDto;
import com.raps4g.rpginventory.dto.ItemRequestDto;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.repositories.InventoryItemRepository;
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
    private InventoryItemRepository inventoryItemRepository;
    
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

        if (itemCategoryRepository.existsByName(itemCategory.getName()) && itemCategory.getId() == null) {
            throw new ResourceAlreadyExistsException("Item category with name '" + itemCategory.getName() + "' already exists.");
        }
        return itemCategoryRepository.save(itemCategory);
    }

    @Override
    public ItemRarity saveItemRarity(ItemRarity itemRarity) {

        if (itemRarityRepository.existsByName(itemRarity.getName()) && itemRarity.getId() == null) {
            throw new ResourceAlreadyExistsException("Item rarity with name '" + itemRarity.getName() + "' already exists.");
        }
        return itemRarityRepository.save(itemRarity);
    }
   
    @Override
    public Item saveItem(Item item) {
        if (itemRepository.existsByName(item.getName()) && item.getId() == null) {
            throw new ResourceAlreadyExistsException("Item with name '" + item.getName() + "' already exists.");
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
    public void deleteItemCategory(Long itemCategoryId) {
        if (itemRepository.existsByItemCategoryId(itemCategoryId)) {
            throw new DataIntegrityViolationException("Item category with id " 
                + itemCategoryId + " cannot be deleted because it is referenced by one or more items.");
        }
        itemCategoryRepository.deleteById(itemCategoryId);
    }
    
    @Override
    public void deleteItemRarity(Long itemRarityId) {
        if (itemRepository.existsByItemRarityId(itemRarityId)) {
            throw new DataIntegrityViolationException("Item rarity with id " 
                + itemRarityId + " cannot be deleted because it is referenced by one or more items.");
        }
        itemRarityRepository.deleteById(itemRarityId);
    }

    @Override
    public void deleteItem(Long itemId) {
        if (inventoryItemRepository.existsByItemId(itemId)) {
            throw new DataIntegrityViolationException("Item with id " 
                + itemId + " cannot be deleted because it is present in one or more inventories.");
        }
        itemRepository.deleteById(itemId);
    }
}
