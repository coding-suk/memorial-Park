package com.example.memorialpark.security.jwt;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.List;

public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwt;

    public JwtAuthFilter(JwtUtil jwt) {
        this.jwt = jwt;
    }

    @Override
    public void doFilterInternal(HttpServletRequest req, HttpServletResponse res, FilterChain chain)
        throws IOException, ServletException {

        try {
            String token = null;
            if(req.getCookies() != null) {
                for(Cookie c : req.getCookies()) {
                    if("AUTH".equals(c.getName())) {
                        token = c.getValue();
                    }
                }
            }

            if(token != null) {
                var jws = jwt.parse(token);
                String userId = jws.getBody().getSubject();
                String role = jws.getBody().get("role", String.class);
                var auth = new UsernamePasswordAuthenticationToken(
                        userId, null, List.of(new SimpleGrantedAuthority("ROLE_"+role)));
                SecurityContextHolder.getContext().setAuthentication(auth);
            }
        } catch (Exception ignored) { /* 무효 토큰을 비인증 처리*/
            chain.doFilter(req, res);
        }
    }
}
