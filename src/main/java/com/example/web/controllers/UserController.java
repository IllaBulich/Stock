package com.example.web.controllers;

import com.example.web.models.User;
import com.example.web.services.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.security.Principal;


@Controller
@RequiredArgsConstructor
public class UserController {

    private final UserService userService;

    @GetMapping("/login")
    public String login() {
        return "user/login";
    }

    @GetMapping("/registration")
    public String registration() {
        return "user/registration";
    }


    @PostMapping("/registration")
    public String createUser(User user, Model model) {
        if (!userService.createUser(user)) {
            model.addAttribute("errorMassege", "пользователь с email" + user.getEmail() + "уже существует");
            return "user/registration";
        }
        userService.createUser(user);
        return "redirect:/login";
    }

    @GetMapping("/user/info/{user}")
    public String userInfo(@PathVariable("user") User user, Model model) {
        model.addAttribute("user", user);


        return "user/user-info";
    }

    @GetMapping("/user/account")
    public String userProducts(Principal principal, Model model) {
        User user = userService.getUserByPrincipal(principal);
        model.addAttribute("user", user);
        return "user/user-account";
    }

    @PostMapping("/user/{id}/remove")
    public String postImmovablesDelete(
            @PathVariable(value = "id") long id,
            Model model) {
        userService.deleteUser(id);
        return "redirect:/admin";
    }
}
