package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.domain.entities.Item;

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

    Page<Item> findByItemCategoryId(Long categoryId, Pageable pageable);

    Page<Item> findByItemCategoryIdAndItemRarityId(Long categoryId, Long rarityId, Pageable pageable);

    Page<Item> findByItemRarityId(Long rarityId, Pageable pageable);

}
