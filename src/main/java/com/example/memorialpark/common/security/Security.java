package com.example.memorialpark.common.security;

import com.example.memorialpark.common.OAuth2SuccessHandler;
import com.example.memorialpark.common.security.jwt.JwtAuthFilter;
import com.example.memorialpark.common.security.jwt.JwtUtil;
import com.example.memorialpark.web.service.CustomOAuth2UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.Customizer;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class Security {

    private JwtUtil jwtUtil;
    private CustomOAuth2UserService oAuth2UserService;
    private OAuth2SuccessHandler oAuth2SuccessHandler;

    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .csrf(csrf -> csrf.disable())
                .cors(Customizer.withDefaults())
                .sessionManagement(sm -> sm.sessionCreationPolicy(SessionCreationPolicy.STATELESS))
                .authorizeHttpRequests(
                        auth -> auth
                                .requestMatchers(
                                        "/health", "/public/**",
                                        "/oauth2/**", "/login/**",
                                        "/login/oauth2/code/**"
                                ).permitAll()
                                .requestMatchers(HttpMethod.GET, "/products/**").permitAll()
//                .authorizeHttpRequests(auth -> auth
//                // 1) 정적 리소스: 안전하게 한 줄로
//                .requestMatchers(PathRequest.toStaticResources().atCommonLocations()).permitAll()
//                // 2) 공개 엔드포인트
//                .requestMatchers("/auth/**", "/login.html", "/mypage.html", "/adminpage.html").permitAll()
//                // 3) 관리자
//                .requestMatchers("/admin/**").hasRole("ADMIN")
//                // 4) 나머지는 인증 필요
//                .anyRequest().authenticated()
                )
                .oauth2Login(oauth -> oauth
                        .userInfoEndpoint(u -> u.userService(oAuth2UserService))
                        .successHandler(oAuth2SuccessHandler)
                );
        http. addFilterBefore(new JwtAuthFilter(jwtUtil),
                UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }
}

