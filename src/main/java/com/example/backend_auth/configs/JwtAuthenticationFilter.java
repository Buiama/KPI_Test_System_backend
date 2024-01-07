package com.example.backend_auth.configs;

import com.example.backend_auth.services.implementations.JwtService;
import com.example.backend_auth.services.implementations.StudentService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import java.io.IOException;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {
    private final StudentService studentService;
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(@NonNull HttpServletRequest request,
                                    @NonNull HttpServletResponse response,
                                    @NonNull FilterChain filterChain) throws ServletException, IOException {
        if (request.getServletPath().contains("/api/v1/login")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String authenticationHeader = request.getHeader("Authorization");
        if (authenticationHeader == null || !authenticationHeader.startsWith("Bearer ")) {
            filterChain.doFilter(request, response);
            return;
        }

        final String jwt = authenticationHeader.substring(7);
        final String email = jwtService.getEmail(jwt);

        if (email != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails student = this.studentService.loadUserByUsername(email);
            if(jwtService.isJwtTokenValid(jwt, student)) {
                UsernamePasswordAuthenticationToken authToken =
                        new UsernamePasswordAuthenticationToken(student,null, student.getAuthorities());
                authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                SecurityContextHolder.getContext().setAuthentication(authToken);
            }
        }
        filterChain.doFilter(request, response);
    }
}
