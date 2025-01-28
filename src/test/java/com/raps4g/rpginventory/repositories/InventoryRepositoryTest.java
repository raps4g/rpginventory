package com.raps4g.rpginventory.repositories;

import java.util.ArrayList;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.raps4g.rpginventory.model.Inventory;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class InventoryRepositoryTest {

    @Autowired
    private InventoryRepository inventoryRepository;
    
    @Autowired
    private TestRepositoriesData testData;
    
    private Inventory inventory;
    
    @BeforeEach
    private void setUp() {
        testData.reset();
        inventory = testData.getInventory();
    }
   
    @Test
    void InventoryRepository_FindByPlayerId_Finds() {

        Inventory savedInventory = inventoryRepository.save(inventory);
        Player savedPlayer = savedInventory.getPlayer();

        Optional<Inventory> foundInventory = inventoryRepository.findByPlayerId(savedPlayer.getId());

        Assertions.assertThat(foundInventory).isNotEmpty();
        Assertions.assertThat(foundInventory.get()).isEqualTo(savedInventory);

    }

    @Test
    void InventoryRepository_FindByPlayerId_DoesNotFind() {

        Inventory savedInventory = inventoryRepository.save(inventory);
        Player savedPlayer = savedInventory.getPlayer();

        Optional<Inventory> foundInventory = inventoryRepository.findByPlayerId(savedPlayer.getId() + 1);
        Assertions.assertThat(foundInventory).isEmpty();
    }

    @Test
    void InventoryRepository_ExistsByPlayerId_ReturnsTrue() {

        Inventory savedInventory = inventoryRepository.save(inventory);
        Player savedPlayer = savedInventory.getPlayer();

        Boolean result = inventoryRepository.existsByPlayerId(savedPlayer.getId());
        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void InventoryRepositoryExistsByNameFalse() {

        Inventory savedInventory = inventoryRepository.save(inventory);
        Player savedPlayer = savedInventory.getPlayer();

        Boolean result = inventoryRepository.existsByPlayerId(savedPlayer.getId() + 1);
        Assertions.assertThat(result).isFalse();
    }
    
}
