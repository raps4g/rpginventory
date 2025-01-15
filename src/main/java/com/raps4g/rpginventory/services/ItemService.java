package com.raps4g.rpginventory.services;

import java.util.Optional;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemRarity;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.domain.entities.dto.ReducedItemDto;

public interface ItemService {

    ItemRarity addItemRarity(String name);
    
    Item addItem(Item item);

    Item convertFromReducedItemDto(ReducedItemDto itemDto);

    ItemDto convertToItemDto(Item item);

    Optional<Item> getItem(Long itemId);
}
