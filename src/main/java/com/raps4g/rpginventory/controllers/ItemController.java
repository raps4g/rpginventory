package com.raps4g.rpginventory.controllers;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
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

import com.raps4g.rpginventory.model.Item;
import com.raps4g.rpginventory.model.ItemCategory;
import com.raps4g.rpginventory.model.ItemRarity;
import com.raps4g.rpginventory.dto.ItemCategoryDto;
import com.raps4g.rpginventory.dto.ItemDto;
import com.raps4g.rpginventory.dto.ItemRarityDto;
import com.raps4g.rpginventory.dto.ItemRequestDto;
import com.raps4g.rpginventory.services.ItemService;

import jakarta.validation.Valid;

@RestController
public class ItemController {

    @Autowired
    private ItemService itemService;


    // POST

    @PostMapping(path = "/items/categories")
    public ResponseEntity<ItemCategoryDto> createItemCategory(@Valid @RequestBody ItemCategoryDto itemCategoryDto) {

        ItemCategory itemCategory = itemService.mapFromItemCategoryDto(itemCategoryDto);
        ItemCategory savedItemCategory = itemService.saveItemCategory(itemCategory);
        ItemCategoryDto savedItemCategoryDto = itemService.mapToItemCategoryDto(savedItemCategory);

        return new ResponseEntity<>(savedItemCategoryDto, HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/items/rarities")
    public ResponseEntity<ItemRarityDto> createItemRarity(@Valid @RequestBody ItemRarityDto itemRarityDto) {

        ItemRarity itemRarity = itemService.mapFromItemRarityDto(itemRarityDto);
        ItemRarity savedItemRarity = itemService.saveItemRarity(itemRarity);
        ItemRarityDto savedItemRarityDto = itemService.mapToItemRarityDto(savedItemRarity);

        return new ResponseEntity<>(savedItemRarityDto, HttpStatus.CREATED);
    }
    
    @PostMapping(path = "/items")
    public ResponseEntity<ItemDto> createItem(@Valid @RequestBody ItemRequestDto itemRequestDto) {

            Item item = itemService.mapFromItemRequestDto(itemRequestDto);
            Item savedItem = itemService.saveItem(item);
            ItemDto savedItemDto = itemService.mapToItemDto(savedItem);

            return new ResponseEntity<>(savedItemDto, HttpStatus.CREATED);
    }


    // PUT

    @PutMapping(path = "/items/categories/{itemCategoryId}")
    public ResponseEntity<ItemCategoryDto> updateItemCategory(
        @PathVariable Long itemCategoryId, 
        @Valid @RequestBody ItemCategoryDto itemCategoryDto
    ) {

        ItemCategory itemCategory = itemService.mapFromItemCategoryDto(itemCategoryDto);
        itemCategory.setId(itemCategoryId);
        ItemCategory savedItemCategory = itemService.saveItemCategory(itemCategory);
        ItemCategoryDto savedItemCategoryDto = itemService.mapToItemCategoryDto(savedItemCategory);
            
        return new ResponseEntity<>(savedItemCategoryDto, HttpStatus.OK);
    }

    @PutMapping(path = "/items/rarities/{itemRarityId}")
    public ResponseEntity<ItemRarityDto> updateItemRarity(
        @PathVariable Long itemRarityId, 
        @Valid @RequestBody ItemRarityDto itemRarityDto
    ) {
        ItemRarity itemRarity = itemService.mapFromItemRarityDto(itemRarityDto);
        itemRarity.setId(itemRarityId);
        ItemRarity savedItemRarity = itemService.saveItemRarity(itemRarity);
        ItemRarityDto savedItemRarityDto = itemService.mapToItemRarityDto(savedItemRarity);
        
        return new ResponseEntity<>(savedItemRarityDto, HttpStatus.OK);
    }

    @PutMapping(path = "/items/{itemId}")
    public ResponseEntity<ItemDto> updateItem(
        @PathVariable Long itemId, 
        @Valid @RequestBody ItemRequestDto itemRequestDto
    ) {
        Item item = itemService.mapFromItemRequestDto(itemRequestDto);
        item.setId(itemId);
        Item savedItem = itemService.saveItem(item);
        ItemDto savedItemDto = itemService.mapToItemDto(savedItem);

        return new ResponseEntity<>(savedItemDto, HttpStatus.OK);
    }


    // GET

    @GetMapping(path = "/items/categories")
    public List<ItemCategoryDto> getAllItemCategories() {

        List<ItemCategory> itemCategories = itemService.getAllItemCategories();

        List<ItemCategoryDto> itemCategoriesDto = itemCategories.stream()
        .map(itemService::mapToItemCategoryDto)
        .collect(Collectors.toList());

        return itemCategoriesDto;
    }
    
    @GetMapping(path = "/items/rarities")
    public List<ItemRarityDto> getAllItemRarities() {

        List<ItemRarity> itemRarities = itemService.getAllItemRarities();

        List<ItemRarityDto> itemRaritiesDto = itemRarities.stream()
        .map(itemService::mapToItemRarityDto)
        .collect(Collectors.toList());

        return itemRaritiesDto;
    }

    @GetMapping(path = "/items")
    public Page<ItemDto> getAllItems(
        Pageable pageable,
        @RequestParam(required = false) Long categoryId, 
        @RequestParam(required = false) Long rarityId
    ) {

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

        return items.map(itemService::mapToItemDto);
    }

    @GetMapping(path = "/items/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long itemId) {

        Item foundItem = itemService.getItem(itemId);
        ItemDto foundItemDto = itemService.mapToItemDto(foundItem); 
        return new ResponseEntity<>(foundItemDto, HttpStatus.OK);
    }

    // DELETE
    
    @DeleteMapping(path = "/items/categories/{itemCategoryId}")
    public ResponseEntity deleteItemCategory(@PathVariable Long itemCategoryId) {
        itemService.deleteItemCategory(itemCategoryId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/items/rarities/{itemRarityId}")
    public ResponseEntity deleteItemRarity(@PathVariable Long itemRarityId) {
        itemService.deleteItemRarity(itemRarityId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

    @DeleteMapping(path = "/items/{itemId}")
    public ResponseEntity deleteItem(@PathVariable Long itemId) {
        itemService.deleteItem(itemId);
        return new ResponseEntity(HttpStatus.NO_CONTENT);
    }

}
