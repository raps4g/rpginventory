package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemCategory;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends CrudRepository<Item, Long>,
        PagingAndSortingRepository<Item, Long> {

    Optional<Item> findByName(String name);

    Optional<Item> findByItemCategoryId(Long categoryId);

    Page<Item> findByItemCategoryId(Long categoryId, Pageable pageable);

    Optional<Item> findByItemRarityId(Long rarityId);
    
    Page<Item> findByItemRarityId(Long rarityId, Pageable pageable);
    
    Page<Item> findByItemCategoryIdAndItemRarityId(Long categoryId, Long rarityId, Pageable pageable);

    Optional<Item> findByValidSlotId(Long slotId);

    boolean existsByName(String name);

    boolean existsByItemCategoryId(Long itemCategoryId);

    boolean existsByItemRarityId(Long itemRarityId);

    boolean existsByValidSlotId(Long slotId);
}
