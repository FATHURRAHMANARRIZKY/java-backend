package fimpa.co.fimpa.controller;

import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.model.Admin;
import fimpa.co.fimpa.model.LoginRequest;
import fimpa.co.fimpa.service.UsersService;
import fimpa.co.fimpa.service.AdminService;
import fimpa.co.fimpa.util.JwtUtil;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.Cookie;

@RestController
public class AuthController {

    @Autowired
    private UsersService usersService;

    @Autowired
    private AdminService adminService;

    @Autowired
    private JwtUtil jwtUtil;

    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletResponse response) {
        Users user = usersService.loginByEmail(loginRequest.getEmail(), loginRequest.getPassword());
        Admin admin = adminService.loginByEmail(loginRequest.getEmail(), loginRequest.getPassword());

        String role;
        String token = null;

        if (user != null) {
            role = "USER";
            token = jwtUtil.generateToken(user.getEmail(), role, user.getUsername());
        } else if (admin != null) {
            role = "ADMIN";
            token = jwtUtil.generateToken(admin.getEmail(), role, admin.getUsername());
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
        }

        if (token != null) {
            return createResponse(response, token);
        }
        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    private ResponseEntity<?> createResponse(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(3600);
        response.addCookie(cookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok(responseBody);
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false);
        cookie.setPath("/");
        cookie.setMaxAge(0);
        response.addCookie(cookie);

        response.setHeader("Set-Cookie",
                "token=; Path=/; HttpOnly; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure=false; SameSite=Lax");

        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }

    @GetMapping("/verify-token")
    public ResponseEntity<?> verifyToken(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtUtil.validateToken(token)) {
            String role = jwtUtil.extractRole(token);
            Map<String, String> responseBody = new HashMap<>();
            responseBody.put("role", role);
            return ResponseEntity.ok(responseBody);
        } else {
            return null;
        }
    }

    @GetMapping("/me")
    public ResponseEntity<?> getCurrentUser(HttpServletRequest request) {
        String token = null;
        Cookie[] cookies = request.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                    break;
                }
            }
        }

        if (token != null && jwtUtil.validateToken(token)) {
            String email = jwtUtil.extractEmail(token);

            Users user = usersService.findByEmail(email);
            Admin admin = adminService.findByEmail(email);

            Map<String, String> responseBody = new HashMap<>();
            if (user != null) {
                responseBody.put("username", user.getUsername());
                responseBody.put("email", user.getEmail());
                responseBody.put("profilePicture",
                        "http://localhost:8080" + user.getProfilePicture());
            } else if (admin != null) {
                responseBody.put("username", admin.getUsername());
                responseBody.put("email", admin.getEmail());
                responseBody.put("profilePicture",
                        "http://localhost:8080" + admin.getProfilePicture());
            }
            return ResponseEntity.ok(responseBody);
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}