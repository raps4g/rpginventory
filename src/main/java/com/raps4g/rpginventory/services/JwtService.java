package com.raps4g.rpginventory.services;

import java.security.Key;
import java.util.Collection;
import java.util.Date;
import java.util.List;
import java.util.function.Function;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import com.raps4g.rpginventory.model.User;

import io.jsonwebtoken.Claims;

public interface JwtService {

    String generateToken(String username, Collection<? extends GrantedAuthority> authorities);

    Key getKey();

    boolean validateToken(String token, UserDetails userDetails);

    boolean isTokenExpired(String token);

    Date extractExpiration(String token);

    String extractUsername(String token);

    User extractUser(String token);

    Long extractUserId(String token);

    <T> T extractClaim(String token, Function<Claims, T> claimResolver);

    Claims extractAllClaims(String token);

    List<GrantedAuthority> getAuthoritiesFromToken(String token);

    String extractToken(String authHeader);
}
