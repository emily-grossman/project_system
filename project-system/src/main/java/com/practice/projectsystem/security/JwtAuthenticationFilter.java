package com.practice.projectsystem.security;

import com.practice.projectsystem.web.ErrorResponseDTO;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.Set;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final ObjectMapper objectMapper;
    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class);

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, ObjectMapper objectMapper) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.objectMapper = objectMapper;
    }

    @Override
    protected void doFilterInternal(
            HttpServletRequest request,
            HttpServletResponse response,
            FilterChain filterChain) throws ServletException, IOException
    {
        // Извлекаем токен из заголовка
        String token = extractTokenFromHeaders(request);

        if (token != null ) {
            try {
                jwtTokenProvider.validateToken(token);

                // Извлекаем email и роли из токена
                String email = jwtTokenProvider.getEmailFromToken(token);
                Set<String> roles = jwtTokenProvider.getRolesFromToken(token);

                // Создаем "полномочия"
                var authorities = roles.stream()
                        .map(SimpleGrantedAuthority::new)
                        .toList();

                // Создаём объект аутентификации
                var authentication = new UsernamePasswordAuthenticationToken(
                        email,
                        null,
                        authorities
                );

                // Устанавливаем в SecurityContext
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } catch (ExpiredJwtException e) {
                log.warn("Токен истёк для запроса {}", request.getRequestURI());
                writeErrorResponse(response, HttpStatus.UNAUTHORIZED,
                        "Токен истёк", "Получите новый токен через /users/login. " + e.getMessage());
                return;
            } catch (JwtException | IllegalArgumentException e) {
                log.warn("Невалидный токен для запроса {}: {}", request.getRequestURI(), e.getMessage());
                writeErrorResponse(response, HttpStatus.UNAUTHORIZED,
                        "Невалидный токен", e.getMessage());
                return;
            }

        }
        filterChain.doFilter(request, response);
    }

    private String extractTokenFromHeaders(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7); // Убираем Bearer
        }
        return null;
    }
    private void writeErrorResponse(
            HttpServletResponse response,
            HttpStatus status,
            String message,
            String details
    ) throws IOException {
        response.setStatus(status.value());
        response.setContentType("application/json");
        response.setCharacterEncoding("UTF-8");

        ErrorResponseDTO errorDto = new ErrorResponseDTO(message, details, LocalDateTime.now());
        response.getWriter().write(objectMapper.writeValueAsString(errorDto));
    }

}
