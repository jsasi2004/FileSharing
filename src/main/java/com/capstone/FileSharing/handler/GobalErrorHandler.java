package com.capstone.FileSharing.handler;

import org.springframework.boot.web.servlet.error.ErrorController;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@ControllerAdvice
public class GobalErrorHandler implements ErrorController {

    @ExceptionHandler(IllegalArgumentException.class)
    public String handle() {
        return "error";
    }
    
}
