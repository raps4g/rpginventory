package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.InventoryItem;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryItemRepository extends JpaRepository<InventoryItem, Long> {

    void deleteByInventoryId(Long inventoryId);

    Optional<InventoryItem> findByItemId(Long itemId);

    boolean existsByItemId(Long itemId);

    boolean existsByInventoryId(Long id);

}
