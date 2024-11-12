package com.timothy.controllers;

import com.timothy.entities.TKUserEntity;
import com.timothy.models.TKRealName;
import com.timothy.services.TKUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/register/api")
public class TKRegisterApiController {
    private static final Logger LOGGER = LogManager.getRootLogger();
    private final TKUserService service;

    @PostMapping("/first-step")
    public ResponseEntity<Void> processFirstStep(
            HttpServletRequest request,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "nickname", required = false) String nickname) {
        TKRealName realName = new TKRealName(firstName);

        if (lastName != null && !lastName.isEmpty()) {
            realName.setLastname(lastName);
        }

        TKUserEntity user = (TKUserEntity)request.getSession(false).getAttribute("registrationUserEntity");
        user.setRealName(realName.toString());
        user.setNickname(nickname);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, "/register/second-step")
                .build();
    }

    @PostMapping("/second-step")
    public ResponseEntity<Void> processSecondStep(
            HttpServletRequest request,
            @RequestParam(value = "birthday") String birthday,
            @RequestParam(value = "gender") String gender) {
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        TKUserEntity user = (TKUserEntity)request.getSession(false).getAttribute("registrationUserEntity");
        user.setBirthday(LocalDate.parse(birthday, dateTimeFormat));
        user.setGender(gender);
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, "/register/third-step")
                .build();
    }

    @PostMapping("/third-step")
    public ResponseEntity<Void> processThirdStep(
            HttpServletRequest request,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "password") String password) {
        TKUserEntity user = (TKUserEntity)request.getSession(false).getAttribute("registrationUserEntity");

        if (user.getRealName() == null || user.getBirthday() == null || user.getGender() == null || id == null || password == null) {
            LOGGER.error("Registration information is not completely. Fail to request /third-step.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입에 필요한 사용자 정보가 일부 누락되었습니다.");
        }

        user.setId(id);
        user.setPassword(password);
        this.service.insertUser(user);
        user.setPassword("");
        return ResponseEntity.status(HttpStatus.SEE_OTHER)
                .header(HttpHeaders.LOCATION, "/register/finish")
                .build();
    }

    @PostMapping("/third-step/check-id-duplication")
    public ResponseEntity<Map<String, Object>> checkIdDuplication(@RequestBody Map<String, Object> payload) {
        String id = (String)payload.get("id");

        if (id == null || id.isEmpty()) {
            LOGGER.warn("Unable to get user id. Fail to request /third-step/check-id-duplication.");
            return ResponseEntity.badRequest().body(Map.of("message", "사용자 아이디 값이 없습니다."));
        }

        if (this.service.getUserById(id) != null) {
            // 사용자 아이디 중복 체크
            LOGGER.warn("User already exists : {}. Fail to request /third-step/check-id-duplication.", id);
            return ResponseEntity.badRequest().body(Map.of("message", "이미 사용중인 아이디입니다."));
        }

        return ResponseEntity.ok(Map.of("message", "사용 가능한 아이디입니다."));
    }
}
