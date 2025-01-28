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

import com.raps4g.rpginventory.model.Slot;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class SlotRepositoryTest {

    @Autowired
    private SlotRepository slotRepository;

    @Autowired
    private TestRepositoriesData testData;

    private Slot slot;

    @BeforeEach
    private void setUp() {
        testData.reset();
        slot = testData.getRightHandSlot();
    }
    
    @Test
    void SlotRepository_FindByName_Finds() {

        Slot savedSlot = slotRepository.save(slot);

        Optional<Slot> foundSlot = slotRepository.findByName("Right hand");

        Assertions.assertThat(foundSlot).isNotEmpty();
        Assertions.assertThat(foundSlot.get()).isEqualTo(savedSlot);

    }

    @Test
    void SlotRepository_FindByName_DoesNotFind() {
        
        slotRepository.save(slot);

        Optional<Slot> foundSlot = slotRepository.findByName("Left hand");
        Assertions.assertThat(foundSlot).isEmpty();
    }

    @Test
    void SlotRepository_ExistsByName_ReturnsTrue() {

        slotRepository.save(slot);

        Boolean result = slotRepository.existsByName("Right hand");
        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void SlotRepositoryExistsByNameFalse() {

        slotRepository.save(slot);

        Boolean result = slotRepository.existsByName("Left hand");
        Assertions.assertThat(result).isFalse();
    }
    
    
}
