package com.raps4g.rpginventory.services.impl;

import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.List;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.raps4g.rpginventory.repositories.UserRepository;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SecurityException;

@ExtendWith(MockitoExtension.class)
public class JwtServiceImplTest {

    @InjectMocks
    private JwtServiceImpl jwtService;

    @Mock
    private UserRepository userRepository;

    private String fixedSecretKey;
    private String username;
    private String password;

    @BeforeEach
    public void setUp() {
        username = "coolusername";
        password = "securepassword";
        fixedSecretKey = Base64.getEncoder().encodeToString("super_secret_and_secure_key_3000".getBytes(StandardCharsets.UTF_8));
        jwtService.secretKey = fixedSecretKey;
    }

    @Test
    public void jwtService_GenerateToken_Generates() {

        List<GrantedAuthority> authorities = List.of(
            new SimpleGrantedAuthority("ROLE_USER"),
            new SimpleGrantedAuthority("ROLE_ADMIN")
        );

        String token = jwtService.generateToken(username, authorities);

        Claims claims = jwtService.extractAllClaims(token);

        Assertions.assertThat(claims.getSubject()).isEqualTo(username);
        Assertions.assertThat(claims.get("roles", List.class))
                .containsExactlyInAnyOrder("ROLE_USER", "ROLE_ADMIN");
    }
    
    @Test
    public void jwtService_ValidateToken_ReturnsTrue() {
        List<GrantedAuthority> authorities = List.of(new SimpleGrantedAuthority("ROLE_USER"));
        String token = jwtService.generateToken(username, authorities);

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, authorities);

        boolean isValid = jwtService.validateToken(token, userDetails);

        Assertions.assertThat(isValid).isTrue();
    }

    @Test
    public void jwtService_ValidateToken_ThrowsSecurityException() {
        String invalidToken = "eyJhbGciOiJIUzI1NiJ9.eyJyb2xlcyI6WyJST0xFX1VTRVIiXSwic3ViIjoidXNlciIsImlhdCI6MTczODAxNzU2MywiZXhwIjoxNzM4MDE5MDAzfQ.RcrfGJuWAlWlzAL8SBnCv6la3uDEld9QvWKykR-WhPo";

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, List.of());

        Assertions.assertThatThrownBy(() -> jwtService.validateToken(invalidToken, userDetails))
            .isInstanceOf(SecurityException.class);
    }

    @Test
    public void jwtService_ValidateToken_ThrowsMalformedTokenException() {
        String invalidToken = "test";

        UserDetails userDetails = new org.springframework.security.core.userdetails.User(username, password, List.of());

        Assertions.assertThatThrownBy(() -> jwtService.validateToken(invalidToken, userDetails))
            .isInstanceOf(MalformedJwtException.class);
    }
}
