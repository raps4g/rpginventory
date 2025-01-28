package com.raps4g.rpginventory.repositories;

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
import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class InventoryItemRepositoryTest {

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private TestRepositoriesData testData;

    private InventoryItem inventoryItem1;
    private InventoryItem inventoryItem2;
    
    @BeforeEach
    private void setUp() {
        testData.reset();
        inventoryItem1 = testData.getSwordInventoryItem();
        inventoryItem2 = testData.getAxeInventoryItem();
    }

    @Test
    public void InventoryItemRepository_FindByItemId_Finds() {
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem1);
        Long itemId = savedInventoryItem.getItem().getId();

        Optional<InventoryItem> foundInventoryItem = inventoryItemRepository.findByItemId(itemId);

        Assertions.assertThat(foundInventoryItem.get()).isEqualTo(savedInventoryItem);
    }

    @Test
    public void InventoryItemRepository_FindByItemId_DoesNotFind() {
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem1);
        Long itemId = savedInventoryItem.getItem().getId();

        Optional<InventoryItem> foundInventoryItem = inventoryItemRepository.findByItemId(itemId + 1);

        Assertions.assertThat(foundInventoryItem).isEmpty();
    }

    @Test
    public void InventoryItemRepository_ExistsByItemId_True() {
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem1);
        Long itemId = savedInventoryItem.getItem().getId();

        boolean result = inventoryItemRepository.existsByItemId(itemId);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void InventoryItemRepository_ExistsByItemId_False() {
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem1);
        Long itemId = savedInventoryItem.getItem().getId();

        boolean result = inventoryItemRepository.existsByItemId(itemId + 1);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void InventoryItemRepository_ExistsByInventoryId_True() {
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem1);
        Long inventoryId = savedInventoryItem.getInventory().getId();

        boolean result = inventoryItemRepository.existsByInventoryId(inventoryId);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void InventoryItemRepository_ExistsByInventoryId_False() {
        
        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem1);
        Long inventoryId = savedInventoryItem.getInventory().getId();

        boolean result = inventoryItemRepository.existsByInventoryId(inventoryId + 1);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void InventoryItemRepository_DeletesByInventoryId() {

        InventoryItem savedInventoryItem = inventoryItemRepository.save(inventoryItem1);
        Long inventoryId = savedInventoryItem.getInventory().getId();

        inventoryItemRepository.deleteByInventoryId(inventoryId);

        boolean result = inventoryItemRepository.existsByInventoryId(inventoryId);

        Assertions.assertThat(result).isFalse();
    }

}
