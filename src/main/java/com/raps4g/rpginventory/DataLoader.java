package com.raps4g.rpginventory;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import com.raps4g.rpginventory.domain.entities.Item;
import com.raps4g.rpginventory.domain.entities.ItemCategory;
import com.raps4g.rpginventory.domain.entities.ItemRarity;
import com.raps4g.rpginventory.domain.entities.Slot;
import com.raps4g.rpginventory.repositories.ItemCategoryRepository;
import com.raps4g.rpginventory.repositories.ItemRarityRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.SlotRepository;

/**
 * DataLoader
 */
@Component
public class DataLoader implements CommandLineRunner {
    private static final Logger logger = LoggerFactory.getLogger(DataLoader.class);

    private ItemRepository itemRepository;
    private ItemRarityRepository itemRarityRepository;
    private ItemCategoryRepository itemCategoryRepository;
    private SlotRepository slotRepository;

    public DataLoader(ItemRepository itemRepository,
                        ItemCategoryRepository itemCategoryRepository,
                        ItemRarityRepository itemRarityRepository,
                        SlotRepository slotRepository) {
        this.itemRepository = itemRepository;
        this.itemCategoryRepository = itemCategoryRepository;
        this.itemRarityRepository = itemRarityRepository;
        this.slotRepository = slotRepository;
    }

    @Transactional
    @Override
    public void run(String... args) throws Exception {
            ItemCategory weaponCategory = ItemCategory.builder()
                    .id(1L)
                    .name("Weapons")
                    .build();
            itemCategoryRepository.save(weaponCategory);
            ItemCategory armorCategory = ItemCategory.builder()
                    .id(2L)
                    .name("Armor")
                    .build();
            itemCategoryRepository.save(armorCategory);
            ItemCategory potionCategory = ItemCategory.builder()
                    .id(3L)
                    .name("Potions")
                    .build();
            itemCategoryRepository.save(potionCategory);
            ItemCategory materialCategory = ItemCategory.builder()
            .id(4L)
                    .name("Materials")
                    .build();
            itemCategoryRepository.save(materialCategory);
            ItemCategory accessoryCategory = ItemCategory.builder()
                    .id(5L)
                    .name("Accessories")
                    .build();
            itemCategoryRepository.save(accessoryCategory);
            ItemCategory miscCategory = ItemCategory.builder()
                    .id(6L)
                    .name("Miscellaneous")
                    .build();
            itemCategoryRepository.save(miscCategory);

            ItemRarity commonRarity = ItemRarity.builder()
                    .id(1L)
                    .name("Common")
                    .build();
            itemRarityRepository.save(commonRarity);
            ItemRarity uncommonRarity = ItemRarity.builder()
                    .id(2L)
                    .name("Uncommon")
                    .build();
            itemRarityRepository.save(uncommonRarity);
            ItemRarity rareRarity = ItemRarity.builder()
                    .id(3L)
                    .name("Rare")
                    .build();
            itemRarityRepository.save(rareRarity);
            ItemRarity epicRarity = ItemRarity.builder()
                    .id(4L)
                    .name("Epic")
                    .build();
            itemRarityRepository.save(epicRarity);
            ItemRarity legendaryRarity = ItemRarity.builder()
                    .id(5L)
                    .name("Legendary")
                    .build();
            itemRarityRepository.save(legendaryRarity);
            
            Slot leftHandSlot = new Slot(1L, "Left hand");
            slotRepository.save(leftHandSlot);

            Item basicSword = Item.builder()
            .name("Basic sword")
            .description("Just a sword")
            .validSlot(leftHandSlot)
            .itemRarity(commonRarity)
            .itemCategory(weaponCategory)
            .build();
            itemRepository.save(basicSword);

            

            
    }
}
