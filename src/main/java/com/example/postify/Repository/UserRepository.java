package com.example.postify.Repository;

import com.example.postify.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface UserRepository extends JpaRepository<User, Long> {

    User findByEmail(String email);

    User findByConfirmationToken(String token);

    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(String name, String email);

    @Query("SELECT u FROM User u WHERE (u.name LIKE %:name% OR u.email LIKE %:email%) AND u.id != :currentUserId")
    List<User> findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(@Param("name") String name, @Param("email") String email, @Param("currentUserId") Long currentUserId);


    List<User> findByNameContainingOrEmailContaining(String searchTerm, String searchTerm2);








}
