package com.example.postify.Service;

import com.example.postify.Model.User;
import com.example.postify.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.postify.Model.User;




import java.io.IOException;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendGridEmailService emailService;

    private String generateConfirmationToken() {
        return UUID.randomUUID().toString(); // Генерация уникального токена
    }

    public void registerUser(String name, String email, String phoneNumber, String password, String bio, String avatarUrl) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);

        userRepository.save(user);

        String token = generateConfirmationToken();
        user.setConfirmationToken(token);

        userRepository.save(user);

        String confirmationLink = "http://localhost:8080/confirm?token=" + token;

        try {
            emailService.sendConfirmationEmail(user.getEmail(), confirmationLink);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public User findByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    public boolean confirmUserEmail(String token) {
        User user = userRepository.findByConfirmationToken(token);
        if (user != null) {
            user.setEmailConfirmed(true);
            userRepository.save(user);
            return true;
        }
        return false;
    }



}
