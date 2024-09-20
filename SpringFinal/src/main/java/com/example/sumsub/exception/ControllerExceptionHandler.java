package com.example.sumsub.exception;
import lombok.extern.slf4j.Slf4j;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
@Slf4j
public class ControllerExceptionHandler {

    @ExceptionHandler(Exception.class)
    public String HandleExceptions(Exception exception, Model model){
        log.error(exception.getMessage());
        model.addAttribute("error", exception.getMessage());
        return "error";
    }
}
