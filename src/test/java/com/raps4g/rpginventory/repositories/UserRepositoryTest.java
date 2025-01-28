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

import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private TestRepositoriesData testData;

    private User user;

    @BeforeEach
    private void setUp() {
        testData.reset();
        user = testData.getUser();
    }
    
    @Test
    void UserRepository_FindByUsername_Finds() {

        User savedUser = userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("coolusername");

        Assertions.assertThat(foundUser).isNotEmpty();
        Assertions.assertThat(foundUser.get()).isEqualTo(savedUser);

    }

    @Test
    void UserRepository_FindByUsername_DoesNotFind() {
        
        userRepository.save(user);

        Optional<User> foundUser = userRepository.findByUsername("uncoolusername");
        Assertions.assertThat(foundUser).isEmpty();
    }

    @Test
    void UserRepository_ExistsByUsername_ReturnsTrue() {

        userRepository.save(user);
        
        Boolean result = userRepository.existsByUsername("coolusername");
        Assertions.assertThat(result).isTrue();
    }
    
    @Test
    void UserRepositoryExistsByUsernameFalse() {

        userRepository.save(user);
        
        Boolean result = userRepository.existsByUsername("uncoolusername");
        Assertions.assertThat(result).isFalse();
    }
    
    
}
