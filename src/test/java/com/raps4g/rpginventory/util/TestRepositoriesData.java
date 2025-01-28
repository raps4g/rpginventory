package com.raps4g.rpginventory.util;

import com.raps4g.rpginventory.util.TestRepositoriesData;
import java.util.ArrayList;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import com.raps4g.rpginventory.model.EquipmentItem;
import com.raps4g.rpginventory.model.Inventory;
import com.raps4g.rpginventory.model.InventoryItem;
import com.raps4g.rpginventory.model.Item;
import com.raps4g.rpginventory.model.ItemCategory;
import com.raps4g.rpginventory.model.ItemRarity;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.model.Role;
import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.repositories.EquipmentItemRepository;
import com.raps4g.rpginventory.repositories.InventoryItemRepository;
import com.raps4g.rpginventory.repositories.InventoryRepository;
import com.raps4g.rpginventory.repositories.ItemCategoryRepository;
import com.raps4g.rpginventory.repositories.ItemRarityRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.repositories.RoleRepository;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.repositories.UserRepository;

@Component
public class TestRepositoriesData {
   
    @Autowired
    private ItemCategoryRepository itemCategoryRepository;
    @Autowired
    private ItemRarityRepository itemRarityRepository;
    @Autowired
    private SlotRepository slotRepository;
    @Autowired
    private ItemRepository itemRepository;
    @Autowired
    private RoleRepository roleRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private PlayerRepository playerRepository;
    @Autowired
    private InventoryRepository inventoryRepository;
    @Autowired
    private InventoryItemRepository inventoryItemRepository;
    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    private ItemCategory savedWeaponCategory;
    private ItemRarity savedCommonRarity;
    private Slot savedRightHandSlot;
    private Item savedSwordItem;
    private Item savedAxeItem;
    private Role savedUserRole;
    private User savedUser;
    private Player savedPlayer;
    private Inventory savedInventory;
    private InventoryItem savedSwordInventoryItem;
    private InventoryItem savedAxeInventoryItem;
    private EquipmentItem savedEquipmentItem;


    public void reset() {
        equipmentItemRepository.deleteAll();
        inventoryItemRepository.deleteAll();
        inventoryRepository.deleteAll();
        playerRepository.deleteAll();
        userRepository.deleteAll();
        roleRepository.deleteAll();
        itemRepository.deleteAll();
        slotRepository.deleteAll();
        itemRarityRepository.deleteAll();
        itemCategoryRepository.deleteAll();

        savedWeaponCategory = null;
        savedCommonRarity = null;
        savedRightHandSlot = null;
        savedSwordItem = null;
        savedAxeItem = null;
        savedUserRole = null;
        savedUser = null;
        savedPlayer = null;
        savedInventory = null;
        savedSwordInventoryItem = null;
        savedAxeInventoryItem = null;
        savedEquipmentItem = null;
    }

    public ItemCategory getWeaponCategory () {
        return ItemCategory.builder().name("Weapon").build();
    }

    public ItemRarity getCommonRarity() {
        return ItemRarity.builder().name("Common").build();
    }

    public Slot getRightHandSlot() {
        return Slot.builder().name("Right hand").build();
    }

    public ItemCategory getSavedWeaponCategory() {
        if (savedWeaponCategory == null) {
            savedWeaponCategory = itemCategoryRepository.save(getWeaponCategory());
        }
        return savedWeaponCategory;
    }

    public ItemRarity getSavedCommonRarity() {
        if (savedCommonRarity == null) {
            savedCommonRarity = itemRarityRepository.save(getCommonRarity());
        }
        return savedCommonRarity;
    }

    public Slot getSavedRightHandSlot() {
        if (savedRightHandSlot == null) {
            savedRightHandSlot = slotRepository.save(getRightHandSlot());
        }
        return savedRightHandSlot;
    }

    public Item getSwordItem() {
        return Item.builder()
            .name("Sword")
            .description("Just a sword.")
            .itemCategory(getSavedWeaponCategory())
            .itemRarity(getSavedCommonRarity())
            .validSlot(getSavedRightHandSlot())
            .value(100)
            .build();
    }
    
    public Item getSavedSwordItem() {
        if (savedSwordItem == null) {
            savedSwordItem = itemRepository.save(getSwordItem());
        }
        return savedSwordItem;
    }

    public Item getAxeItem() {
        return Item.builder()
            .name("Axe")
            .description("Just an axe.")
            .itemCategory(getSavedWeaponCategory())
            .itemRarity(getSavedCommonRarity())
            .validSlot(getSavedRightHandSlot())
            .value(100)
            .build();
    }

    public Item getSavedAxeItem() {
        if (savedAxeItem == null) {
            savedAxeItem = itemRepository.save(getAxeItem());
        }
        return savedAxeItem;
    }

    public Role getUserRole() {
        return Role.builder().name("USER").build();
    }

    public Role getSavedUserRole() {
        if (savedUserRole == null) {
            savedUserRole = roleRepository.save(getUserRole());
        }
        return savedUserRole;
    }

    public User getUser() {
        return User.builder()
            .username("coolusername")
            .password("secretpassword")    
            .roles(Set.of(getSavedUserRole()))
            .build();
    }

    public User getSavedUser() {
        if (savedUser == null) {
            savedUser = userRepository.save(getUser());
        }
        return savedUser;
    }

    public Player getPlayer() {
        return Player.builder()
            .name("player")
            .gold(1000L)
            .user(getSavedUser())
            .build();
    }

    public Player getSavedPlayer() {
        if (savedPlayer == null) {
            savedPlayer = playerRepository.save(getPlayer());
        }
        return savedPlayer;
    }

    public Inventory getInventory() {
        return Inventory.builder()
            .player(getSavedPlayer())
            .inventoryItems(new ArrayList<>())
            .build();
    }

    public Inventory getSavedInventory() {
        if (savedInventory == null) {
            savedInventory = inventoryRepository.save(getInventory());
        }
        return savedInventory;
    }

    public InventoryItem getSwordInventoryItem() {
        return InventoryItem.builder()
            .inventory(getSavedInventory())
            .item(getSavedSwordItem())
            .build();
    }

    public InventoryItem getSavedSwordInventoryItem() {
        if (savedSwordInventoryItem == null) {
            savedSwordInventoryItem = inventoryItemRepository.save(getSwordInventoryItem());
        }
        return savedSwordInventoryItem;
    }
    public InventoryItem getAxeInventoryItem() {
        return InventoryItem.builder()
            .inventory(getSavedInventory())
            .item(getSavedAxeItem())
            .build();
    }

    public InventoryItem getSavedAxeInventoryItem() {
        if (savedAxeInventoryItem == null) {
            savedAxeInventoryItem = inventoryItemRepository.save(getAxeInventoryItem());
        }
        return savedAxeInventoryItem;
    }

    public EquipmentItem getSwordEquipmentItem() {
        return EquipmentItem.builder()
        .inventoryItem(getSavedSwordInventoryItem())
        .player(getSavedPlayer())
        .slot(getSavedSwordItem().getValidSlot())
        .build();
    }
    
    public EquipmentItem getAxeEquipmentItem() {
        return EquipmentItem.builder()
        .inventoryItem(getSavedAxeInventoryItem())
        .player(getSavedPlayer())
        .slot(getSavedAxeItem().getValidSlot())
        .build();
    }

}
