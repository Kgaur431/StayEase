package com.kartik.StayEase.controllers;

import com.kartik.StayEase.dto.UserRequestDTO;
import com.kartik.StayEase.dto.UserResponseDTO;
import com.kartik.StayEase.entities.User;
import com.kartik.StayEase.services.UserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/public/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    // Register a new userOS
    @PostMapping("/register")
    public ResponseEntity<UserResponseDTO> registerUser(@Validated @RequestBody UserRequestDTO request) {
        User user = new User();
        user.setEmail(request.getEmail());
        user.setPassword(request.getPassword());
        user.setFirstName(request.getFirstName());
        user.setLastName(request.getLastName());
        user.setRoles(request.getRole()); // Set role if provided

        String token = userService.registerUser(user);
        return new ResponseEntity<>(new UserResponseDTO("Registration successful", token), HttpStatus.CREATED);
    }

    // Login user
    @PostMapping("/login")
    public ResponseEntity<UserResponseDTO> loginUser(@Validated @RequestBody UserRequestDTO request) {
        String token = userService.loginUser(request.getEmail(), request.getPassword());
        return new ResponseEntity<>(new UserResponseDTO("Login successful", token), HttpStatus.OK);
    }
}
