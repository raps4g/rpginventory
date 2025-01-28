package com.raps4g.rpginventory.services.impl;

import java.util.ArrayList;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.hamcrest.core.IsInstanceOf;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.shadow.com.univocity.parsers.common.DataValidationException;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.dao.DataIntegrityViolationException;

import com.raps4g.rpginventory.exceptions.InsufficientGoldException;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.model.Inventory;
import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.model.Item;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.repositories.EquipmentItemRepository;
import com.raps4g.rpginventory.repositories.InventoryItemRepository;
import com.raps4g.rpginventory.repositories.InventoryRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.util.TestServiceData;

@ExtendWith(MockitoExtension.class)
public class InventoryServiceImplTest {
 
    @InjectMocks
    private InventoryServiceImpl inventoryService;

    @Mock 
    private InventoryRepository inventoryRepository;

    @Mock
    private PlayerRepository playerRepository;

    @Mock
    private ItemRepository itemRepository;

    @Mock
    private InventoryItemRepository inventoryItemRepository;
    
    @Mock
    private EquipmentItemRepository equipmentItemRepository;

    private Player player;
    private Item item;
    private Inventory inventory;
    private InventoryItem inventoryItem;

    @BeforeEach
    public void setUp() {

        item = TestServiceData.getSwordItem();
        player = TestServiceData.getPlayer();
        inventory = TestServiceData.getInventory();
        inventoryItem = TestServiceData.getInventoryItem();
    }

    @Test
    public void InventoryServcie_CreatePlayerInventory_Creates() {
        Long playerId = player.getId(); 
        Mockito.when(inventoryRepository.existsByPlayerId(playerId)).thenReturn(false);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(inventoryRepository.save(Mockito.any(Inventory.class))).thenAnswer(invocation -> {
            Inventory inventoryToSave = invocation.getArgument(0);
            inventoryToSave.setId(1L);
            inventoryToSave.setInventoryItems(new ArrayList<>());
            return inventoryToSave;
        });
    
        Inventory result = inventoryService.createPlayerInventory(playerId);

        ArgumentCaptor<Inventory> inventoryCaptor = ArgumentCaptor.forClass(Inventory.class);
        Mockito.verify(inventoryRepository).save(inventoryCaptor.capture());
        Inventory capturedInventory = inventoryCaptor.getValue();

        Assertions.assertThat(result).isEqualTo(capturedInventory);
        Assertions.assertThat(capturedInventory).isEqualTo(inventory);
    }

    @Test
    public void InventoryServcie_CreatePlayerInventory_ThrowsResourceAlreadyExistsException() {
        Long playerId = player.getId(); 
        Mockito.when(inventoryRepository.existsByPlayerId(playerId)).thenReturn(true);
    
        Assertions.assertThatThrownBy(() -> inventoryService.createPlayerInventory(playerId))
            .isInstanceOf(ResourceAlreadyExistsException.class);

    }
    
    @Test
    public void InventoryServcie_CreatePlayerInventory_ThrowsResourceNotFoundException() {
        Long playerId = player.getId(); 
        Mockito.when(inventoryRepository.existsByPlayerId(playerId)).thenReturn(false);
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
    
        Assertions.assertThatThrownBy(() -> inventoryService.createPlayerInventory(playerId))
            .isInstanceOf(ResourceNotFoundException.class);

    }


    @Test
    public void InventoryService_BuyItem_BuysItem() {
        Long playerId = player.getId();
        Long itemId = item.getId();
        
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(inventoryRepository.findByPlayerId(playerId)).thenReturn(Optional.of(inventory));
        
        Mockito.when(inventoryItemRepository.save(Mockito.any(InventoryItem.class))).thenAnswer(invocation -> {
            InventoryItem inventoryItemToSave = invocation.getArgument(0);
            inventoryItemToSave.setId(1L);
            return inventoryItemToSave;
        });

       
        InventoryItem result = inventoryService.buyItem(playerId, itemId);
        
        ArgumentCaptor<InventoryItem> inventoryItemCaptor = ArgumentCaptor.forClass(InventoryItem.class);
        Mockito.verify(inventoryItemRepository).save(inventoryItemCaptor.capture());
        InventoryItem capturedInventoryItem = inventoryItemCaptor.getValue();

        Assertions.assertThat(player.getGold()).isEqualTo(900);
        Assertions.assertThat(capturedInventoryItem).isEqualTo(result);
        Assertions.assertThat(capturedInventoryItem.getInventory()).isEqualTo(inventory);
        Assertions.assertThat(capturedInventoryItem.getItem()).isEqualTo(item);

        Mockito.verify(playerRepository).save(player);
    }
    
    @Test
        public void InventoryService_BuyItem_ThrowsInsufficientGoldException() {
        Long playerId = player.getId();
        Long itemId = item.getId();
       
        player.setGold(0L);

        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        
        Assertions.assertThatThrownBy(() -> inventoryService.buyItem(playerId, itemId))
            .isInstanceOf(InsufficientGoldException.class);
        
    }
    
    @Test
    public void InventoryService_BuyItem_ThrowsResourceNotFoundExceptionPlayer() {
        Long playerId = player.getId();
        Long itemId = item.getId();
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.empty());
    
