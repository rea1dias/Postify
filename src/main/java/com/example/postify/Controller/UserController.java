package com.example.postify.Controller;

import org.springframework.ui.Model;
import com.example.postify.Model.User;
import com.example.postify.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;

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
        return "redirect:/users/login";
    }


    @GetMapping("/login")
    public String loginView() {
        return "users/login";
    }

    @PostMapping("/login")
    public String login(@RequestParam String email,
                        @RequestParam String password,
                        Model model,
                        HttpSession session) {
        User user = userService.findByEmail(email);
        if (user != null && user.getPassword().equals(password)) {
            session.setAttribute("user", user);
            return "redirect:/";
        } else {
            model.addAttribute("error", "Invalid email or password");
            return "users/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam("token") String token) {
        boolean isConfirmed = userService.confirmUserEmail(token);
        if (isConfirmed) {
            return "index";
        } else {
            return "users/register";
        }
    }


}
