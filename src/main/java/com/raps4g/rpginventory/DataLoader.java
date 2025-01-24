package com.raps4g.rpginventory;

import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raps4g.rpginventory.config.AppConfig;
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
public class DataLoader implements CommandLineRunner {

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private ItemRepository itemRepository;

    @Autowired
    private ItemRarityRepository itemRarityRepository;

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired
    private InventoryRepository inventoryRepository;

    @Autowired
    private InventoryItemRepository inventoryItemRepository;

    @Autowired
    private EquipmentItemRepository equipmentItemRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private AppConfig appConfig;

    @Transactional
    @Override
    public void run(String... args) throws Exception {

        if (!appConfig.getLoadItems()) {
            return;
        }

        ItemCategory weaponCategory = itemCategoryRepository.save(new ItemCategory(null, "weapon"));
        ItemCategory armorCategory = itemCategoryRepository.save(new ItemCategory(null, "armor"));
        ItemCategory accessoryCategory = itemCategoryRepository.save(new ItemCategory(null, "accessory"));
        ItemCategory potionCategory = itemCategoryRepository.save(new ItemCategory(null, "potion"));
        ItemCategory miscCategory = itemCategoryRepository.save(new ItemCategory(null, "misc"));
        ItemCategory consumableCategory = itemCategoryRepository.save(new ItemCategory(null, "consumable"));
        ItemCategory materialsCategory = itemCategoryRepository.save(new ItemCategory(null, "materials"));
        ItemCategory artifactCategory = itemCategoryRepository.save(new ItemCategory(null, "artifact"));
        ItemCategory toolCategory = itemCategoryRepository.save(new ItemCategory(null, "tool"));

        ItemRarity commonRarity = itemRarityRepository.save(new ItemRarity(null, "common"));
        ItemRarity uncommonRarity = itemRarityRepository.save(new ItemRarity(null, "uncommon"));
        ItemRarity rareRarity = itemRarityRepository.save(new ItemRarity(null, "rare"));
        ItemRarity epicRarity = itemRarityRepository.save(new ItemRarity(null, "epic"));
        ItemRarity legendaryRarity = itemRarityRepository.save(new ItemRarity(null, "legendary"));
        ItemRarity mythicRarity = itemRarityRepository.save(new ItemRarity(null, "mythic"));
        ItemRarity cursedRarity = itemRarityRepository.save(new ItemRarity(null, "cursed"));

        Slot dominantHandSlot = slotRepository.save(new Slot(null, "dominant hand"));
        Slot headSlot = slotRepository.save(new Slot(null, "head"));
        Slot nonDominantHandSlot = slotRepository.save(new Slot(null, "non-dominant hand"));
        Slot bodySlot = slotRepository.save(new Slot(null, "body"));
        Slot legsSlot = slotRepository.save(new Slot(null, "legs"));
        Slot feetSlot = slotRepository.save(new Slot(null, "feet"));
        Slot neckSlot = slotRepository.save(new Slot(null, "neck"));
        Slot ringSlot = slotRepository.save(new Slot(null, "ring"));
        Slot waistSlot = slotRepository.save(new Slot(null, "waist"));
        Slot shouldersSlot = slotRepository.save(new Slot(null, "shoulders"));
        Slot backSlot = slotRepository.save(new Slot(null, "back"));
        
        Item ironSword = Item.builder()
        .name("Iron Sword")
        .description("A sturdy sword forged from iron. Reliable for any beginner adventurer.")
        .value(150)
        .itemCategory(weaponCategory)
        .itemRarity(commonRarity)
        .validSlot(dominantHandSlot)
        .build();
        ironSword = itemRepository.save(ironSword);

        Item steelHelmet = Item.builder()
        .name("Steel Helmet")
        .description("A helmet made of steel to protect your head during battle.")
        .value(300)
        .itemCategory(armorCategory)
        .itemRarity(uncommonRarity)
        .validSlot(headSlot)
        .build();
        steelHelmet = itemRepository.save(steelHelmet);

        Item enchantedRing = Item.builder()
        .name("Enchanted Ring")
        .description("A mystical ring imbued with magical powers.")
        .value(1200)
        .itemCategory(accessoryCategory)
        .itemRarity(epicRarity)
        .validSlot(ringSlot)
        .build();
        enchantedRing = itemRepository.save(enchantedRing);

        Item healingPotion = Item.builder()
        .name("Healing Potion")
        .description("Restores a small amount of health when consumed.")
        .value(50)
        .itemCategory(potionCategory)
        .itemRarity(commonRarity)
        .validSlot(null)
        .build();
        healingPotion = itemRepository.save(healingPotion);

        Item leatherTrousers = Item.builder()
        .name("Leather Trousers")
        .description("Basic trousers made from leather. Offers minimal protection.")
        .value(120)
        .itemCategory(armorCategory)
        .itemRarity(commonRarity)
        .validSlot(legsSlot)
        .build();
        leatherTrousers = itemRepository.save(leatherTrousers);

        Item chainmailLeggings = Item.builder()
        .name("Chainmail Leggings")
        .description("Leggings crafted from interlocking metal rings. Provides good protection.")
        .value(450)
        .itemCategory(armorCategory)
        .itemRarity(uncommonRarity)
        .validSlot(legsSlot)
        .build();
        chainmailLeggings = itemRepository.save(chainmailLeggings);

        Item ironShield = Item.builder()
        .name("Iron Shield")
        .description("A sturdy shield made of iron. Reliable for blocking attacks.")
        .value(600)
        .itemCategory(armorCategory)
        .itemRarity(commonRarity)
        .validSlot(nonDominantHandSlot)
        .build();
        ironShield = itemRepository.save(ironShield);

        Item woodenShield = Item.builder()
        .name("Wooden Shield")
        .description("A simple shield made from reinforced wood. Lightweight but fragile.")
        .value(250)
        .itemCategory(armorCategory)
        .itemRarity(commonRarity)
        .validSlot(nonDominantHandSlot)
        .build();
        woodenShield = itemRepository.save(woodenShield);

        Item steelBoots = Item.builder()
        .name("Steel Boots")
        .description("Heavy boots made of steel. Offers great protection for the feet.")
        .value(700)
        .itemCategory(armorCategory)
        .itemRarity(uncommonRarity)
        .validSlot(feetSlot)
        .build();
        steelBoots = itemRepository.save(steelBoots);

        Item ironOre = Item.builder()
        .name("Iron Ore")
        .description("A raw material used for crafting and forging weapons and armor.")
        .value(50)
        .itemCategory(materialsCategory)
        .itemRarity(commonRarity)
        .validSlot(null) // No specific slot
        .build();
        ironOre = itemRepository.save(ironOre);

        Item steelIngot = Item.builder()
        .name("Steel Ingot")
        .description("A refined material forged from iron. Essential for crafting advanced gear.")
        .value(150)
        .itemCategory(materialsCategory)
        .itemRarity(uncommonRarity)
        .validSlot(null) // No specific slot
        .build();
        steelIngot = itemRepository.save(steelIngot);

        Item dragonScale = Item.builder()
        .name("Dragon Scale")
        .description("A rare and valuable scale shed by a dragon. Extremely durable.")
        .value(1000)
        .itemCategory(materialsCategory)
        .itemRarity(legendaryRarity)
        .validSlot(null)
        .build();
        dragonScale = itemRepository.save(dragonScale);

        Item silkThread = Item.builder()
        .name("Silk Thread")
        .description("Fine thread spun from silkworms. Useful for tailoring high-quality clothing.")
        .value(80)
        .itemCategory(materialsCategory)
        .itemRarity(commonRarity)
        .validSlot(null)
        .build();
        silkThread = itemRepository.save(silkThread);

        Item enchantedGem = Item.builder()
        .name("Enchanted Gem")
        .description("A gemstone imbued with magical properties. Used in crafting enchanted items.")
        .value(2000)
        .itemCategory(materialsCategory)
        .itemRarity(epicRarity)
        .validSlot(null)
        .build();
        enchantedGem = itemRepository.save(enchantedGem);

        Item fireScroll = Item.builder()
        .name("Scroll of Fireball")
        .description("A single-use scroll that unleashes a powerful fireball.")
        .value(800)
        .itemCategory(consumableCategory)
        .itemRarity(rareRarity)
        .validSlot(null)
        .build();
        fireScroll = itemRepository.save(fireScroll);

        Item dragonScaleArmor = Item.builder()
        .name("Dragon Scale Armor")
        .description("Armor crafted from the scales of a dragon, offering unmatched protection.")
        .value(10000)
        .itemCategory(armorCategory)
        .itemRarity(legendaryRarity)
        .validSlot(bodySlot)
        .build();
        dragonScaleArmor = itemRepository.save(dragonScaleArmor);

        Item artifactOfEternity = Item.builder()
        .name("Artifact of Eternity")
        .description("An ancient artifact of immense power. Its origin is shrouded in mystery.")
        .value(50000)
        .itemCategory(artifactCategory)
        .itemRarity(mythicRarity)
        .validSlot(null)
        .build();
        artifactOfEternity = itemRepository.save(artifactOfEternity);

        Item minerPickaxe = Item.builder()
        .name("Miner's Pickaxe")
        .description("A trusty pickaxe for mining ores and breaking rocks.")
        .value(250)
        .itemCategory(toolCategory)
        .itemRarity(commonRarity)
        .validSlot(dominantHandSlot)
        .build();
        minerPickaxe = itemRepository.save(minerPickaxe);

        Item wingedBoots = Item.builder()
        .name("Winged Boots")
        .description("Boots that grant the wearer the ability to float and jump higher.")
        .value(2000)
        .itemCategory(armorCategory)
        .itemRarity(rareRarity)
        .validSlot(feetSlot)
        .build();
        wingedBoots = itemRepository.save(wingedBoots);

        Item cursedAmulet = Item.builder()
        .name("Cursed Amulet")
        .description("A sinister amulet that drains the life force of its wearer.")
        .value(0)
        .itemCategory(accessoryCategory)
        .itemRarity(cursedRarity)
        .validSlot(neckSlot)
        .build();
        cursedAmulet = itemRepository.save(cursedAmulet);

        Item leatherBelt = Item.builder()
        .name("Leather Belt")
        .description("A sturdy belt made from high-quality leather.")
        .value(75)
        .itemCategory(armorCategory)
        .itemRarity(commonRarity)
        .validSlot(waistSlot)
        .build();
        leatherBelt = itemRepository.save(leatherBelt);

        Item crystalShoulderPads = Item.builder()
        .name("Crystal Shoulder Pads")
        .description("Beautiful shoulder pads adorned with enchanted crystals.")
        .value(1800)
        .itemCategory(armorCategory)
        .itemRarity(epicRarity)
        .validSlot(shouldersSlot)
        .build();
        crystalShoulderPads = itemRepository.save(crystalShoulderPads);

        Item cloakOfShadows = Item.builder()
        .name("Cloak of Shadows")
        .description("A cloak that allows the wearer to become nearly invisible in the dark.")
        .value(2500)
        .itemCategory(armorCategory)
        .itemRarity(legendaryRarity)
        .validSlot(backSlot)
        .build();
        cloakOfShadows = itemRepository.save(cloakOfShadows);

        Role adminRole = Role.builder().name("ADMIN").build();
        Role userRole = Role.builder().name("USER").build();
        adminRole = roleRepository.save(adminRole);
        userRole = roleRepository.save(userRole);

        User admin = User.builder().username("admin").roles(Set.of(adminRole)).build();
        User genericUser = User.builder().username("user").roles(Set.of(userRole)).build();

        admin = userRepository.save(admin);
        appConfig.ensureAdminPassword();
        admin.setPassword(passwordEncoder.encode(appConfig.getAdminPassword()));
        genericUser = userRepository.save(genericUser);
        genericUser.setPassword(passwordEncoder.encode("test"));

        Player genericPlayer = Player.builder()
        .name("player")
        .gold(9999L)
        .user(genericUser)
        .build();

        genericPlayer = playerRepository.save(genericPlayer);

        Inventory genericInventory = Inventory.builder()
        .player(genericPlayer)
        .build();

        genericInventory = inventoryRepository.save(genericInventory);

        InventoryItem playerSword = InventoryItem.builder()
        .inventory(genericInventory)
        .item(ironSword)
        .build();
        playerSword = inventoryItemRepository.save(playerSword);

        EquipmentItem equipmentItem = EquipmentItem.builder()
        .inventoryItem(playerSword)
        .player(genericPlayer)
        .slot(playerSword.getItem().getValidSlot())
        .build();
        equipmentItem = equipmentItemRepository.save(equipmentItem);
    }
}
