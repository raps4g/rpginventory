package com.raps4g.rpginventory.services.impl;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.model.Item;
import com.raps4g.rpginventory.model.ItemCategory;
import com.raps4g.rpginventory.model.ItemRarity;
import com.raps4g.rpginventory.repositories.ItemCategoryRepository;
import com.raps4g.rpginventory.repositories.ItemRarityRepository;
import com.raps4g.rpginventory.repositories.ItemRepository;
import com.raps4g.rpginventory.repositories.SlotRepository;
import com.raps4g.rpginventory.util.TestServiceData;

@ExtendWith(MockitoExtension.class)
public class ItemServiceImplTest {
 
    @InjectMocks
    private ItemServiceImpl itemService;

    @Mock
    private ItemCategoryRepository itemCategoryRepository;

    @Mock
    private ItemRarityRepository itemRarityRepository;

    @Mock
    private SlotRepository slotRepository;

    @Mock 
    private ItemRepository itemRepository;

    private ItemCategory inputItemCategory;
    private ItemCategory savedItemCategory;
    private ItemRarity inputItemRarity;
    private ItemRarity savedItemRarity;
    private Item inputItem;
    private Item savedItem;

    @BeforeEach
    public void setUp() {

        inputItemCategory = TestServiceData.getUnsavedWeaponCategory();
        savedItemCategory = TestServiceData.getWeaponCategory();
        inputItemRarity = TestServiceData.getUnsavedCommonRarity();
        savedItemRarity = TestServiceData.getCommonRarity();
        inputItem = TestServiceData.getUnsavedSwordItem();
        savedItem = TestServiceData.getSwordItem();
    }

    @Test
    public void ItemServcie_SaveItemCategory_Saves() {

        Mockito.when(itemCategoryRepository.existsByName(inputItemCategory.getName())).thenReturn(false);
        Mockito.when(itemCategoryRepository.save(inputItemCategory)).thenReturn(savedItemCategory);

        ItemCategory result = itemService.saveItemCategory(inputItemCategory);

        Assertions.assertThat(result).isEqualTo(savedItemCategory);
    }

    @Test
    public void ItemServcie_SaveItemRarity_Saves() {

        Mockito.when(itemRarityRepository.existsByName(inputItemRarity.getName())).thenReturn(false);
        Mockito.when(itemRarityRepository.save(inputItemRarity)).thenReturn(savedItemRarity);

        ItemRarity result = itemService.saveItemRarity(inputItemRarity);

        Assertions.assertThat(result).isEqualTo(savedItemRarity);
    }

    @Test
    public void ItemServcie_SaveItem_Saves() {
        Mockito.when(itemRepository.existsByName(inputItem.getName())).thenReturn(false);
        Mockito.when(itemRepository.save(inputItem)).thenReturn(savedItem);

        Item result = itemService.saveItem(inputItem);

        Assertions.assertThat(result).isEqualTo(savedItem);

    }

    @Test
    public void ItemServcie_SaveItem_Updates() {
        Mockito.when(itemRepository.existsByName(inputItem.getName())).thenReturn(true);
        Mockito.when(itemRepository.save(savedItem)).thenReturn(savedItem);

        Item result = itemService.saveItem(savedItem);

        Assertions.assertThat(result).isEqualTo(savedItem);
    }

    @Test
    public void ItemServcie_SaveItem_ThrowsException() {

        Mockito.when(itemRepository.existsByName(inputItem.getName())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> itemService.saveItem(inputItem))
            .isInstanceOf(ResourceAlreadyExistsException.class);
    }

}
