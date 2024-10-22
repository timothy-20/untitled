package com.timothy.controllers;

import com.timothy.entities.TKUserEntity;
import com.timothy.services.TKUserService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("login/api")
public class TKLoginApiController {
    private final TKUserService service;

    @Autowired
    public TKLoginApiController(TKUserService service) {
        super();
        this.service = service;
    }

    @PostMapping("/user-account")
    public ResponseEntity<Map<String, Object>> processLogin(HttpServletRequest request) {
        HttpHeaders headers = this.createDefaultHeaders();
        Map<String, Object> body = new HashMap<>();
        String baseURL = request.getScheme() + "://" + request.getServerName() + ":" + request.getServerPort();
        String id = request.getParameter("id");
        TKUserEntity user = this.service.getUserById(id);

        if (user == null) {
            // 사용자가 존재하지 않는 경우
            body.put("redirectUrl", baseURL + "/login/user-account");
            body.put("message", "사용자 계정을 찾을 수 없습니다.");
            return new ResponseEntity<>(body, headers, HttpStatus.UNPROCESSABLE_ENTITY);
        }

        String password = request.getParameter("password");

        if (!user.getPassword().equals(password)) {
            // 사용자의 비밀번호가 일치하지 않는 경우
            body.put("redirectUrl", baseURL + "/login/user-account");
            body.put("message", "비밀번호가 일치하지 않습니다.");
            return new ResponseEntity<>(body, headers, HttpStatus.UNAUTHORIZED);
        }

        body.put("redirectUrl", baseURL + "/login/complete");
        return new ResponseEntity<>(body, headers, HttpStatus.OK);
    }

    private HttpHeaders createDefaultHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setCacheControl(CacheControl.noCache());
        return headers;
    }
}
