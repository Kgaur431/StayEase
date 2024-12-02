package com.kartik.StayEase.configuration;

import com.kartik.StayEase.services.UserService;
import io.jsonwebtoken.Claims;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.Collections;


@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private final JwtTokenProvider jwtTokenProvider;
    private final UserService userDetailsService;

    public JwtAuthenticationFilter(JwtTokenProvider jwtTokenProvider, UserService userDetailsService) {
        this.jwtTokenProvider = jwtTokenProvider;
        this.userDetailsService = userDetailsService;
    }

    // @Override
//    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
//            throws ServletException, IOException {
//
//        String path = request.getRequestURI();
//
//        // Skip JWT filter for public endpoints
//        if (path.startsWith("/public/")) {
//            filterChain.doFilter(request, response);
//            return;
//        }
//
//        String token = getTokenFromRequest(request);
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            System.out.println("Token is valid");
//        } else {
//            System.out.println("Token is invalid" + token);
//        }
//        if (token != null && jwtTokenProvider.validateToken(token)) {
//            String username = jwtTokenProvider.getUsernameFromToken(token);
//
//            // Log the roles from the token
//            String roles = jwtTokenProvider.getRolesFromToken(token).toString();
//            System.out.println("Roles in Token: " + roles); // This will print the roles
//
//
//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(username, null, jwtTokenProvider.getAuthorities(token));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        }
//        filterChain.doFilter(request, response);
//    }

    private String getTokenFromRequest(HttpServletRequest request) {
        String bearerToken = request.getHeader("Authorization");
        if (bearerToken != null && bearerToken.startsWith("Bearer ")) {
            return bearerToken.substring(7);
        }
        return null;
    }

    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String path = request.getRequestURI();

        // Skip JWT filter for public endpoints
        if (path.startsWith("/public/")) {
            filterChain.doFilter(request, response);
            return;
        }

        String token = getTokenFromRequest(request);
        if (token != null && jwtTokenProvider.validateToken(token)) {
            System.out.println("Token is valid");
            String username = jwtTokenProvider.getUsernameFromToken(token);
            System.out.println("Username in Token: " + username);

            // Log the roles from the token
            String roles = jwtTokenProvider.getRolesFromToken(token).toString();
            System.out.println("Roles in Token: " + roles); // This will print the roles

//            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
//                UsernamePasswordAuthenticationToken authentication =
//                        new UsernamePasswordAuthenticationToken(username, null, jwtTokenProvider.getAuthorities(token));
//                SecurityContextHolder.getContext().setAuthentication(authentication);
//            }
//        } else {
//            System.out.println("Token is invalid: " + token);
//        }

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                System.out.println("Setting authentication context");
                UsernamePasswordAuthenticationToken authentication =
                        new UsernamePasswordAuthenticationToken(username, null, jwtTokenProvider.getAuthorities(token));
                SecurityContextHolder.getContext().setAuthentication(authentication);
            } else {
                System.out.println("Authentication already set or username is null");
            }

            filterChain.doFilter(request, response);
        }
    }
}


/**
 *
 * chain.doFilter(request, response);: This is important to ensure the request continues after successful authentication.
 * Without this, the filter chain will stop, and the request won't reach your controllers.
 *
 * : The chain.doFilter() method might throw IOException or ServletException.
 * I added a try-catch block around the chain.doFilter call, though you should handle this exception more gracefully in production code
 *
 *
 * l chain.doFilter(request, response) in the successfulAuthentication method to ensure that the request continues down the filter chain.
 * Without it, the filter chain will be interrupted, and the request won't reach the intended destination (controller)
 */
