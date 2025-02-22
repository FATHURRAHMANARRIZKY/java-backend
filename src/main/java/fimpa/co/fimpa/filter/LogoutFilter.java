package fimpa.co.fimpa.filter;

import jakarta.servlet.Filter;
import jakarta.servlet.FilterChain;
import jakarta.servlet.FilterConfig;
import jakarta.servlet.ServletException;
import jakarta.servlet.ServletRequest;
import jakarta.servlet.ServletResponse;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import java.io.IOException;

@Component
@Order(1)
public class LogoutFilter implements Filter {

    @Override
    public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
            throws IOException, ServletException {
        HttpServletRequest httpRequest = (HttpServletRequest) request;
        HttpServletResponse httpResponse = (HttpServletResponse) response;

        // Log untuk memeriksa metode permintaan dan URI
        System.out.println("Metode permintaan di filter: " + httpRequest.getMethod());
        System.out.println("URI permintaan di filter: " + httpRequest.getRequestURI());

        if ("POST".equalsIgnoreCase(httpRequest.getMethod()) && "/logout".equals(httpRequest.getRequestURI())) {
            // Log untuk memverifikasi apakah token ada di header
            String token = httpRequest.getHeader("Authorization");
            if (token != null && !token.isEmpty()) {
                System.out.println("Token diterima di header: " + token);
            } else {
                System.out.println("Token tidak ada di header.");
            }

            // Hapus cookie token
            Cookie cookie = new Cookie("token", null);
            cookie.setHttpOnly(true);
            cookie.setSecure(false); // Set to true if using HTTPS
            cookie.setPath("/");
            cookie.setMaxAge(0); // Hapus cookie
            httpResponse.addCookie(cookie);

            httpResponse.setHeader("Set-Cookie",
                    "token=; Path=/; HttpOnly; Max-Age=0; Expires=Thu, 01 Jan 1970 00:00:00 GMT; Secure=false; SameSite=Lax");
        }

        chain.doFilter(request, response);
    }

    @Override
    public void init(FilterConfig filterConfig) throws ServletException {
    }

    @Override
    public void destroy() {
    }
}