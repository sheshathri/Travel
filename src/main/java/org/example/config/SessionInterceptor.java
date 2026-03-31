package org.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Component;
import org.springframework.web.servlet.HandlerInterceptor;

@Component
public class SessionInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);
        
        // Allow access to login, register, and static resources
        String requestURI = request.getRequestURI();
        if (requestURI.equals("/login") || requestURI.equals("/register") ||
            requestURI.startsWith("/css/") || requestURI.startsWith("/js/") ||
            requestURI.startsWith("/images/")) {
            return true;
        }

        // Check if user is logged in
        if (session == null || session.getAttribute("loggedInUser") == null) {
            response.sendRedirect("/login");
            return false;
        }

        return true;
    }
}
