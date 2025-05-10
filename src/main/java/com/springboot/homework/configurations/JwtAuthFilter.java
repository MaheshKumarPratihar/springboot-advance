package com.springboot.homework.configurations;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Profile;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
@Profile("!test")
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authHeader = request.getHeader("Authorization");
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String jwt = authHeader.substring(7);
            String username = jwtTokenProvider.getUsername(jwt);

            if (jwtTokenProvider.validateToken(jwt)) { // Validate the JWT token
                Set<String> roleNames = jwtTokenProvider.getRoles(jwt)
                        .stream()
                        .map(String::toUpperCase) // Convert to uppercase
                        .collect(Collectors.toSet());

                // Convert database roles into Spring Security authorities
                Set<SimpleGrantedAuthority> authorities = roleNames.stream()
                        .map(role -> new SimpleGrantedAuthority(role)) // Prefix with ROLE_
                        .collect(Collectors.toSet());

                // Set the authentication object in SecurityContext
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }


        }

        // Continue the filter chain
        filterChain.doFilter(request, response);
    }
}