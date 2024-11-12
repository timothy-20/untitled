package com.timothy.controllers;

import com.timothy.entities.TKUserEntity;
import com.timothy.services.TKUserService;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("login/api")
public class TKLoginApiController {
    private static final Logger LOGGER = LogManager.getRootLogger();
    private final TKUserService service;

    @PostMapping("/user-account")
    public ResponseEntity<Map<String, Object>> processLogin(
            HttpServletRequest request,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "password") String password) {
        if (id == null || id.isEmpty()) {
            LOGGER.warn("Unable to get user id.");
            return ResponseEntity.badRequest().body(Map.of("message", "아이디 입력이 유효하지 않습니다."));
        }

        TKUserEntity user = this.service.getUserById(id);

        if (user == null) {
            // 사용자가 존재하지 않는 경우
            LOGGER.warn("Fail to find user with id: {}", id);
            return ResponseEntity.badRequest().body(Map.of("message", "사용자 계정을 찾을 수 없습니다."));
        }

        if (password == null || password.isEmpty()) {

        }

        if (!user.getPassword().equals(password)) {
            // 사용자의 비밀번호가 일치하지 않는 경우
            return ResponseEntity.badRequest().body(Map.of("message", "비밀번호가 일치하지 않습니다."));
        }

        body.put("redirectUrl", baseURL + "/login/complete");
        return new ResponseEntity<>(body, headers, HttpStatus.OK);

//        return ResponseEntity.status(HttpStatus.SEE_OTHER)
//                .header(HttpHeaders.LOCATION, "/register/finish")
//                .build();
    }
}
