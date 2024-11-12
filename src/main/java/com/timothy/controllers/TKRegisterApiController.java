package com.timothy.controllers;

import com.timothy.entities.TKUserEntity;
import com.timothy.models.TKRealName;
import com.timothy.services.TKUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.*;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RequiredArgsConstructor
@RestController
@RequestMapping("/register/api")
public class TKRegisterApiController {
    private static final Logger LOGGER = LogManager.getRootLogger();
    private final TKUserService service;
    private boolean isIdDuplicate = true;

    @PostMapping("/first-step")
    public ResponseEntity<Void> processFirstStep(
            HttpServletRequest request,
            @RequestParam(value = "lastName", required = false) String lastName,
            @RequestParam(value = "firstName") String firstName,
            @RequestParam(value = "nickname", required = false) String nickname
    ) {
        TKRealName realName = new TKRealName(firstName);

        if (lastName != null && !lastName.isEmpty()) {
            realName.setLastname(lastName);
        }

        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute("realName", realName.toString());
            session.setAttribute("nickname", nickname);
        }

        return new ResponseEntity<>(this.createRedirectHeaders(URI.create("/register/second-step")), HttpStatus.SEE_OTHER);
    }

    @PostMapping("/second-step")
    public ResponseEntity<Void> processSecondStep(
            HttpServletRequest request,
            @RequestParam(value = "birthday") String birthday,
            @RequestParam(value = "gender") String gender
    ) {
        HttpSession session = request.getSession(false);

        if (session != null) {
            DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
            session.setAttribute("birthday", LocalDate.parse(birthday, dateTimeFormat));
            session.setAttribute("gender", gender);
        }

        return new ResponseEntity<>(this.createRedirectHeaders(URI.create("/register/third-step")), HttpStatus.SEE_OTHER);
    }

    @PostMapping("/third-step/check-id-duplication")
    public ResponseEntity<Map<String, Object>> checkIdDuplication(
            HttpServletRequest request,
            @RequestBody Map<String, Object> payload
    ) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            LOGGER.error("Registration session is invalid. Fail to request /third-step/check-id-duplication.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다");
        }

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

        this.isIdDuplicate = false;
        return ResponseEntity.ok(Map.of("message", "사용 가능한 아이디입니다."));
    }

    @PostMapping("/third-step")
    public ResponseEntity<Void> processThirdStep(
            HttpServletRequest request,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "password") String password
    ) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            LOGGER.error("Registration session is invalid. Fail to request /third-step.");
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다");
        }

        String realName = (String)session.getAttribute("realName");
        String nickname = (String)session.getAttribute("nickname");
        LocalDate birthday = (LocalDate)session.getAttribute("birthday");
        String gender = (String)session.getAttribute("gender");

        if (realName == null || nickname == null || birthday == null || gender == null || id == null || password == null) {
            LOGGER.error("Registration information is not completely. Fail to request /third-step.");
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입에 필요한 사용자 정보가 일부 누락되었습니다.");
        }

        if (this .service.getUserById(id) != null) {
            // 사용자 아이디 중복 체크
            LOGGER.warn("User already exists : {}. Fail to request /third-step.", id);
            session.setAttribute("idErrorMessage", "이미 사용중인 아이디입니다.");
            return ResponseEntity.status(HttpStatus.SEE_OTHER)
                    .header(HttpHeaders.LOCATION, "/register/third-step")
                    .build();
        }

        TKUserEntity userEntity = new TKUserEntity();
        userEntity.setRealName(realName);
        userEntity.setNickname(nickname);
        userEntity.setBirthday(birthday);
        userEntity.setGender(gender);
        userEntity.setId(id);
        userEntity.setPassword(password);
        this.service.insertUser(userEntity);
        return new ResponseEntity<>(this.createRedirectHeaders(URI.create("/register/finish")), HttpStatus.SEE_OTHER);
    }

    private HttpHeaders createRedirectHeaders(URI targetURI) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(targetURI);
        headers.setCacheControl(CacheControl.noStore());
        return headers;
    }
}
