package com.raps4g.rpginventory.filters;

import com.raps4g.rpginventory.exceptions.PlayerOwnershipException;
import java.time.Instant;
import java.util.Collection;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raps4g.rpginventory.exceptions.ResourceNotFoundException;
import com.raps4g.rpginventory.model.Player;
import com.raps4g.rpginventory.repositories.PlayerRepository;
import com.raps4g.rpginventory.services.JwtService;

import io.jsonwebtoken.io.IOException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class PlayerOwnershipFilter extends OncePerRequestFilter {

    @Autowired
    private JwtService jwtService;

    @Autowired
    private PlayerRepository playerRepository;

    @Override
    protected void doFilterInternal(
        HttpServletRequest request, 
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException, java.io.IOException {
        String uri = request.getRequestURI();
        
        try {
            if (isAdmin()) {
                filterChain.doFilter(request, response);
                return;
            }
            if (uri.startsWith("/players")) {

                Long playerId = extractPlayerIdFromUri(uri);

                if (playerId != null) {

                    String authHeader = request.getHeader("Authorization");

                    if (authHeader == null) {
                        throw new PlayerOwnershipException("You do not have permission to access this resource.");
                    }

                    String token = jwtService.extractToken(authHeader);
                    Long userId = jwtService.extractUserId(token);

                    Player player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new ResourceNotFoundException("Player with id " + playerId + " not found."));

                    if (!userId.equals(player.getUser().getId())) {
                        throw new PlayerOwnershipException("You do not have permission to access this resource.");
                    }
                }
            }

            filterChain.doFilter(request, response);
        } catch (PlayerOwnershipException e) {
            handleException(response, "FORBIDDEN", e.getMessage(), HttpServletResponse.SC_FORBIDDEN);
        } catch (ResourceNotFoundException e) {
            handleException(response, "RESOURCE_NOT_FOUND", e.getMessage(), HttpServletResponse.SC_NOT_FOUND);
        }
    }

    private boolean isAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication != null && authentication.isAuthenticated()) {
            Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
            for (GrantedAuthority authority : authorities) {
                if ("ROLE_ADMIN".equals(authority.getAuthority())) {
                    return true;
                }
            }
        }
        return false;
    }

    private Long extractPlayerIdFromUri(String uri) {
        String target = "/players/";
        int startIndex = uri.indexOf(target);

        if (startIndex != -1) {
            startIndex += target.length();

            int endIndex = startIndex;
            while (endIndex < uri.length() && Character.isDigit(uri.charAt(endIndex))) {
                endIndex++;
            }

            String numberSubstring = uri.substring(startIndex, endIndex);
            if (!numberSubstring.isEmpty()) {
                return Long.parseLong(numberSubstring);
            }
        }

        return null;
    }

    private void handleException(
        HttpServletResponse response,
        String errorCode,
        String message,
        int statusCode
    ) throws IOException, java.io.IOException {
        response.setStatus(statusCode);
        response.setContentType("application/json");

        String jsonResponse = String.format(
            "{ \"timestamp\": \"%s\", \"message\": \"%s\", \"status\": %d, \"error_code\": \"%s\", }",
            Instant.now(),
            message,
            statusCode,
            errorCode
        );

        response.getWriter().write(jsonResponse);
        response.getWriter().flush();
    }
}
