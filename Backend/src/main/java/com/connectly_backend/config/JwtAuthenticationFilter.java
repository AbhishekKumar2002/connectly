package com.connectly_backend.config;

import java.io.IOException;
import jakarta.servlet.ServletException;
import java.util.List;

import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;


@Component 
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    public JwtAuthenticationFilter(JwtService jwtService)
    {
        this.jwtService = jwtService;
    }

    @Override 
    protected void doFilterInternal(
        HttpServletRequest request,
        HttpServletResponse response,
        FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader("Authorization");

        if(authHeader == null || !authHeader.startsWith("Bearer "))
        {
            filterChain.doFilter(request, response);
            return;
        }

        String token = authHeader.substring(7);
        try{
            Claims claims = jwtService.extractClaims(token);
            String email = claims.getSubject();

            if(email == null || email.isBlank()){
                response.sendError(
                    HttpServletResponse.SC_UNAUTHORIZED,
                    "Invalid token"
                );
                return;
            }

            if(SecurityContextHolder.getContext().getAuthentication() == null){
                UsernamePasswordAuthenticationToken authentication = new UsernamePasswordAuthenticationToken(
                    email,
                    null,
                    List.of()
                );

                authentication.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                SecurityContextHolder.getContext().setAuthentication(authentication);
            }
        }catch(io.jsonwebtoken.JwtException | IllegalArgumentException exception){
            SecurityContextHolder.clearContext();

            response.sendError(
                HttpServletResponse.SC_UNAUTHORIZED,
                "Invalid or Expired Token"
            );
            return;
        }

        filterChain.doFilter(request, response);

    }
   
}
