package com.timothy.controllers;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.server.ResponseStatusException;

@ControllerAdvice
public class TKGlobalExceptionHandler {
    private static final Logger logger = LogManager.getLogger(TKGlobalExceptionHandler.class);

    @ExceptionHandler(ResponseStatusException.class)
    public String handleResponseStatusException(ResponseStatusException exception, Model model) {
        logger.info("Test log...");
        model.addAttribute("errorCode", exception.getStatusCode());
        model.addAttribute("errorMessage", exception.getReason());
        return "TKGlobalErrorView";
    }

    @ExceptionHandler(Exception.class)
    public String handleGenericException(Exception exception, Model model) {
        model.addAttribute("log", this.getStackTraceAsString(exception));
        return "";
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
