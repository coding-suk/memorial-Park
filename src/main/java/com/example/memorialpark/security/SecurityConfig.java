package com.example.memorialpark.security;

import com.example.memorialpark.security.jwt.JwtAuthFilter;
import com.example.memorialpark.security.jwt.JwtUtil;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
public class SecurityConfig {

    @Bean
    public JwtUtil jwtUtil(@Value("${jwt.secret}") String secret,
                           @Value("${jwt.expiry-minutes}") long minutes) {
        return new JwtUtil(secret, minutes);
    }

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http, JwtUtil jwt) throws Exception{
        http.csrf(
                csrf -> csrf.disable()
        );
        http.authorizeHttpRequests(auth -> auth
                .requestMatchers("/auth/**", "/login.html", "/mypage.html", "/**/*.css", "/**/*.js").permitAll()
                .requestMatchers("/admin/**").hasRole("ADMIN")
                .anyRequest().authenticated()
        );
        http.addFilterBefore(new JwtAuthFilter(jwt), UsernamePasswordAuthenticationFilter.class);
        return http.build();
    }

}
