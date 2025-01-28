package com.raps4g.rpginventory.services.impl;

import java.util.Optional;
import java.util.Set;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

import com.raps4g.rpginventory.model.Role;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.repositories.UserRepository;
import com.raps4g.rpginventory.util.TestServiceData;

@ExtendWith(MockitoExtension.class)
public class UserDetailsServiceImplTest {

    @InjectMocks
    private UserDetailsServiceImpl userDetailsService;

    @Mock
    private UserRepository userRepository;

    private User savedUser;

    @BeforeEach
    public void setUp() {
        savedUser = TestServiceData.getUser();
    }

    @Test
    public void userDetailsService_LoadUserByUsername_ReturnsUser() {
        String username = savedUser.getUsername();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.of(savedUser));

        UserDetails userDetails = userDetailsService.loadUserByUsername(username);

        Assertions.assertThat(userDetails.getUsername()).isEqualTo(savedUser.getUsername());
        Assertions.assertThat(userDetails.getPassword()).isEqualTo(savedUser.getPassword());
        Assertions.assertThat(userDetails.getAuthorities())
                .extracting(GrantedAuthority::getAuthority)
                .containsExactlyInAnyOrder("ROLE_USER");
    }
    @Test
    public void userDetailsService_LoadUserByUsername_ThrowsUsernameNotFoundException() {
        String username = savedUser.getUsername();

        Mockito.when(userRepository.findByUsername(username)).thenReturn(Optional.empty());

        Assertions.assertThatThrownBy(() -> userDetailsService.loadUserByUsername(username))
            .isInstanceOf(UsernameNotFoundException.class);
    }}

