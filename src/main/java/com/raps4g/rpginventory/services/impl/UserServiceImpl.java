package com.raps4g.rpginventory.services.impl;

import com.raps4g.rpginventory.services.JwtService;
import com.raps4g.rpginventory.services.UserService;
import com.raps4g.rpginventory.exceptions.InvalidCredentialsException;
import java.util.Optional;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.raps4g.rpginventory.dto.LoginRequestDto;
import com.raps4g.rpginventory.dto.RegisterRequestDto;
import com.raps4g.rpginventory.dto.UserResponseDto;
import com.raps4g.rpginventory.exceptions.ResourceAlreadyExistsException;
import com.raps4g.rpginventory.model.Role;
import com.raps4g.rpginventory.model.User;
import com.raps4g.rpginventory.repositories.RoleRepository;
import com.raps4g.rpginventory.repositories.UserRepository;


@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private RoleRepository roleRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private AuthenticationManager authManager;

    @Autowired
    private JwtService jwtService;

    @Override
    public UserResponseDto mapToUserResponseDto(User user) {
        UserResponseDto userResponse = UserResponseDto.builder()
            .id(user.getId())
            .username(user.getUsername())
            .build();
        return userResponse;
    }
    
    @Override
    public User mapFromLoginRequestDto(LoginRequestDto loginRequestDto) {
        User user = User.builder()
            .username(loginRequestDto.getUsername())
            .password(loginRequestDto.getPassword())
            .build();
        return user;
    }

    @Override
    public User mapFromRegisterRequestDto(RegisterRequestDto registerRequestDto) {
        User user = User.builder()
            .username(registerRequestDto.getUsername())
            .password(registerRequestDto.getPassword())
            .build();
        return user;
    }

    @Override
    public User register(User user) {
        
        if(userRepository.existsByUsername(user.getUsername())) {
            throw new ResourceAlreadyExistsException("User with username " + user.getUsername() + " already exists.");
        }

        Optional<Role> foundUserRole = roleRepository.findByName("USER");

        Role userRole;
        if (foundUserRole.isPresent()) {
            userRole = foundUserRole.get();
        } else {
            userRole = Role.builder().name("USER").build();
            userRole = roleRepository.save(userRole);

        }

        user.setPassword(passwordEncoder.encode(user.getPassword()));
        user.setRoles(Set.of(userRole));
        return userRepository.save(user);
    }

    @Override
    public String verify(User user) {
        Authentication authentication = 
            authManager.authenticate(new UsernamePasswordAuthenticationToken(user.getUsername(), user.getPassword()));

        UserDetails userDetails = (UserDetails) authentication.getPrincipal();
        return jwtService.generateToken(user.getUsername(), userDetails.getAuthorities());

    }

}
