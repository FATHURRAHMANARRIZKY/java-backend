package fimpa.co.fimpa.controller;

import fimpa.co.fimpa.model.Users;
import fimpa.co.fimpa.model.Admin;
import fimpa.co.fimpa.model.LoginRequest;
import fimpa.co.fimpa.service.UsersService;
import fimpa.co.fimpa.service.AdminService;
import fimpa.co.fimpa.util.JwtUtil;
import io.jsonwebtoken.lang.Arrays;

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
            // Log token yang dihasilkan untuk memastikan konsistensi
            System.out.println("Token dihasilkan: " + token);
            return createResponse(response, token);
        }

        return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid email or password");
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(HttpServletResponse response) {
        Cookie cookie = new Cookie("token", null);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(0); // Hapus cookie
        response.addCookie(cookie);

        // Log setelah menambahkan cookie
        System.out.println("Cookie token dihapus: " + cookie.toString());

        response.setHeader("Set-Cookie",
                "token=; Path=/; HttpOnly; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure=false; SameSite=Lax");

        // Log header Set-Cookie
        System.out.println("Header Set-Cookie setelah logout: " + response.getHeader("Set-Cookie"));

        return ResponseEntity.status(HttpStatus.OK).body("Logout successful");
    }
    private ResponseEntity<?> createResponse(HttpServletResponse response, String token) {
        Cookie cookie = new Cookie("token", token);
        cookie.setHttpOnly(true);
        cookie.setSecure(false); // Set to true if using HTTPS
        cookie.setPath("/");
        cookie.setMaxAge(3600); // Set expiration time in seconds
        response.addCookie(cookie);

        Map<String, String> responseBody = new HashMap<>();
        responseBody.put("token", token);
        return ResponseEntity.ok(responseBody);
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
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Invalid token");
        }
    }
}