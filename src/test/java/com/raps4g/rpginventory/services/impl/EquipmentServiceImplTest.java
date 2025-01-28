package com.raps4g.rpginventory.services.impl;

import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raps4g.rpginventory.exceptions.ItemCannotBeEquippedException;
import com.raps4g.rpginventory.model.EquipmentItem;
import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.repositories.EquipmentItemRepository;
import com.raps4g.rpginventory.repositories.InventoryItemRepository;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.util.TestServiceData;

@ExtendWith(MockitoExtension.class)
public class EquipmentServiceImplTest {

    @InjectMocks
    private EquipmentServiceImpl equipmentService;

    @Mock
    private EquipmentItemRepository equipmentItemRepository;
    
    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private InventoryItemRepository inventoryItemRepository;

    private EquipmentItem equipmentItem;
    private InventoryItem inventoryItem;
    private Player player;

    @BeforeEach
    public void setUp() {
        
        equipmentItem = TestServiceData.getEquipmentItem();
        inventoryItem = TestServiceData.getInventoryItem();
        player = TestServiceData.getPlayer();
    }

    @Test
    public void equipmentService_EquipItem_EquipsItem() throws IllegalAccessException {
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();
        Long slotId = inventoryItem.getItem().getValidSlot().getId();
        
        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));
        Mockito.when(equipmentItemRepository.findBySlotId(slotId)).thenReturn(Optional.empty());
        Mockito.when(equipmentItemRepository.save(Mockito.any(EquipmentItem.class))).thenAnswer(invocation -> {
            EquipmentItem equipmentItem = invocation.getArgument(0);
            equipmentItem.setId(1L);
            return equipmentItem;
        });
        
        EquipmentItem result = equipmentService.equipItem(playerId, inventoryItemId);

        Assertions.assertThat(result).isEqualTo(equipmentItem);

    }
   
    @Test
    public void equipmentService_EquipItem_ThrowsIllegalAccesException() throws IllegalAccessException {
        Long playerId = player.getId() + 1;
        Long inventoryItemId = inventoryItem.getId();
        
        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));

        Assertions.assertThatThrownBy(() -> equipmentService.equipItem(playerId, inventoryItemId))
            .isInstanceOf(IllegalAccessException.class);
    }

    @Test
    public void equipmentService_EquipItem_ThrowsItemCannotBeEquippedException_SlotInUse() throws IllegalAccessException {
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();
        Long slotId = inventoryItem.getItem().getValidSlot().getId();
        
        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));
        Mockito.when(equipmentItemRepository.findBySlotId(slotId)).thenReturn(Optional.of(equipmentItem));
        
        Assertions.assertThatThrownBy(() -> equipmentService.equipItem(playerId, inventoryItemId))
            .isInstanceOf(ItemCannotBeEquippedException.class);
    }

    @Test
    public void equipmentService_EquipItem_ThrowsItemCannotBeEquippedException_NotEquipable() throws IllegalAccessException {
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();
      
        inventoryItem.getItem().setValidSlot(null);

        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));
        
        Assertions.assertThatThrownBy(() -> equipmentService.equipItem(playerId, inventoryItemId))
            .isInstanceOf(ItemCannotBeEquippedException.class);
    }

    @Test
    public void equipmentService_UnequipItem_UnequipsItem() throws IllegalAccessException {
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();
        
        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(equipmentItemRepository.findByInventoryItemId(inventoryItemId)).thenReturn(Optional.of(equipmentItem));
       
        equipmentService.unequipItem(playerId, inventoryItemId);

        Mockito.verify(equipmentItemRepository).delete(equipmentItem);
    }
    @Test
    public void equipmentService_UnequipItem_ThrowsIllegalAccessException() throws IllegalAccessException {
        Long playerId = player.getId() + 1;
        Long inventoryItemId = inventoryItem.getId();
        
        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(equipmentItemRepository.findByInventoryItemId(inventoryItemId)).thenReturn(Optional.of(equipmentItem));
    
        Assertions.assertThatThrownBy(() -> equipmentService.unequipItem(playerId, inventoryItemId))
            .isInstanceOf(IllegalAccessException.class);

    }
}
