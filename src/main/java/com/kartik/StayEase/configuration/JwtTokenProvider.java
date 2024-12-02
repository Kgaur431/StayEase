package com.kartik.StayEase.configuration;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import javax.crypto.spec.SecretKeySpec;
import java.util.*;
import java.util.stream.Collectors;

@Component
public class JwtTokenProvider {

    private final SecretKey secretKey = new SecretKeySpec(
            "aVeryStronVeryStrongSecretKeyWithAtLeast512BitsLength!gSecretKeyWithAtLeast512BitsLength!!!".getBytes(),
            "HmacSHA512");
    private final long EXPIRATION_TIME = 86400000; // 24 hours in milliseconds

    // Generate JWT token with username and authorities


    // Validate JWT token
    public boolean validateToken(String token) {
        System.out.println("Secret Key (generation): " + secretKey);
            try {
                System.out.println("Validating token: " + token);
                Jwts.parserBuilder()
                        .setSigningKey(secretKey)
                        .build()
                        .parseClaimsJws(token);
                return true;
            } catch (Exception e) {
                System.out.println("Invalid token: " + e.getMessage());
                return false;
            }
    }

    // Extract username from JWT token
    public String getUsernameFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        return claims.getSubject();
    }

    public String generateToken(String username, Collection<? extends GrantedAuthority> authorities) {
        System.out.println("Secret Key (validation): " + secretKey);
        String roles = authorities.stream()
                .map(GrantedAuthority::getAuthority) // Extract the authority as is
                .collect(Collectors.joining(","));
        return Jwts.builder()
                .setSubject(username)
                .claim("roles", roles) // Store roles without re-prefixing
                .setExpiration(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                .signWith(secretKey, SignatureAlgorithm.HS512)
                .compact();
    }


    public List<GrantedAuthority> getRolesFromToken(String token) {
        Claims claims = Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
        String roles = claims.get("roles", String.class);
        if (roles == null || roles.isEmpty()) {
            return Collections.emptyList();
        }
        return Arrays.stream(roles.split(","))
                .map(SimpleGrantedAuthority::new)
                .collect(Collectors.toList());
    }

    // Extract all claims from the JWT token
    public Claims getClaimsFromToken(String token) {
        return Jwts.parser().setSigningKey(secretKey).parseClaimsJws(token).getBody();
    }


//    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
//        List<GrantedAuthority> authorities = getRolesFromToken(token).stream()
//                .map(role -> new SimpleGrantedAuthority(role.getAuthority()))
//                .collect(Collectors.toList());
//        return authorities;
//    }

    public Collection<? extends GrantedAuthority> getAuthorities(String token) {
        List<String> roles = getRolesFromToken(token).stream()
                .map(GrantedAuthority::getAuthority) // Extract the authority as is
                .collect(Collectors.toList());
        return roles.stream()
                .map(role -> new SimpleGrantedAuthority("ROLE_" + role)) // Prefix roles
                .collect(Collectors.toList());
    }


}



/**
 *
 * Since authResult.getAuthorities() returns a collection of GrantedAuthority objects,
 * we don't need to cast it to a specific type unless we know it will always be a List<? extends GrantedAuthority>.
 * so generateToken method should be able to handle the authorities collection correctly, regardless of whether it's a List, Set, or any other collection type.
 */
