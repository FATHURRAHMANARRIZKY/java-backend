package fimpa.co.fimpa.filter;

import fimpa.co.fimpa.util.JwtUtil;
import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
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
            "/ratings/**", "/verify-token", "/me", "/uploads","/uploads/**", "/admins", "/users");

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        String requestURI = httpRequest.getRequestURI();
        String method = httpRequest.getMethod();

        System.out.println("Request Method: " + method);
        System.out.println("Request URI: " + requestURI);

        // Skip the URL paths defined in PUBLIC_URLS
        for (String url : PUBLIC_URLS) {
            if (requestURI.startsWith(url)) {
                chain.doFilter(request, response); // Skip filtering for public URLs
                return;
            }
        }

        // Check for JWT token in cookie
        String token = null;
        Cookie[] cookies = httpRequest.getCookies();
        if (cookies != null) {
            for (Cookie cookie : cookies) {
                if ("token".equals(cookie.getName())) {
                    token = cookie.getValue();
                }
            }
        }

        // Validate token
        if (token != null && jwtUtil.validateToken(token)) {
            System.out.println("Token is valid: " + token);
            chain.doFilter(request, response); // Proceed with request
        } else {
            System.out.println("Invalid token or no token found.");
            httpResponse.setStatus(HttpServletResponse.SC_UNAUTHORIZED); // Send 401 Unauthorized
            httpResponse.getWriter().write("Unauthorized access: Invalid token");
        }
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}