package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.Optional;
import java.util.Locale.Category;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemCategory;
import com.raps4g.rpginventory.domain.entities.ItemRarity;
import com.raps4g.rpginventory.domain.entities.dto.ItemCategoryDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemRarityDto;
import com.raps4g.rpginventory.services.ItemService;

@RestController
public class ItemController {
    private ItemService itemService;

    public ItemController(ItemService itemService) {
        this.itemService = itemService;
    }

    // POST

    @PostMapping(path = "/itemCategories")
    public ResponseEntity<ItemCategoryDto> addItemCategory(@RequestBody ItemCategoryDto itemCategoryDto) {
            ItemCategory itemCategory = itemService.convertFromItemCategoryDto(itemCategoryDto);
            ItemCategory savedItemCategory = itemService.saveItemCategory(itemCategory);
            ItemCategoryDto savedItemCategoryDto = itemService.convertToItemCategoryDto(savedItemCategory);
            return new ResponseEntity<>(savedItemCategoryDto, HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/itemRarities")
    public ResponseEntity<ItemRarityDto> addItemRarity(@RequestBody ItemRarityDto itemRarityDto) {
            ItemRarity itemRarity = itemService.convertFromItemRarityDto(itemRarityDto);
            ItemRarity savedItemRarity = itemService.saveItemRarity(itemRarity);
            ItemRarityDto savedItemRarityDto = itemService.convertToItemRarityDto(savedItemRarity);
            return new ResponseEntity<>(savedItemRarityDto, HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/items")
    public ResponseEntity<ItemDto> addItem(@RequestBody ItemDto itemDto) {
            Item item = itemService.convertFromItemDto(itemDto);
            Item savedItem = itemService.saveItem(item);
            ItemDto savedItemDto = itemService.convertToItemDto(savedItem);
            return new ResponseEntity<>(savedItemDto, HttpStatus.CREATED);
    }


    // PUT

    @PutMapping(path = "/itemRarities/{itemRarityId}")
    public ResponseEntity<ItemRarityDto> uptadeItemRarity(@PathVariable Long itemRarityId, 
        @RequestBody ItemRarityDto itemRarityDto) {
            ItemRarity itemRarity = itemService.convertFromItemRarityDto(itemRarityDto);
            itemRarity.setId(itemRarityId);
            ItemRarity savedItemRarity = itemService.saveItemRarity(itemRarity);
            ItemRarityDto savedItemRarityDto = itemService.convertToItemRarityDto(savedItemRarity);
            return new ResponseEntity<>(savedItemRarityDto, HttpStatus.OK);
    }

    @PutMapping(path = "/itemCategories/{itemCategoryId}")
    public ResponseEntity<ItemCategoryDto> uptadeItemCategory(@PathVariable Long itemCategoryId, 
        @RequestBody ItemCategoryDto itemCategoryDto) {
            ItemCategory itemCategory = itemService.convertFromItemCategoryDto(itemCategoryDto);
            itemCategory.setId(itemCategoryId);
            ItemCategory savedItemCategory = itemService.saveItemCategory(itemCategory);
            ItemCategoryDto savedItemCategoryDto = itemService.convertToItemCategoryDto(savedItemCategory);
            return new ResponseEntity<>(savedItemCategoryDto, HttpStatus.OK);
    }

    @PutMapping(path = "/items/{itemId}")
    public ResponseEntity<ItemDto> uptadeItem(@PathVariable Long itemId, 
        @RequestBody ItemDto itemDto) {
            Item item = itemService.convertFromItemDto(itemDto);
            item.setId(itemId);
            Item savedItem = itemService.saveItem(item);
            ItemDto savedItemDto = itemService.convertToItemDto(savedItem);
            return new ResponseEntity<>(savedItemDto, HttpStatus.OK);
    }


    // GET

    @GetMapping(path = "/itemRarities")
    public List<ItemRarityDto> getAllItemRarities() {
        List<ItemRarity> items = itemService.getAllItemRarities();
        return items.stream()
        .map(itemService::convertToItemRarityDto)
        .collect(Collectors.toList());
    }

    @GetMapping(path = "/itemCategories")
    public List<ItemCategoryDto> getAllItemCategories() {
        List<ItemCategory> items = itemService.getAllItemCategories();
        return items.stream()
        .map(itemService::convertToItemCategoryDto)
        .collect(Collectors.toList());
    }
    
    @GetMapping(path = "/items")
    public Page<ItemDto> getAllItems( Pageable pageable,
        @RequestParam(required = false) Long categoryId, 
        @RequestParam(required = false) Long rarityId) {

        Page<Item> items;
        if (categoryId != null && rarityId != null) {
            items = itemService.getAllItemsByCategoryAndRarity(pageable, categoryId, rarityId);
        } else if (categoryId != null) {
            items = itemService.getAllItemsByCategory(pageable, categoryId);
        } else if (rarityId != null) {
            items = itemService.getAllItemsByRarity(pageable, rarityId);
        } else {
            items = itemService.getAllItems(pageable);
        }

        return items.map(itemService::convertToItemDto);
    }

    @GetMapping(path = "/items/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long itemId) {
        Optional<Item> foundItem = itemService.getItem(itemId);
        return foundItem.map(item -> {
            ItemDto itemDto = itemService.convertToItemDto(item);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

    // DELETE
    
    @DeleteMapping(path = "/itemRarities/{itemRarityId}")
    public ResponseEntity deleteItemRarity(@PathVariable Long itemRarityId) {
        itemService.deleteItemRarity(itemRarityId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/itemCategories/{itemCategoryId}")
    public ResponseEntity deleteItemCategory(@PathVariable Long itemCategoryId) {
        itemService.deleteItemCategory(itemCategoryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
