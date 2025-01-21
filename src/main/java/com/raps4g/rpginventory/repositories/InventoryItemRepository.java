package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.domain.entities.InventoryItem;

import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends CrudRepository<InventoryItem, Long>, 
                    PagingAndSortingRepository<InventoryItem, Long> {

    void deleteByInventoryId(Long inventoryId);

}
