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
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.raps4g.rpginventory.model.Item;
import com.raps4g.rpginventory.model.ItemCategory;
import com.raps4g.rpginventory.model.ItemRarity;
import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class ItemRepositoryTest {

    @Autowired
    private ItemRepository itemRepository;

    @Autowired   
    private TestRepositoriesData testData;
    
    private Item item1;
    private Item item2;
    
    @BeforeEach
    private void setUp() {
        testData.reset();
        item1 = testData.getSwordItem();
        item2 = testData.getAxeItem();
    }

    @Test
    void ItemRepository_Save_CreatesNewItem() {
        
        Item savedItem = itemRepository.save(item1);
        
        Assertions.assertThat(savedItem).isNotNull();
        Assertions.assertThat(savedItem.getId()).isNotNull();
        Assertions.assertThat(savedItem)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(item1);
    }

    @Test
    void ItemRepository_Save_NameNullException() {

        item1.setName(null);
        
        Assertions.assertThatThrownBy(() -> itemRepository.saveAndFlush(item1))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
    
    @Test
    void ItemRepository_Save_ItemCategoryNullException() {

        item1.setItemCategory(null);
        
        Assertions.assertThatThrownBy(() -> itemRepository.saveAndFlush(item1))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
    
    @Test
    void ItemRepository_Save_ItemRarityNullException() {

        item1.setItemRarity(null);
        
        Assertions.assertThatThrownBy(() -> itemRepository.saveAndFlush(item1))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
    
    @Test
    void ItemRepository_Save_ValueNullException() {

        item1.setValue(null);
        
        Assertions.assertThatThrownBy(() -> itemRepository.saveAndFlush(item1))
            .isInstanceOf(DataIntegrityViolationException.class);
    }
   
    @Test
    void ItemRepository_Save_UpdatesItem() {

        Item savedItem = itemRepository.save(item1);
        savedItem.setName("Long sword");
        savedItem.setValue(100);
        
        Item updatedItem = itemRepository.save(savedItem);
        
        Assertions.assertThat(updatedItem).isNotNull().isEqualTo(savedItem);
    }

    @Test
    void ItemRepository_FindByName_Finds() {

        Item savedItem = itemRepository.save(item1);
        
        Optional<Item> foundItem = itemRepository.findByName("Sword");

        Assertions.assertThat(foundItem.get()).isEqualTo(savedItem);
    }
    
    @Test
    void ItemRepository_FindByName_DoesNotFind() {

        Item savedItem = itemRepository.save(item1);
        
        Optional<Item> foundItem = itemRepository.findByName("sword");

        Assertions.assertThat(foundItem).isEmpty();
    }
    
    @Test
    void ItemRepository_FindByCategoryId_Finds() {

        Item savedItem = itemRepository.save(item1);
        Long itemCategoryId = savedItem.getItemCategory().getId();
        Item savedItem2 = itemRepository.save(item2);
    
        Page<Item> foundItems = itemRepository.findByItemCategoryId(itemCategoryId, Pageable.unpaged());

        Assertions.assertThat(foundItems.getContent()).containsExactlyInAnyOrder(savedItem, savedItem2);
    }
    
    @Test
    void ItemRepository_FindByCategoryId_DoesNotFind() {

        Item savedItem = itemRepository.save(item1);
        Long itemCategoryId = savedItem.getItemCategory().getId();
    
        Page<Item> foundItems = itemRepository.findByItemCategoryId(itemCategoryId + 1, Pageable.unpaged());

        Assertions.assertThat(foundItems).isEmpty();
    }

    @Test
    void ItemRepository_FindByRarityId_Finds() {

        Item savedItem = itemRepository.save(item1);
        Item savedItem2 = itemRepository.save(item2);
        Long itemRarityId = savedItem.getItemRarity().getId();

        Page<Item> foundItems = itemRepository.findByItemRarityId(itemRarityId, Pageable.unpaged());

        Assertions.assertThat(foundItems.getContent()).containsExactlyInAnyOrder(savedItem, savedItem2);
    }
    
    @Test
    void ItemRepository_FindByRarityId_DoesNotFind() {

        Item savedItem = itemRepository.save(item1);
        Long itemRarityId = savedItem.getItemRarity().getId();

        Page<Item> foundItems = itemRepository.findByItemRarityId(itemRarityId + 1, Pageable.unpaged());

        Assertions.assertThat(foundItems).isEmpty();
    }

    @Test
    void ItemRepositery_FindByCategoryIdAndRarityId_Finds () {

        Item savedItem = itemRepository.save(item1);
        Item savedItem2 = itemRepository.save(item2);

        Long itemCategoryId = savedItem.getItemCategory().getId();
        Long itemRarityId = savedItem.getItemRarity().getId();

        Page<Item> foundItems = itemRepository.findByItemCategoryIdAndItemRarityId(itemCategoryId, itemRarityId, Pageable.unpaged());

        Assertions.assertThat(foundItems.getContent()).containsExactlyInAnyOrder(savedItem, savedItem2);

    }
    
    @Test
    void ItemRepositery_FindByCategoryIdAndRarityId_DoesNotFind () {

        Item savedItem = itemRepository.save(item1);

        Long itemCategoryId = savedItem.getItemCategory().getId();
        Long itemRarityId = savedItem.getItemRarity().getId();

        Page<Item> foundItems = itemRepository.findByItemCategoryIdAndItemRarityId(itemCategoryId + 1, itemRarityId, Pageable.unpaged());
        
        Assertions.assertThat(foundItems).isEmpty();
    }
    
    @Test
    void ItemRepository_ExistsByName_True() {

        Item savedItem = itemRepository.save(item1);
        boolean result = itemRepository.existsByName("Sword");

        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void ItemRepository_ExistsByName_False() {

        Item savedItem = itemRepository.save(item1);
        boolean result = itemRepository.existsByName("sword");

        Assertions.assertThat(result).isFalse();
    }

    @Test
    void ItemRepository_ExistsByCategory_True() {

        Item savedItem = itemRepository.save(item1);
        Long itemCategoryId = savedItem.getItemCategory().getId();
        boolean result = itemRepository.existsByItemCategoryId(itemCategoryId);

        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void ItemRepository_ExistsByCategory_False() {

        Item savedItem = itemRepository.save(item1);
        Long itemCategoryId = savedItem.getItemCategory().getId();
        boolean result = itemRepository.existsByItemCategoryId(itemCategoryId + 1);

        Assertions.assertThat(result).isFalse();
    }
    
    @Test
    void ItemRepository_ExistsByRarity_True() {

        Item savedItem = itemRepository.save(item1);
        Long itemRarityId = savedItem.getItemRarity().getId();
        boolean result = itemRepository.existsByItemRarityId(itemRarityId);

        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void ItemRepository_ExistsByRarity_False() {

        Item savedItem = itemRepository.save(item1);
        Long itemRarityId = savedItem.getItemRarity().getId();
        boolean result = itemRepository.existsByItemRarityId(itemRarityId + 1);

        Assertions.assertThat(result).isFalse();
    }

    @Test
    void ItemRepository_ExistsBySlot_True() {

        Item savedItem = itemRepository.save(item1);
        Long slotId = savedItem.getValidSlot().getId();
        boolean result = itemRepository.existsByValidSlotId(slotId);

        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void ItemRepository_ExistsBySlot_False() {

        Item savedItem = itemRepository.save(item1);
        Long slotId = savedItem.getValidSlot().getId();
        boolean result = itemRepository.existsByValidSlotId(slotId + 1);

        Assertions.assertThat(result).isFalse();
    }
}
