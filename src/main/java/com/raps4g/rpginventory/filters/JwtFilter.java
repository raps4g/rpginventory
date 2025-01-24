package com.raps4g.rpginventory.filters;

import java.io.IOException;
import java.time.Instant;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationContext;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.raps4g.rpginventory.services.impl.UserDetailsServiceImpl;

import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.MalformedJwtException;
import io.jsonwebtoken.security.SignatureException;

import com.raps4g.rpginventory.services.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@Component
public class JwtFilter extends OncePerRequestFilter{

    @Autowired
    private JwtService jwtService;

    @Autowired
    private ApplicationContext context;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
    throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        String token = null;
        String username = null;

        try {

            if(authHeader != null && authHeader.startsWith("Bearer ")) {
                token = jwtService.extractToken(authHeader);
                username = jwtService.extractUsername(token);
            } 

            if(username != null && SecurityContextHolder.getContext().getAuthentication() == null) {

                UserDetails userDetails = context.getBean(UserDetailsServiceImpl.class).loadUserByUsername(username); 

                if(jwtService.validateToken(token, userDetails)) {

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);

                }

            } 
            filterChain.doFilter(request, response);
        } catch (SignatureException ex) {
            handleException(response, "INVALID_JWT_SIGNATURE", "Invalid JWT signature.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (MalformedJwtException ex) {
            handleException(response, "MALFORMED_JWT", "Invalid JWT token.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (ExpiredJwtException ex) {
            handleException(response, "JWT_EXPIRED", "JWT token has expired.", HttpServletResponse.SC_UNAUTHORIZED);
        } catch (Exception ex) {
            handleException(response, "AUTHENTICATION_FAILED", "Authentication failed.", HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    private void handleException(HttpServletResponse response, String errorCode, String message, int statusCode) throws IOException {
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

