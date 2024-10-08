package com.example.postify.Controller;

import com.example.postify.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

import org.springframework.web.bind.annotation.*;

@Controller
@RequestMapping("/users")
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping("/register")
    public String registerView() {
        return "users/register";
    }

    @PostMapping("/register")
    public String register(@RequestParam String name,
                           @RequestParam String email,
                           @RequestParam String phoneNumber,
                           @RequestParam String password,
                           @RequestParam(required = false) String bio,
                           @RequestParam(required = false) String avatarUrl)
    {
        userService.registerUser(name, email, phoneNumber, password, bio, avatarUrl);
        return "redirect:/users/login"; // Перенаправление на страницу авторизации
    }



    @GetMapping("/login")
    public String showLoginForm() {
        return "users/login";
    }


}
