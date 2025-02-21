package fimpa.co.fimpa.controller;

import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.service.UsersService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class UsersController {

    @Autowired
    private UsersService service;

    @PostMapping("/register-user")
    public ResponseEntity<?> register(@Valid @RequestBody Users user) {
        service.register(user);
        return ResponseEntity.ok("User registered successfully");
    }
}