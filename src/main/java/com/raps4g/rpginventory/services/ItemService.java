package com.raps4g.rpginventory.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemCategory;
import com.raps4g.rpginventory.domain.entities.ItemRarity;
import com.raps4g.rpginventory.domain.entities.dto.ItemCategoryDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemRarityDto;
import com.raps4g.rpginventory.domain.entities.dto.AddItemDto;

public interface ItemService {

    // Mappers
  
    ItemCategory mapFromItemCategoryDto(ItemCategoryDto itemCategoryDto);

    ItemCategoryDto mapToItemCategoryDto(ItemCategory itemCategory);

    ItemRarity mapFromItemRarityDto(ItemRarityDto itemRarityDto);

    ItemRarityDto mapToItemRarityDto(ItemRarity itemRarity);

    Item mapFromItemRequestDto(AddItemDto itemRequestDto);

    ItemDto mapToItemDto(Item item);

    
    // Add

    ItemCategory saveItemCategory(ItemCategory itemCategory);

    ItemRarity saveItemRarity(ItemRarity itemRarity);

    Item saveItem(Item item);

    // Get

    List<ItemCategory> getAllItemCategories();

    List<ItemRarity> getAllItemRarities();

    Item getItem(Long itemId);
    
    Page<Item> getAllItems(Pageable pageable);
    
    Page<Item> getAllItemsByCategory(Pageable pageable, Long categoryId);

    Page<Item> getAllItemsByRarity(Pageable pageable, Long rarityId);

    Page<Item> getAllItemsByCategoryAndRarity(Pageable pageable, Long categoryId, Long rarityId);

    // Delete

    void deleteItemCategory(Long itemCategoryId);
    
    void deleteItemRarity(Long itemRarityId);

    void deleteItem(Long itemId);

}
