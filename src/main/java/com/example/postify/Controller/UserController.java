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

        if (user != null) {
            if (user.getPassword().equals(password)) {
                if (!user.isEmailConfirmed()) {
                    model.addAttribute("error", "Please confirm your email before logging in.");
                    return "users/login";
                }
                session.setAttribute("user", user);
                return "redirect:/";
            } else {
                model.addAttribute("error", "Invalid email or password.");
                return "users/login";
            }
        } else {
            model.addAttribute("error", "Invalid email or password.");
            return "users/login";
        }
    }

    @GetMapping("/logout")
    public String logout(HttpSession session) {
        session.invalidate();
        return "redirect:/users/login";
    }

    @GetMapping("/confirm")
    public String confirmEmail(@RequestParam("token") String token, Model model) {
        boolean isConfirmed = userService.confirmUserEmail(token);
        if (isConfirmed) {
            return "users/emailConfirm/confirm_success";
        } else {
            return "users/emailConfirm/confirm_error";
        }
    }

    @GetMapping("/profile")
    public String viewProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "users/profile";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/profile/edit")
    public String editProfile(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            model.addAttribute("user", user);
            return "users/profile_edit";
        }
        return "redirect:/users/login";
    }



    @PostMapping("/profile/edit")
    public String updateProfile(@RequestParam String name,
                                @RequestParam String email,
                                @RequestParam String phoneNumber,
                                @RequestParam(required = false) String bio,
                                @RequestParam(required = false) String avatarUrl,
                                HttpSession session, Model model) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            boolean updated = userService.updateUserProfile(user.getId(), name, email, phoneNumber, bio, avatarUrl);

            if (updated) {
                user.setName(name);
                user.setEmail(email);
                user.setPhoneNumber(phoneNumber);
                user.setBio(bio);
                user.setAvatarUrl(avatarUrl);
                session.setAttribute("user", user);
                model.addAttribute("success", "Profile updated successfully.");
                return "redirect:/users/profile";
            } else {
                model.addAttribute("error", "User not Found");
            }
        } else {
            model.addAttribute("error", "You need to log in to update your profile.");
        }
        return "users/profile";
    }


}