        Assertions.assertThatThrownBy(() -> inventoryService.buyItem(playerId, itemId))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void InventoryService_BuyItem_ThrowsResourceNotFoundExceptionInventory() {
        Long playerId = player.getId();
        Long itemId = item.getId();
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        Mockito.when(inventoryRepository.findByPlayerId(playerId)).thenReturn(Optional.empty());
    
        Assertions.assertThatThrownBy(() -> inventoryService.buyItem(playerId, itemId))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    @Test
    public void InventoryService_BuyItem_ThrowsResourceNotFoundExceptionItem() {
        Long playerId = player.getId();
        Long itemId = item.getId();
        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.empty());
    
        Assertions.assertThatThrownBy(() -> inventoryService.buyItem(playerId, itemId))
            .isInstanceOf(ResourceNotFoundException.class);
    }

    
    @Test
    public void InventoryService_AddItemToInventory_AddsItem() {
        Long playerId = player.getId();
        Long itemId = item.getId();
        
        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryRepository.findByPlayerId(playerId)).thenReturn(Optional.of(inventory));
        Mockito.when(itemRepository.findById(itemId)).thenReturn(Optional.of(item));
        
        Mockito.when(inventoryItemRepository.save(Mockito.any(InventoryItem.class))).thenAnswer(invocation -> {
            InventoryItem inventoryItemToSave = invocation.getArgument(0);
            inventoryItemToSave.setId(1L);
            return inventoryItemToSave;
        });

        InventoryItem result = inventoryService.addItemToInventory(playerId, itemId);

        Assertions.assertThat(inventory.getInventoryItems()).contains(result);

        ArgumentCaptor<InventoryItem> inventoryItemCaptor = ArgumentCaptor.forClass(InventoryItem.class);
        Mockito.verify(inventoryItemRepository).save(inventoryItemCaptor.capture());
        InventoryItem capturedInventoryItem = inventoryItemCaptor.getValue();

        Assertions.assertThat(capturedInventoryItem.getInventory()).isEqualTo(inventory);
        Assertions.assertThat(capturedInventoryItem.getItem()).isEqualTo(item);
    }


    @Test
    public void InventoryService_RemoveItemFromInventory_RemovesItem() throws IllegalAccessException {
        
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));
        Mockito.when(equipmentItemRepository.existsByInventoryItemId(inventoryItemId)).thenReturn(false);

        inventoryService.removeItemFromInventory(playerId, inventoryItemId);

        Mockito.verify(inventoryItemRepository).delete(inventoryItem);

    }

    @Test
    public void InventoryService_RemoveItemFromInventory_ThrowsResourceNotFoundExceptionPlayer() throws IllegalAccessException {
        
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.existsById(playerId)).thenReturn(false);

        Assertions.assertThatThrownBy(() -> inventoryService.removeItemFromInventory(playerId, inventoryItemId))
            .isInstanceOf(ResourceNotFoundException.class);

    }
    
    @Test
    public void InventoryService_RemoveItemFromInventory_ThrowsResourceNotFoundExceptionInventoryItem() throws IllegalAccessException {

        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> inventoryService.removeItemFromInventory(playerId, inventoryItemId))
            .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    public void InventoryService_RemoveItemFromInventory_ThrowsDataIntegrityViolationException() throws IllegalAccessException {

        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));
        Mockito.when(equipmentItemRepository.existsByInventoryItemId(inventoryItemId)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> inventoryService.removeItemFromInventory(playerId, inventoryItemId))
            .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    public void InventoryService_RemoveItemFromInventory_ThrowsIllegalAccessException() throws IllegalAccessException {

        Long playerId = player.getId() + 1;
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.existsById(playerId)).thenReturn(true);
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));

        Assertions.assertThatThrownBy(() -> inventoryService.removeItemFromInventory(playerId, inventoryItemId))
            .isInstanceOf(IllegalAccessException.class);

    }


    @Test
    public void InventoryService_SellItem_SellsItem() throws IllegalAccessException {
        
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();
        Long playerGold = player.getGold();

        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));
        Mockito.when(equipmentItemRepository.existsByInventoryItemId(inventoryItemId)).thenReturn(false);

        Player result = inventoryService.sellItem(playerId, inventoryItemId);

        Mockito.verify(playerRepository).save(player);
        Mockito.verify(inventoryItemRepository).delete(inventoryItem);

        Assertions.assertThat(result.getId()).isEqualTo(player.getId());
        Assertions.assertThat(result.getGold()).isEqualTo(playerGold + inventoryItem.getItem().getValue());
    }

    @Test
    public void InventoryService_SellItem_ThrowsResourceNotFoundExceptionPlayer() throws IllegalAccessException {
        
        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> inventoryService.sellItem(playerId, inventoryItemId))
            .isInstanceOf(ResourceNotFoundException.class);

    }
    
    @Test
    public void InventoryService_SellItem_ThrowsResourceNotFoundExceptionInventoryItem() throws IllegalAccessException {

        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> inventoryService.sellItem(playerId, inventoryItemId))
            .isInstanceOf(ResourceNotFoundException.class);

    }

    @Test
    public void InventoryService_SellItem_ThrowsDataIntegrityViolationException() throws IllegalAccessException {

        Long playerId = player.getId();
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));
        Mockito.when(equipmentItemRepository.existsByInventoryItemId(inventoryItemId)).thenReturn(true);

        Assertions.assertThatThrownBy(() -> inventoryService.sellItem(playerId, inventoryItemId))
            .isInstanceOf(DataIntegrityViolationException.class);

    }

    @Test
    public void InventoryService_SellItem_ThrowsIllegalAccessException() throws IllegalAccessException {

        Long playerId = player.getId() + 1;
        Long inventoryItemId = inventoryItem.getId();

        Mockito.when(playerRepository.findById(playerId)).thenReturn(Optional.of(player));
        Mockito.when(inventoryItemRepository.findById(inventoryItemId)).thenReturn(Optional.of(inventoryItem));

        Assertions.assertThatThrownBy(() -> inventoryService.sellItem(playerId, inventoryItemId))
            .isInstanceOf(IllegalAccessException.class);

    }

}
