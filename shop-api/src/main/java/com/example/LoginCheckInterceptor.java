package com.example;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.web.servlet.HandlerInterceptor;

public class LoginCheckInterceptor implements HandlerInterceptor {
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        HttpSession session = request.getSession(false);

        // 로그인 안 된 경우 로그인 페이지로 강제 이동
        if (session == null || session.getAttribute("loginMember") == null) {
            System.out.println(" 인터셉터가 막아버린 주소: " + request.getRequestURI());
            response.sendRedirect("/login.html");
            return false;
        }
        return true;
    }
}