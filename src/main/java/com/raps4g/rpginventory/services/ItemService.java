package com.raps4g.rpginventory.services;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemCategory;
import com.raps4g.rpginventory.domain.entities.ItemRarity;
import com.raps4g.rpginventory.domain.entities.dto.ItemCategoryDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemDto;
import com.raps4g.rpginventory.domain.entities.dto.ItemRarityDto;

public interface ItemService {

    // Mappers
   
    Item convertFromItemDto(ItemDto itemDto);

    ItemDto convertToItemDto(Item item);

    ItemCategory convertFromItemCategoryDto(ItemCategoryDto itemCategoryDto);

    ItemCategoryDto convertToItemCategoryDto(ItemCategory itemCategory);

    ItemRarity convertFromItemRarityDto(ItemRarityDto itemRarityDto);

    ItemRarityDto convertToItemRarityDto(ItemRarity itemRarity);
    
    // Add

    ItemRarity saveItemRarity(ItemRarity itemRarity);

    ItemCategory saveItemCategory(ItemCategory itemCategory);

    Item saveItem(Item item);

    // Get

    Optional<Item> getItem(Long itemId);
    
    List<Item> getAllItems();

    Page<Item> getAllItems(Pageable pageable);
    
    Page<Item> getAllItemsByCategory(Pageable pageable, Long categoryId);

    Page<Item> getAllItemsByRarity(Pageable pageable, Long rarityId);

    Page<Item> getAllItemsByCategoryAndRarity(Pageable pageable, Long categoryId, Long rarityId);

    List<ItemCategory> getAllItemCategories();
    
    List<ItemRarity> getAllItemRarities();

    // Delete

    void deleteItem(Long itemId);
    
    void deleteItemCategory(Long itemCategoryId);
    
    void deleteItemRarity(Long itemRarityId);


}
