package com.timothy.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class TKGlobalExceptionHandler {
    private static final Logger LOGGER = LogManager.getRootLogger();

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException exception, Model model) {
        LOGGER.error(exception);
        model.addAttribute("errorCode", exception.getStatusCode());
        model.addAttribute("errorMessage", exception.getReason());
        return "TKGlobalErrorView";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception exception, Model model) {
        LOGGER.error(this.getStackTraceAsString(exception));
        model.addAttribute("errorCode", "예상치 못한 문제 발생");
        model.addAttribute("errorMessage", "서버 내부에 예상치 못한 문제가 발생했습니다. 자세한 사항은 관리자에게 문의해주세요.");
        return "TKGlobalErrorView";
    }

    private String getStackTraceAsString(Exception exception) {
        StringBuilder stringBuilder = new StringBuilder();
        stringBuilder.append(exception.toString()).append("\n");

        for (StackTraceElement stackTraceElement : exception.getStackTrace()) {
            stringBuilder.append(stackTraceElement.toString()).append("\n");
        }

        return stringBuilder.toString();
    }
}
