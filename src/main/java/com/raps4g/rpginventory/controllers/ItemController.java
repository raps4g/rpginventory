package com.raps4g.rpginventory.controllers;

import java.util.Optional;

import org.hibernate.annotations.Parameter;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.domain.entities.dto.ReducedItemDto;
import com.raps4g.rpginventory.mappers.Mapper;
import com.raps4g.rpginventory.services.ItemService;

@RestController
public class ItemController {
    private ItemService itemService;

    private Mapper<Item, ReducedItemDto> itemMapper;

    public ItemController(ItemService itemService, 
                            Mapper<Item, ReducedItemDto> itemMapper) {
        this.itemService = itemService;
        this.itemMapper = itemMapper;
    }

    @PostMapping(path = "/items")
    public ResponseEntity<ItemDto> addItem(@RequestBody ReducedItemDto itemDto) {
        Item item = itemService.convertFromReducedItemDto(itemDto);
        Item savedItem = itemService.addItem(item);
        ItemDto savedItemDto = itemService.convertToItemDto(savedItem);
        return new ResponseEntity<>(savedItemDto, HttpStatus.CREATED);
    }

    @GetMapping(path = "/items/{itemId}")
    public ResponseEntity<ItemDto> getItem(@PathVariable Long itemId) {
        Optional<Item> foundItem = itemService.getItem(itemId);
        return foundItem.map(item -> {
            ItemDto itemDto = itemService.convertToItemDto(item);
            return new ResponseEntity<>(itemDto, HttpStatus.OK);
        }).orElse(new ResponseEntity<>(HttpStatus.NOT_FOUND));
    }

}
