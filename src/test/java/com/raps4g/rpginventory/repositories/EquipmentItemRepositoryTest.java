package com.raps4g.rpginventory.repositories;


import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jdbc.EmbeddedDatabaseConnection;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;

import com.raps4g.rpginventory.model.EquipmentItem;
import com.raps4g.rpginventory.util.TestRepositoriesData;


@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class EquipmentItemRepositoryTest {

    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    private TestRepositoriesData testData;

    private EquipmentItem equipmentItem1;
    private EquipmentItem equipmentItem2;
    
    @BeforeEach
    private void setUp() {
        testData.reset();
        equipmentItem1 = testData.getSwordEquipmentItem();
        equipmentItem2 = testData.getAxeEquipmentItem();
    }

    @Test
    public void EquipmentItemRepository_FindByInventoryItemId_Finds() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long inventoryItemId = savedEquipmentItem.getInventoryItem().getId();

        Optional<EquipmentItem> foundEquipmentItem = equipmentItemRepository.findByInventoryItemId(inventoryItemId);

        Assertions.assertThat(foundEquipmentItem.get()).isEqualTo(savedEquipmentItem);
    }

    @Test
    public void EquipmentItemRepository_FindByInventoryItemId_DoesNotFind() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long inventoryItemId = savedEquipmentItem.getInventoryItem().getId();

        Optional<EquipmentItem> foundEquipmentItem = equipmentItemRepository.findByInventoryItemId(inventoryItemId + 1);

        Assertions.assertThat(foundEquipmentItem).isEmpty();
    }

    @Test
    public void EquipmentItemRepository_FindBySlotId_Finds() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long slotId = savedEquipmentItem.getSlot().getId();

        Optional<EquipmentItem> foundEquipmentItem = equipmentItemRepository.findBySlotId(slotId);

        Assertions.assertThat(foundEquipmentItem.get()).isEqualTo(savedEquipmentItem);
    }

    @Test
    public void EquipmentItemRepository_FindBySlotId_DoesNotFind() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long slotId = savedEquipmentItem.getSlot().getId();

        Optional<EquipmentItem> foundEquipmentItem = equipmentItemRepository.findBySlotId(slotId + 1);

        Assertions.assertThat(foundEquipmentItem).isEmpty();
    }

    @Test
    public void EquipmentItemRepository_ExistsByInventoryItemId_True() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long inventoryItemId = savedEquipmentItem.getInventoryItem().getId();

        boolean result = equipmentItemRepository.existsByInventoryItemId(inventoryItemId);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void EquipmentItemRepository_ExistsByInventoryItemId_False() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long inventoryItemId = savedEquipmentItem.getInventoryItem().getId();

        boolean result = equipmentItemRepository.existsByInventoryItemId(inventoryItemId + 1);

        Assertions.assertThat(result).isFalse();
    }


    @Test
    public void EquipmentItemRepository_ExistsByPlayerId_True() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long playerId = savedEquipmentItem.getPlayer().getId();

        boolean result = equipmentItemRepository.existsByPlayerId(playerId);

        Assertions.assertThat(result).isTrue();
    }

    @Test
    public void EquipmentItemRepository_ExistsByPlayerId_False() {
        
        EquipmentItem savedEquipmentItem = equipmentItemRepository.save(equipmentItem1);
        Long playerId = savedEquipmentItem.getPlayer().getId();

        boolean result = equipmentItemRepository.existsByPlayerId(playerId + 1);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    public void EquipmentItemRepository_FindAllByPlayerId_Finds() {
        
        EquipmentItem savedEquipmentItem1 = equipmentItemRepository.save(equipmentItem1);
        EquipmentItem savedEquipmentItem2 = equipmentItemRepository.save(equipmentItem2);
        Long playerId = savedEquipmentItem1.getPlayer().getId();

        List<EquipmentItem> foundEquipmentItems = equipmentItemRepository.findAllByPlayerId(playerId);

        Assertions.assertThat(foundEquipmentItems).containsExactlyInAnyOrder(savedEquipmentItem1, savedEquipmentItem2);
    }

    @Test
    public void EquipmentItemRepository_FindAllByPlayerId_DoesNotFind() {
        
        Long playerId = equipmentItem1.getPlayer().getId();

        List<EquipmentItem> foundEquipmentItems = equipmentItemRepository.findAllByPlayerId(playerId);

        Assertions.assertThat(foundEquipmentItems).isEmpty();
    }

}
