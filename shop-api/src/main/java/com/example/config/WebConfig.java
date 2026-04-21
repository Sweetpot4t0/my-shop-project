package com.example.config;

import com.example.LoginCheckInterceptor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    // BCrypt 암호화 빈 처리
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }
    // ✅ 스프링 시큐리티의 기본 로그인 화면을 무력화시키는 설정
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable()) // 테스트 편의를 위해 CSRF 끄기
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll() // 모든 요청을 일단 허용 (우리가 만든 인터셉터가 검사할 거니까!)
                )
                .formLogin(login -> login.disable()) // 기본 로그인 폼 끄기
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new LoginCheckInterceptor())
                .addPathPatterns("/**")
                .excludePathPatterns(
                        "/",
                        "/index.html",
                        "/login.html",
                        "/api/member/login",
                        "/api/member/current-info",
                        "/css/**",
                        "/js/**",
                        "/*.ico",
                        "/error",
                        "/api/member/current",
                        "/products",
                        "/signup.html",
                        "/members/check-id",
                        "/api/member/check-id",
                        "/api/member/signup"
                );
    }
}