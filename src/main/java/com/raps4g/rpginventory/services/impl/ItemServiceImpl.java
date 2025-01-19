package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.modelmapper.ModelMapper;
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
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.repositories.ItemCategoryRepository;
import com.raps4g.rpginventory.repositories.ItemRarityRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.services.ItemService;


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
    
    // Mappers

    @Override
    public Item convertFromItemDto(ItemDto itemDto) {
        
        ItemCategory itemCategory = itemCategoryRepository.findByName(itemDto.getItemCategory().getName())
            .orElseGet(() -> new ItemCategory(null, itemDto.getItemCategory().getName()));

        ItemRarity itemRarity = itemRarityRepository.findByName(itemDto.getItemRarity().getName())
            .orElseGet(() -> new ItemRarity(null, itemDto.getItemRarity().getName()));
        
        Slot validSlot = slotRepository.findByName(itemDto.getValidSlot().getName())
            .orElseGet(() -> new Slot(null, itemDto.getValidSlot().getName()));

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
    public ItemDto convertToItemDto(Item item) {
        return modelMapper.map(item, ItemDto.class);
    }

    @Override
    public ItemCategory convertFromItemCategoryDto(ItemCategoryDto itemCategoryDto) {
        return modelMapper.map(itemCategoryDto, ItemCategory.class);
    }

    @Override
    public ItemRarity convertFromItemRarityDto(ItemRarityDto itemRarityDto) {
        return modelMapper.map(itemRarityDto, ItemRarity.class);
    }

    @Override
    public ItemCategoryDto convertToItemCategoryDto(ItemCategory itemCategory) {
        return modelMapper.map(itemCategory, ItemCategoryDto.class);
    }

    @Override
    public ItemRarityDto convertToItemRarityDto(ItemRarity itemRarity) {
        return modelMapper.map(itemRarity, ItemRarityDto.class);
    }


    // Add

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

    @Override
    public ItemRarity saveItemRarity(ItemRarity itemRarity) {
        if (itemRarityRepository.findByName(itemRarity.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Item rarity named \"" + itemRarity.getName() + "\" already exists.");
        }
        return itemRarityRepository.save(itemRarity);
    }
   
    @Override
    public ItemCategory saveItemCategory(ItemCategory itemCategory) {
        if (itemCategoryRepository.findByName(itemCategory.getName()).isPresent()) {
            throw new ResourceAlreadyExistsException("Item category named \""+ itemCategory.getName() + "\" already exists.");
        }

        return itemCategoryRepository.save(itemCategory);
    }
    

    // Get

    @Override
    public Optional<Item> getItem(Long itemId) {
        Optional<Item> foundItem = itemRepository.findById(itemId);
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
    public Page<Item> getAllItemsByCategoryAndRarity(Pageable pageable, Long categoryId, Long rarityId) {
        return itemRepository.findByItemCategoryIdAndItemRarityId(categoryId, rarityId, pageable);
    }

    @Override
    public Page<Item> getAllItemsByRarity(Pageable pageable, Long rarityId) {
        return itemRepository.findByItemRarityId(rarityId, pageable);
    }

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


    // Delete
    // placeholders, this funcitons should manage cases where items, categories,
    // or rarities are referenced in other objects.

    @Override
    public void deleteItem(Long itemId) {
        itemRepository.deleteById(itemId);
    }
    
    @Override
    public void deleteItemCategory(Long itemId) {
        itemCategoryRepository.deleteById(itemId);
    }
    
    @Override
    public void deleteItemRarity(Long itemId) {
        itemRarityRepository.deleteById(itemId);
    }

}
