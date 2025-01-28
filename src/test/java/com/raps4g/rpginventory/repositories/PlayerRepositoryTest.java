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

import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class PlayerRepositoryTest {

    @Autowired
    private PlayerRepository playerRepository;

    @Autowired    
    private TestRepositoriesData testData;

    private Player player;
    
    @BeforeEach
    private void setUp() {
        testData.reset();
        player = testData.getPlayer();
    }
   
    @Test
    void PlayerRepository_FindByName_Finds() {

        Player savedPlayer = playerRepository.save(player);
        Optional<Player> foundPlayer = playerRepository.findByName("player");

        Assertions.assertThat(foundPlayer).isNotEmpty();
        Assertions.assertThat(foundPlayer.get()).isEqualTo(savedPlayer);

    }

    @Test
    void PlayerRepository_FindByName_DoesNotFind() {

        playerRepository.save(player);
        Optional<Player> foundPlayer = playerRepository.findByName("player2");
        Assertions.assertThat(foundPlayer).isEmpty();
    }

    @Test
    void PlayerRepository_ExistsByName_ReturnsTrue() {

        playerRepository.save(player);
        Boolean result = playerRepository.existsByName("player");
        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void PlayerRepositoryExistsByNameFalse() {

        playerRepository.save(player);
        Boolean result = playerRepository.existsByName("player2");
        Assertions.assertThat(result).isFalse();
    }
    
}
