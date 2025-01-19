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

    }
}
