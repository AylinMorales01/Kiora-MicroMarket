package com.kiora.micromarket.filter;

import java.io.IOException;

import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.util.StringUtils;

import com.kiora.micromarket.service.JwtService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtValidationFilter extends OncePerRequestFilter {
    private final JwtService jwtService;

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
        throws IOException, ServletException {
        
        String path = request.getRequestURI();

        // Rutas publicas
        if (path.startsWith("/api/v1/employees/auth/login") || path.equals("/api/v1/employees/create")) {
            filterChain.doFilter(request, response);
            return;
        }

        String bearerToken = request.getHeader("Authorization");

        if (StringUtils.hasText(bearerToken) && bearerToken.startsWith("Bearer ")) {
            String token = bearerToken.substring(7);

            if (jwtService.isTokenValid(token)) {
                // Token valido, continuamos
                filterChain.doFilter(request, response);
                return;
            }
        }

        // Token invalido o no presente
        response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Acceso no autorizado o token invalido");
    }
}
