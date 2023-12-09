package com.example.web.controllers;

import com.example.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.security.Principal;

@Controller
@RequiredArgsConstructor
public class MainController {

    private final UserService userService;

    @GetMapping("/")
    public String home(
            Principal principal,
            Model model){
        model.addAttribute("user",userService.getUserByPrincipal(principal));
        return "home";
    }
}
