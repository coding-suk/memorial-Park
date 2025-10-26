package com.example.memorialpark.security;

import com.example.memorialpark.security.jwt.JwtAuthFilter;
import com.example.memorialpark.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.security.servlet.PathRequest;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class Security {

    @Bean
    public JwtUtil jwtUtil(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.expiry-minutes}") long minutes) {
        return new JwtUtil(secret, minutes);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwt) throws Exception {
        http.csrf(csrf -> csrf.disable());
        http.authorizeHttpRequests(auth -> auth
                // 1) 정적 리소스: 안전하게 한 줄로
                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
                // 2) 공개 엔드포인트
                .requestMatchers("/auth/**", "/login.html", "/mypage.html", "/adminpage.html").permitAll()
                // 3) 관리자
                .requestMatchers("/admin/**").hasRole("ADMIN")
                // 4) 나머지는 인증 필요
                .anyRequest().authenticated()
        );

        // 커스텀 필터 잠시 비활성화
         http.addFilterBefore(new JwtAuthFilter(jwt), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }
}

