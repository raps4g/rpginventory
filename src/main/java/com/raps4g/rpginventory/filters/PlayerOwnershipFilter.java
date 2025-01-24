package com.raps4g.rpginventory.filters;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

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
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException, java.io.IOException {
        String uri = request.getRequestURI();
        if (uri.contains("/players")) {
            Long playerId = extractPlayerIdFromUri(uri);
            System.out.println(playerId);

            if (playerId != null) {
                String authHeader = request.getHeader("Authorization");
                if (authHeader == null) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // Or throw a custom exception
                    response.getWriter().write("You do not have permission to access this resource.");
                    return;
                }
                String token = jwtService.extractToken(authHeader);
                Long userId = jwtService.extractUserId(token);

                Player player = playerRepository.findById(playerId)
                    .orElseThrow(() -> new RuntimeException("Player not found in filter."));

                if (!userId.equals(player.getUser().getId())) {
                    response.setStatus(HttpServletResponse.SC_FORBIDDEN);  // Or throw a custom exception
                    response.getWriter().write("You do not have permission to access this resource.");
                    return;
                }
            }
        }
        System.out.println("User validated.");
        filterChain.doFilter(request, response); // Continue processing the request
    }

    private Long extractPlayerIdFromUri(String uri) {
        System.out.println(uri);
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
}

