package fimpa.co.fimpa.filter;

import fimpa.co.fimpa.util.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.annotation.WebFilter;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.util.List;

@Component
public class JwtAuthenticationFilter implements Filter {

    @Autowired
    private JwtUtil jwtUtil;

    private static final List<String> PUBLIC_URLS = List.of(
            "/register-user", "/register", "/login", "/logout",
            "/contact", "/products", "/products/**", "/ratings",
            "/ratings/**", "/verify-token", "/me", "/uploads/**", "/admins", "/users");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        System.out.println("Metode permintaan di filter: " + method);
        System.out.println("URI permintaan di filter: " + requestURI);

        for (String url : PUBLIC_URLS) {
            if (requestURI.startsWith(url)) {
                // Jika ini adalah URL publik, lanjutkan filter chain tanpa memeriksa token
                System.out.println("Mengizinkan akses ke URL publik: " + requestURI);
                chain.doFilter(request, response);
                return;
            }
        }

        // Di bawah ini adalah pemeriksaan token untuk URL selain yang ada dalam daftar publik
        String token = null;
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        if (token != null && jwtUtil.validateToken(token)) {
            System.out.println("Token valid di filter: " + token);
            chain.doFilter(request, response); // Pastikan chain.doFilter dipanggil saat token valid
        } else {
            System.out.println("Token tidak valid atau tidak ada token di filter.");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED);
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}
