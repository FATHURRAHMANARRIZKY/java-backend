package fimpa.co.fimpa.controller;

import fimpa.co.fimpa.model.Admin;
import fimpa.co.fimpa.service.AdminService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

import jakarta.validation.Valid;

@RestController
public class AdminController {

    @Autowired
    private AdminService service;

    @PostMapping("/register")
    public ResponseEntity<?> register(@Valid @RequestBody Admin admin) {
        service.register(admin);
        return ResponseEntity.ok("Admin registered successfully");
    }
}