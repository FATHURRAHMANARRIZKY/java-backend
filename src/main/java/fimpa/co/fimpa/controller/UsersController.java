package fimpa.co.fimpa.controller;

import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.service.UsersService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
public class UsersController {

    @Autowired
    private UsersService service;

    @GetMapping("/users")
    public ResponseEntity<List<Users>> getAllUsers() {
        List<Users> users = service.findAll();
        return ResponseEntity.ok(users);
    }

    @PostMapping("/register-user")
    public ResponseEntity<String> registerUser(
            @RequestParam("username") String username,
            @RequestParam("email") String email,
            @RequestParam("password") String password,
            @RequestParam("fullName") String fullName,
            @RequestParam("phoneNumber") String phoneNumber,
            @RequestParam("address") String address,
            @RequestParam("file") MultipartFile file) throws IOException {

        // Save the uploaded profile photo and get the image URL
        String profileImageUrl = service.saveImage(file, "profile/user/");

        Users user = new Users(null, username, email, password, null, profileImageUrl, fullName, phoneNumber, address);
        service.register(user);

        return ResponseEntity.ok("User registered successfully");
    }

}
