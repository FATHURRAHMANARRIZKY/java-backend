package fimpa.co.fimpa.controller;

import fimpa.co.fimpa.model.Admin;
import fimpa.co.fimpa.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.util.List;

@RestController
public class AdminController {

    @Autowired
    private AdminService service;

    @GetMapping("/admins")
    public ResponseEntity<List<Admin>> getAllAdmins() {
        List<Admin> admin = service.findAll();
        return ResponseEntity.ok(admin);
    }

    @PostMapping("/register")
public ResponseEntity<String> registerAdmin(@RequestParam("username") String username,
                                            @RequestParam("email") String email,
                                            @RequestParam("password") String password,
                                            @RequestParam("fullName") String fullName,
                                            @RequestParam("phoneNumber") String phoneNumber,
                                            @RequestParam("address") String address,
                                            @RequestParam("file") MultipartFile file) throws IOException {

    // Save the uploaded profile photo and get the image URL
    String profileImageUrl = service.saveImage(file, "profile/admin/");

    // Create Admin object with the profile image URL
    Admin admin = new Admin(null, username, email, password, null, profileImageUrl, fullName, phoneNumber, address);
    service.register(admin);

    return ResponseEntity.ok("Admin registered successfully");
}

}