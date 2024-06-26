package com.example.web.controllers;


import com.example.web.exception.ProductNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;

@ControllerAdvice
public class ExceptionController {
    @ExceptionHandler(ProductNotFoundException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public String handleYourSpecificException(ProductNotFoundException e, Model model) {
        model.addAttribute("error", e.getMessage());
        return "specificError";
    }


}
