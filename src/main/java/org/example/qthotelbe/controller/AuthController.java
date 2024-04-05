package org.example.qthotelbe.controller;

import lombok.RequiredArgsConstructor;
import org.example.qthotelbe.exception.UserAlreadyExistsException;
import org.example.qthotelbe.model.User;
import org.example.qthotelbe.service.IUserService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;

@RequiredArgsConstructor
public class AuthController {
    private final IUserService userService;


    @PostMapping("/register-user")
    public ResponseEntity<?> registerUser(@RequestBody  User user){
        try {
            User theUser = userService.registerUser(user);
            return ResponseEntity.ok("User registered successfully");
        } catch (UserAlreadyExistsException e) {
            return ResponseEntity.status(HttpStatus.CONFLICT).body(e.getMessage());
        }
    }
}
