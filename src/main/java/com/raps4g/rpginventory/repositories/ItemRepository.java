package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.Item;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ItemRepository extends JpaRepository<Item, Long> {

    Optional<Item> findByName(String name);

    Page<Item> findByItemCategoryId(Long categoryId, Pageable pageable);

    Page<Item> findByItemRarityId(Long rarityId, Pageable pageable);
    
    Page<Item> findByItemCategoryIdAndItemRarityId(Long categoryId, Long rarityId, Pageable pageable);

    boolean existsByName(String name);

    boolean existsByItemCategoryId(Long itemCategoryId);

    boolean existsByItemRarityId(Long itemRarityId);

    boolean existsByValidSlotId(Long slotId);

}
