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

import com.raps4g.rpginventory.model.ItemCategory;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class ItemCategoryRepositoryTest {

    @Autowired
    private ItemCategoryRepository itemCategoryRepository;

    @Autowired
    private TestRepositoriesData testData;

    private ItemCategory itemCategory;

    @BeforeEach
    private void setUp() {
        testData.reset();
        itemCategory = testData.getWeaponCategory();
    }

    @Test
    void ItemCategoryRepository_FindByName_Finds() {

        ItemCategory savedItemCategory = itemCategoryRepository.save(itemCategory);

        Optional<ItemCategory> foundItemCategory = itemCategoryRepository.findByName("Weapon");

        Assertions.assertThat(foundItemCategory).isNotEmpty();
        Assertions.assertThat(foundItemCategory.get()).isEqualTo(savedItemCategory);

    }

    @Test
    void ItemCategoryRepository_FindByName_DoesNotFind() {

        itemCategoryRepository.save(itemCategory);

        Optional<ItemCategory> foundItemCategory = itemCategoryRepository.findByName("weapon");
        Assertions.assertThat(foundItemCategory).isEmpty();
    }

    @Test
    void ItemCategoryRepository_ExistsByName_ReturnsTrue() {

        itemCategoryRepository.save(itemCategory);

        Boolean result = itemCategoryRepository.existsByName("Weapon");
        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void ItemCategoryRepositoryExistsByNameFalse() {

        itemCategoryRepository.save(itemCategory);

        Boolean result = itemCategoryRepository.existsByName("weapon");
        Assertions.assertThat(result).isFalse();
    }

}
