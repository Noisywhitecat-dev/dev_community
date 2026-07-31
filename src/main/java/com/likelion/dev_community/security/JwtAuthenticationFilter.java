package com.likelion.dev_community.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtProvider jwtProvider;

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                     HttpServletResponse response,
                                     FilterChain filterChain) throws ServletException, IOException {

        String token = resolveToken(request);

        if (token != null) {
            try {
                Claims claims = jwtProvider.parseAccessToken(token);

                Long userId = Long.valueOf(claims.getSubject());
                String username = claims.get("username", String.class);
                String nickname = claims.get("nickname", String.class);
                List<String> roles = claims.get("roles", List.class);

                List<GrantedAuthority> authorities = roles.stream()
                        .map(role -> (GrantedAuthority) new SimpleGrantedAuthority("ROLE_" + role))
                        .toList();

                CustomUserDetails userDetails = new CustomUserDetails(userId, username, null, nickname, authorities);

                Authentication authentication = new JwtAuthenticationToken(userDetails, authorities);

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
            catch (JwtException e){
                SecurityContextHolder.clearContext();
            }

        }

        filterChain.doFilter(request, response);
    }

    private String resolveToken(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }
}
