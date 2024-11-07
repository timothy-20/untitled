package com.timothy.controllers;

import com.timothy.entities.TKUserEntity;
import com.timothy.models.TKRealName;
import com.timothy.services.TKUserService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;
import lombok.RequiredArgsConstructor;
import org.springframework.http.*;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.net.URI;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;

@RequiredArgsConstructor
@RestController
@RequestMapping("/register/api")
public class TKRegisterApiController {
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
        DateTimeFormatter dateTimeFormat = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        HttpSession session = request.getSession(false);

        if (session != null) {
            session.setAttribute("birthday", LocalDate.parse(birthday, dateTimeFormat));
            session.setAttribute("gender", gender);
        }

        return new ResponseEntity<>(this.createRedirectHeaders(URI.create("/register/third-step")), HttpStatus.SEE_OTHER);
    }

    @PostMapping("/third-step")
    public ResponseEntity<Void> processThirdStep(
            HttpServletRequest request,
            TKUserService service,
            @RequestParam(value = "id") String id,
            @RequestParam(value = "password") String password
    ) {
        HttpSession session = request.getSession(false);

        if (session == null) {
            throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "회원가입 세션이 유효하지 않습니다");
        }

        String realName = (String)session.getAttribute("realName");
        String nickname = (String)session.getAttribute("nickname");
        LocalDate birthday = (LocalDate)session.getAttribute("birthday");
        String gender = (String)session.getAttribute("gender");

        if (realName == null || nickname == null || birthday == null || gender == null || id == null || password == null) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "회원가입에 필요한 사용자 정보가 일부 누락되었습니다.");
        }

        TKUserEntity userEntity = new TKUserEntity();
        userEntity.setRealName(realName);
        userEntity.setNickname(nickname);
        userEntity.setBirthday(birthday);
        userEntity.setGender(gender);
        userEntity.setId(id);
        userEntity.setPassword(password);
        service.insertUser(userEntity);
        return new ResponseEntity<>(this.createRedirectHeaders(URI.create("/register/finish")), HttpStatus.SEE_OTHER);
    }

    private HttpHeaders createRedirectHeaders(URI targetURI) {
        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(targetURI);
        headers.setCacheControl(CacheControl.noStore());
        return headers;
    }
}
