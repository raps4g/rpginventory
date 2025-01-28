package com.raps4g.rpginventory.util;

import java.util.ArrayList;
import java.util.Set;

import org.assertj.core.internal.Arrays;

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

public class TestServiceData {

    public static Player getUnsavedPlayer() {
        return Player.builder()
            .name("player")
            .gold(1000L)
            .build();
    }

    public static Player getPlayer() {
        Player player = getUnsavedPlayer();
        player.setId(1L);
        return player;
    }
    
    public static User getUnsavedUser() {
        return User.builder()
            .username("coolusername")
            .password("plainpassword")
            .build();
    }

    public static User getUser() {
        return User.builder()
            .id(1L)
            .username("coolusername")
            .password("encodedpassword")
            .roles(Set.of(getUserRole()))
            .build();
    }

    public static Role getUserRole() {
        return Role.builder().id(1L).name("USER").build();
    }
   
    public static ItemCategory getUnsavedWeaponCategory() {
        return ItemCategory.builder().name("Weapon").build();
    }

    public static ItemCategory getWeaponCategory() {
        ItemCategory itemCategory = getUnsavedWeaponCategory();
        itemCategory.setId(1L);
        return itemCategory;
    }
    
    public static ItemRarity getUnsavedCommonRarity() {
        return ItemRarity.builder().name("Common").build();
    }

    public static ItemRarity getCommonRarity() {
        ItemRarity itemRarity = getUnsavedCommonRarity();
        itemRarity.setId(1L);
        return itemRarity;
    }

    public static Slot getUnsavedRightHandSlot() {
        return Slot.builder().name("Right hand").build();
    }

    public static Slot getRightHandSlot() {
        Slot slot = getUnsavedRightHandSlot();
        slot.setId(1L);
        return slot;
    }

    public static Item getUnsavedSwordItem() {
        return Item.builder()
            .name("Sword")
            .description("Just a sword")
            .itemCategory(getWeaponCategory())
            .itemRarity(getCommonRarity())
            .validSlot(getRightHandSlot())
            .value(100)
            .build();
    }

    public static Item getSwordItem() {
        Item item = getUnsavedSwordItem();
        item.setId(1L);
        return item;
    }


    public static Inventory getInventory() {
        return Inventory.builder()
            .id(1L)
            .player(getPlayer())
            .inventoryItems(new ArrayList<>())
            .build();
    }
    
    public static InventoryItem getUnsavedInventoryItem() {
        return InventoryItem.builder()
            .inventory(getInventory())
            .item(getSwordItem())
            .build();
    } 

    public static InventoryItem getInventoryItem() {
        return InventoryItem.builder()
        .id(1L)
        .inventory(getInventory())
        .item(getSwordItem())
        .build();
    }

    public static EquipmentItem getEquipmentItem() {
        return EquipmentItem.builder()
            .id(1L)
            .inventoryItem(getInventoryItem())
            .player(getPlayer())
            .slot(getRightHandSlot())
            .build();
    }
}
