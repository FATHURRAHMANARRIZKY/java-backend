// package fimpa.co.fimpa.filter;

// import jakarta.servlet.Filter;
// import jakarta.servlet.FilterChain;
// import jakarta.servlet.FilterConfig;
// import jakarta.servlet.ServletException;
// import jakarta.servlet.ServletRequest;
// import jakarta.servlet.ServletResponse;
// import jakarta.servlet.annotation.WebFilter;
// import jakarta.servlet.http.HttpServletRequest;
// import java.io.IOException;

// @WebFilter(urlPatterns = "/uploads/*")
// public class UploadsFilter implements Filter {

//     @Override
//     public void init(FilterConfig filterConfig) throws ServletException {
//     }

//     @Override
//     public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
//             throws IOException, ServletException {
//         HttpServletRequest req = (HttpServletRequest) request;
//         String path = req.getRequestURI();
//         if (path.startsWith("/uploads/")) {
//             chain.doFilter(request, response);
//             return;
//         }
//         response.getWriter().write("Unauthorized");
//     }

//     @Override
//     public void destroy() {
//     }
// }