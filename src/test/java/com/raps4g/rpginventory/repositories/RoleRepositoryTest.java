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

import com.raps4g.rpginventory.model.Role;
import com.raps4g.rpginventory.util.TestRepositoriesData;

@DataJpaTest
@AutoConfigureTestDatabase(connection = EmbeddedDatabaseConnection.H2)
@Import(TestRepositoriesData.class)
public class RoleRepositoryTest {

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private TestRepositoriesData testData;

    private Role role;

    @BeforeEach
    private void setUp() {
        testData.reset();
        role = testData.getUserRole();
    }
    
    @Test
    void RoleRepository_FindByRolename_Finds() {
        
        Role savedRole = roleRepository.save(role);

        Optional<Role> foundRole = roleRepository.findByName("USER");

        Assertions.assertThat(foundRole).isNotEmpty();
        Assertions.assertThat(foundRole.get()).isEqualTo(savedRole);

    }

    @Test
    void RoleRepository_FindByRolename_DoesNotFind() {

        Optional<Role> foundRole = roleRepository.findByName("USERR");
        Assertions.assertThat(foundRole).isEmpty();
    }

}
