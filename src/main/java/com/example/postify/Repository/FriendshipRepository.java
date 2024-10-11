package com.example.postify.Repository;

import com.example.postify.Model.Friendship;
import com.example.postify.Model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface FriendshipRepository extends JpaRepository<Friendship, Long> {

    List<Friendship> findByUserAndAccepted(User user, boolean accepted);
    List<Friendship> findByFriendAndAccepted(User friend, boolean accepted);






}
