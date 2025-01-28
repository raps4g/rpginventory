package com.raps4g.rpginventory.services.impl;

import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.model.Role;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.repositories.RoleRepository;
import com.raps4g.rpginventory.repositories.UserRepository;
import com.raps4g.rpginventory.services.JwtService;
import com.raps4g.rpginventory.util.TestServiceData;

@ExtendWith(MockitoExtension.class)
public class UserServiceImplTest {
    
    @InjectMocks
    private UserServiceImpl userService;

    @Mock
    private UserRepository userRepository;

    @Mock
    private RoleRepository roleRepository;

    @Mock
    private PasswordEncoder passwordEncoder;
    
    @Mock
    private AuthenticationManager authManager;

    @Mock
    private JwtService jwtService;

    @Mock
    private Authentication authentication;

    private User inputUser;
    private User savedUser;
    private Role savedRole;


    @BeforeEach
    public void setUp() {
        inputUser = TestServiceData.getUnsavedUser();
        savedUser = TestServiceData.getUser();
        savedRole = TestServiceData.getUserRole();
    }


    @Test
    public void UserService_Register_CreatesUser() {

        Mockito.when(userRepository.existsByUsername(inputUser.getUsername())).thenReturn(false);
        Mockito.when(roleRepository.findByName("USER")).thenReturn(Optional.of(savedRole));
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(1L);
            return userToSave;
        });
        Mockito.when(passwordEncoder.encode(inputUser.getPassword())).thenReturn("encodedpassword");

        User result = userService.register(inputUser);

        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        Assertions.assertThat(capturedUser).isEqualTo(savedUser);
        Assertions.assertThat(result).isEqualTo(capturedUser);
    }

    @Test
    public void UserService_Register_CreatesRoleAndUser() {

        Mockito.when(passwordEncoder.encode(inputUser.getPassword())).thenReturn("encodedpassword");
        Mockito.when(userRepository.existsByUsername(inputUser.getUsername())).thenReturn(false);
        Mockito.when(roleRepository.findByName("USER")).thenReturn(Optional.empty());
        Mockito.when(userRepository.save(Mockito.any(User.class))).thenAnswer(invocation -> {
            User userToSave = invocation.getArgument(0);
            userToSave.setId(1L);
            return userToSave;
        });
        Mockito.when(roleRepository.save(Mockito.any(Role.class))).thenAnswer(invocation -> {
            Role roleToSave = invocation.getArgument(0);
            roleToSave.setId(1L);
            return roleToSave;
        });

        User result = userService.register(inputUser);
        
        ArgumentCaptor<Role> roleCaptor = ArgumentCaptor.forClass(Role.class);
        Mockito.verify(roleRepository).save(roleCaptor.capture());
        Role capturedRole = roleCaptor.getValue();


        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        Mockito.verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        Assertions.assertThat(capturedRole).isEqualTo(savedRole);
        Assertions.assertThat(capturedUser).isEqualTo(savedUser);

        Assertions.assertThat(result).isEqualTo(capturedUser);
    }

    @Test
    public void UserService_Register_ThrowsException() {
        
        Mockito.when(userRepository.existsByUsername(inputUser.getUsername())).thenReturn(true);

        Assertions.assertThatThrownBy(() -> userService.register(inputUser))
            .isInstanceOf(ResourceAlreadyExistsException.class);
    }

    @Test
    public void userService_Verify_ReturnsToken() {
        GrantedAuthority authority = new SimpleGrantedAuthority("ROLE_USER");
        UserDetails userDetails = new org.springframework.security.core.userdetails.User(
            inputUser.getUsername(), inputUser.getPassword(), List.of(authority));
        String mockToken = "mockJwtToken";

        Mockito.when(authManager.authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class)))
            .thenReturn(authentication);
        Mockito.when(authentication.getPrincipal()).thenReturn(userDetails);
        Mockito.when(jwtService.generateToken(inputUser.getUsername(), userDetails.getAuthorities()))
            .thenReturn(mockToken);

        String result = userService.verify(inputUser);

        Assertions.assertThat(result).isEqualTo(mockToken);

        Mockito.verify(authManager).authenticate(Mockito.any(UsernamePasswordAuthenticationToken.class));
        Mockito.verify(jwtService).generateToken(inputUser.getUsername(), userDetails.getAuthorities());
    }
    
}
