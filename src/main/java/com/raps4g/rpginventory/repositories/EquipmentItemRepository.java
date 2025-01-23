package com.raps4g.rpginventory.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;

import com.raps4g.rpginventory.domain.entities.EquipmentItem;

@Repository
public interface EquipmentItemRepository extends CrudRepository<EquipmentItem, Long>, 
                    PagingAndSortingRepository<EquipmentItem, Long> {

    List<EquipmentItem> findAllByPlayerId(Long playerId);

    Optional<EquipmentItem> findByInventoryItemId(Long inventoryItemId);

    Optional<EquipmentItem> findBySlotId(Long id);

    boolean existsByInventoryItemId(Long inventoryItemId);

    boolean existsByPlayerId(Long playerId);

}
