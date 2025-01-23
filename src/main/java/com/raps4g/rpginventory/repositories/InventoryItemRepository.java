package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.model.ItemCategory;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long>, 
                    PagingAndSortingRepository<InventoryItem, Long> {

    void deleteByInventoryId(Long inventoryId);

    Optional<InventoryItem> findByItemId(Long itemId);

    boolean existsByItemId(Long itemId);

    List<InventoryItem> findAllByItemId(Long itemId);

    boolean existsByInventoryId(Long id);

}
