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

import com.raps4g.rpginventory.model.ItemRarity;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class ItemRarityRepositoryTest {

    @Autowired
    private ItemRarityRepository itemRarityRepository;
    
    @Autowired
    private TestRepositoriesData testData;

    private ItemRarity itemRarity;

    @BeforeEach
    private void setUp() {
        testData.reset();
        itemRarity = testData.getCommonRarity();
    }

    @Test
    void ItemRarityRepository_FindByName_Finds() {

        ItemRarity savedItemRarity = itemRarityRepository.save(itemRarity);

        Optional<ItemRarity> foundItemRarity = itemRarityRepository.findByName("Common");

        Assertions.assertThat(foundItemRarity).isNotEmpty();
        Assertions.assertThat(foundItemRarity.get()).isEqualTo(savedItemRarity);

    }

    @Test
    void ItemRarityRepository_FindByName_DoesNotFind() {

        itemRarityRepository.save(itemRarity);

        Optional<ItemRarity> foundItemRarity = itemRarityRepository.findByName("common");
        Assertions.assertThat(foundItemRarity).isEmpty();
    }

    @Test
    void ItemRarityRepository_ExistsByName_ReturnsTrue() {

        itemRarityRepository.save(itemRarity);

        Boolean result = itemRarityRepository.existsByName("Common");
        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void ItemRarityRepositoryExistsByNameFalse() {

        itemRarityRepository.save(itemRarity);

        Boolean result = itemRarityRepository.existsByName("common");
        Assertions.assertThat(result).isFalse();
    }
    
    
}
