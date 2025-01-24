package com.raps4g.rpginventory.repositories;

import com.raps4g.rpginventory.model.Inventory;
import com.raps4g.rpginventory.model.Player;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InventoryRepository extends JpaRepository<Inventory, Long> {

    Optional<Inventory> findByPlayer(Player player);

    Optional<Inventory> findByPlayerId(Long playerId);

    boolean existsByPlayerId(Long playerId);

}
