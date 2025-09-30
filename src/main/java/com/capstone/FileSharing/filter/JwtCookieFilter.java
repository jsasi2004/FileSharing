package com.capstone.FileSharing.filter;

import java.io.IOException;

import com.capstone.FileSharing.config.security.CustomUserDetails;
import com.capstone.FileSharing.model.User;
import com.capstone.FileSharing.repository.UserRepository;
import com.capstone.FileSharing.service.JwtService;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;
import org.springframework.web.servlet.HandlerExceptionResolver;


import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@Slf4j
@Component
@RequiredArgsConstructor
public class JwtCookieFilter extends OncePerRequestFilter {

    private final JwtService jwtService;
    private final UserRepository userRepository;
    private final HandlerExceptionResolver handlerExceptionResolver;

    @Override
    protected void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain
    )
            throws ServletException, IOException {
        Cookie[] cookies = request.getCookies();

        try {

            if (cookies == null) {
                log.warn("Didn't receive any cookies with the request");
                filterChain.doFilter(request, response);
                return;
            }

            String token = null;
            for (Cookie cookie : cookies)
                if ("jwtToken".equals(cookie.getName()))
                    token = cookie.getValue();

            if (token == null) {
                log.warn("Token is null");
                filterChain.doFilter(request, response);
                return;
            }
            // throw new AuthorizationHeaderMissingException("Didn't receive any cookies with key as 'jwtToken' from the request request", request.getServletPath());

            System.out.println("in jwt cookie filter : " + token);

            String email = jwtService.getUsername(token);

            if(SecurityContextHolder.getContext().getAuthentication() == null && email != null) {
                String userEmail = jwtService.getUsername(token);
                User user = userRepository.findByUsername(userEmail).get();
                CustomUserDetails userDetails = new CustomUserDetails(user);
                if(jwtService.isValidToken(email, userDetails)) {
                    UsernamePasswordAuthenticationToken authenticationToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities()
                    );

                    authenticationToken.setDetails(
                            new WebAuthenticationDetailsSource().buildDetails(request)
                    );

                    SecurityContextHolder
                            .getContext()
                            .setAuthentication(authenticationToken);

                }
            }
            filterChain.doFilter(request, response);
        }
        catch (Exception e) {
            handlerExceptionResolver.resolveException(request, response, null, e);
        }
    }
}