package com.suvam.teacherapi.security;

import com.suvam.teacherapi.exception.ErrorResponse;
import com.suvam.teacherapi.service.CustomUserDetailsService;
import com.suvam.teacherapi.service.JwtService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.Builder;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.jspecify.annotations.NonNull;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.web.filter.OncePerRequestFilter;
import tools.jackson.databind.ObjectMapper;

import java.io.IOException;
import java.time.LocalDateTime;

@Builder
@RequiredArgsConstructor
@Slf4j
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final CustomUserDetailsService userDetailsService;
    private final ObjectMapper objectMapper;
    private static final String BEARER_PREFIX = "Bearer ";
    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    ) throws ServletException, IOException {
        String authHeader = request.getHeader(HttpHeaders.AUTHORIZATION);
        String username;
        String token;
        Claims claims;

        if (authHeader == null || !authHeader.startsWith(BEARER_PREFIX)) {
            filterChain.doFilter(request, response);
            return;
        }

        try {
            token = authHeader.substring(BEARER_PREFIX.length());
            claims = jwtService.extractAllClaims(token);
            username = claims.getSubject();

        } catch (ExpiredJwtException e) {
            log.debug("Expired JWT presented: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        } catch (JwtException | IllegalArgumentException e) {
            log.warn("Rejected JWT: {}", e.getMessage());
            filterChain.doFilter(request, response);
            return;
        }

        if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            try {
                final UserDetails userDetails = userDetailsService.loadUserByUsername(username);

                if (jwtService.isTokenValid(claims, userDetails)) {
                    UsernamePasswordAuthenticationToken authToken =
                            new UsernamePasswordAuthenticationToken(
                                    userDetails,
                                    null,
                                    userDetails.getAuthorities()
                            );

                    authToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContext context = SecurityContextHolder.createEmptyContext();
                    context.setAuthentication(authToken);
                    SecurityContextHolder.setContext(context);

                    log.info("Successfully authenticated user: {}", username);
                }
            } catch (UsernameNotFoundException e) {
                // Token is well-signed, but the subject no longer exists (deleted/renamed).
                // Treat exactly like an invalid token: fall through unauthenticated,
                // let anyRequest().authenticated() + restAuthenticationEntryPoint produce a clean 401.
                log.warn("JWT subject not found: {}", e.getMessage());
            } catch (Exception e) {
                // Genuine infra failure (DB down, timeout, etc.) — not a credentials problem.
                // Short-circuit here: neither ExceptionTranslationFilter nor
                // GlobalExceptionHandler can reach an exception thrown this early.
                log.error("Unexpected error while authenticating JWT", e);
                writeErrorResponse(
                        response,
                        HttpStatus.INTERNAL_SERVER_ERROR,
                        "Authentication failed due to a server error",
                        request.getRequestURI()
                );
                return;
            }
        }
        filterChain.doFilter(request, response);
    }

    private void writeErrorResponse(
           HttpServletResponse response,
           HttpStatus status,
           String message,
           String path
    ) throws IOException {
        ErrorResponse errorResponse = new ErrorResponse(
                LocalDateTime.now(),
                status.value(),
                status.getReasonPhrase(),
                message,
                path
        );
        response.setStatus(status.value());
        response.setContentType(MediaType.APPLICATION_JSON_VALUE);
        objectMapper.writeValue(response.getWriter(), errorResponse);
    }
}
