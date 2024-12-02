package com.kartik.StayEase.services;

import com.kartik.StayEase.configuration.JwtTokenProvider;
import com.kartik.StayEase.entities.Role;
import com.kartik.StayEase.entities.User;
import com.kartik.StayEase.repositories.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

@Service
public class UserService implements UserDetailsService {


    private UserRepository userRepository;
    private  JwtTokenProvider jwtTokenProvider;
    private  PasswordEncoder passwordEncoder;

 @Autowired
    public UserService(UserRepository userRepository, JwtTokenProvider jwtTokenProvider, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.jwtTokenProvider = jwtTokenProvider;
        this.passwordEncoder = passwordEncoder;
    }

    public UserService() {
    }

    // Register new user with optional role
    public String registerUser(User user) {
        // Check if email already exists
        Optional<User> existingUser = userRepository.findByEmail(user.getEmail());
        if (existingUser.isPresent()) {
            return "User with this email already exists.";
        }

        // Hash password before saving
        user.setPassword(passwordEncoder.encode(user.getPassword()));

        // If role is provided, assign it, else default to CUSTOMER
        Role roleFromClient = user.getRoles();
        if (roleFromClient != null) {
            user.setRoles(roleFromClient);
        } else {
            user.setRoles(Role.CUSTOMER);
        }
        userRepository.save(user);

        // Generate JWT token with authorities
        List<GrantedAuthority> authorities = Collections.singletonList(new SimpleGrantedAuthority(user.getRoles().name()));
        // Generate JWT token
        return "User registered successfully. Token: " + jwtTokenProvider.generateToken(user.getEmail(), authorities);
    }

    // Login user
    public String loginUser(String email, String password) {
        User existingUser = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (passwordEncoder.matches(password, existingUser.getPassword())) {
            // Generate JWT token on successful login
            return "Login successful. Token: " + jwtTokenProvider.generateToken(existingUser.getEmail(), Collections.singletonList(new SimpleGrantedAuthority(existingUser.getRoles().name())));
        } else {
            throw new RuntimeException("Invalid credentials");
        }
    }

    // Load user details for authentication
    @Override
    public UserDetails loadUserByUsername(String email) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        return org.springframework.security.core.userdetails.User
                .withUsername(user.getEmail())
                .password(user.getPassword())
                .authorities(user.getRoles().name())
                .build();
    }
}
