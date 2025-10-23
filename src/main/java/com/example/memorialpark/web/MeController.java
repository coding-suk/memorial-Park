package com.example.memorialpark.web;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CookieValue;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/auth")
public class MeController {

    @GetMapping("/me")
    public ResponseEntity<?> me(@CookieValue(value="X-SESSION-USER", required=false) String uid) {

        if(uid == null) {
            return ResponseEntity.status(401).build();
        }

        return ResponseEntity.ok(Map.of(
                "id", uid,
                "name", "테스트 사용자",
                "role", "USER"
        ));

    }

}
