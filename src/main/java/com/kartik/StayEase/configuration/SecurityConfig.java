package com.kartik.StayEase.configuration;

import com.kartik.StayEase.services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Lazy;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;


import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {


    private final JwtAuthenticationFilter jwtAuthenticationFilter;

    public SecurityConfig(JwtAuthenticationFilter jwtAuthenticationFilter) {
        this.jwtAuthenticationFilter = jwtAuthenticationFilter;
    }

    @Bean
    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .authorizeRequests(authz -> authz
                        .requestMatchers("/public/**").permitAll() // Allow public endpoints
                        .requestMatchers("/register").permitAll() // Allow register endpoint
                        .requestMatchers("/hotels/**").hasRole("ADMIN")  // Update the roles as needed)
                        .requestMatchers("/bookings/**").hasAnyRole("ADMIN", "HOTEL_MANAGER")
                        .anyRequest().authenticated()        // Protect all other endpoints
                )
                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
//
//    private final JwtTokenProvider jwtTokenProvider;
//    private final UserDetailsService userDetailsService;
//    private final PasswordEncoder passwordEncoder;
//    @Lazy // Delays initialization to avoid circular dependency
//    private final AuthenticationManager authenticationManager;
//
//    @Autowired
//    public SecurityConfig(
//            JwtTokenProvider jwtTokenProvider,
//            UserDetailsService userDetailsService,
//            PasswordEncoder passwordEncoder,
//            @Lazy AuthenticationManager authenticationManager) {
//        this.jwtTokenProvider = jwtTokenProvider;
//        this.userDetailsService = userDetailsService;
//        this.passwordEncoder = passwordEncoder;
//        this.authenticationManager = authenticationManager;
//    }
//
//    @Bean
//    public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
//        JwtAuthenticationFilter jwtAuthenticationFilter = new JwtAuthenticationFilter(jwtTokenProvider, userDetailsService);
//
//        http
//                .csrf(csrf -> csrf.disable())
//                .authorizeHttpRequests(authz -> authz
//                        .requestMatchers("/api/auth/**").permitAll()
//                        .anyRequest().authenticated()
//                )
//                .addFilterBefore(jwtAuthenticationFilter, UsernamePasswordAuthenticationFilter.class);
//
//        return http.build();
//    }
//
//    @Bean
//    public AuthenticationManager authenticationManager(HttpSecurity http) throws Exception {
//        AuthenticationManagerBuilder authenticationManagerBuilder = http.getSharedObject(AuthenticationManagerBuilder.class);
//        authenticationManagerBuilder.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder);
//        return authenticationManagerBuilder.build();
//    }
//
    @Bean
    public PasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
}



/**
 *
 *
 * @Configuration: Marks this class as a configuration class to be processed by Spring.
 * @Bean: Tells Spring to treat this method as a bean definition, so Spring will instantiate BCryptPasswordEncoder and manage it in the application context.
 * passwordEncoder(): This method returns an instance of BCryptPasswordEncoder, which Spring will use whenever a PasswordEncoder bean is required.
 *
 * there’s no explicit HTTP security configuration (like allowing certain endpoints or securing others). This might be why the 401 Unauthorized error is being triggered.
 */