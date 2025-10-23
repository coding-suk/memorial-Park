package com.example.memorialpark.web;

import jakarta.servlet.http.Cookie;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.servlet.http.HttpSession;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.UUID;
import java.net.URI;

@RestController
@RequestMapping("/auth")
public class FakeAuthController {

    @GetMapping("/login/fake")
    public void loginFake(HttpServletResponse res, HttpSession session) throws Exception{
        String state = UUID.randomUUID().toString();
        session.setAttribute("STATE", state);

        // 카카오를 흉내내서 곧바로 콜백으로 돌려보냄
        res.sendRedirect("/auth/callback/fake?code=abc123&state=" + state);
    }

    @GetMapping("callback/fake")
    public ResponseEntity<Void> callbackFake(String code, String state, HttpSession session, HttpServletResponse res) {
        String saved = (String) session.getAttribute("STATE");

        if(saved == null || !saved.equals(state)) {
            return ResponseEntity.badRequest().build();
        }

        // 아주 단순한 세션 쿠키(1단계: JWT는 다음 단계)
        Cookie c = new Cookie("X-SESSION-USER", "fake-1001");

        c.setHttpOnly(true);
        c.setPath("/");
        res.addCookie(c);

        return ResponseEntity.status(302).location(URI.create("/mypage.html")).build();
    }


}
