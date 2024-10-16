package com.example.postify.Service;

import com.example.postify.Model.User;
import com.example.postify.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.postify.Model.User;
import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private SendGridEmailService emailService;

    private String generateConfirmationToken() {
        return UUID.randomUUID().toString();
    }

    public void registerUser(String name, String email, String phoneNumber, String password, String bio, String avatarUrl) {
        User user = new User();
        user.setName(name);
        user.setEmail(email);
        user.setPhoneNumber(phoneNumber);
        user.setPassword(password);
        user.setBio(bio);
        user.setAvatarUrl(avatarUrl);

        String token = generateConfirmationToken();
        user.setConfirmationToken(token);
        user.setEmailConfirmed(false);

        userRepository.save(user);

        String confirmationLink = "http://localhost:8080/users/confirm?token=" + token;

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

    public boolean updateUserProfile(Long userId, String name, String email, String phoneNumber, String bio, String avatarUrl) {
        Optional<User> userOptional = userRepository.findById(userId);
        if (userOptional.isPresent()) {
            User user = userOptional.get();
            user.setName(name);
            user.setEmail(email);
            user.setPhoneNumber(phoneNumber);
            user.setBio(bio);
            user.setAvatarUrl(avatarUrl);
            try {
                userRepository.save(user);
                return true;
            } catch (Exception e) {
                e.printStackTrace();
                return false;
            }
        }
        return false;
    }

    public List<User> searchUsers(String query, Long currentUserId) {
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(query, query, currentUserId);
    }


    public List<User> searchByNameOrEmail(String searchTerm) {
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(searchTerm, searchTerm);
    }



}
