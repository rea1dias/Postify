package com.example.postify.Service;


import com.example.postify.Model.Friendship;
import com.example.postify.Model.User;
import com.example.postify.Repository.FriendshipRepository;
import com.example.postify.Repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class FriendshipService {

    @Autowired
    private FriendshipRepository friendshipRepository;

    @Autowired
    private UserRepository userRepository;


    public void sendFriendRequest(User user, User friend) {
        Friendship friendship = new Friendship();
        friendship.setUser(user);
        friendship.setFriend(friend);
        friendship.setAccepted(false);
        friendshipRepository.save(friendship);
    }

    public void acceptFriendRequest(Long friendshipId) {
        Optional<Friendship> friendshipOptional = friendshipRepository.findById(friendshipId);
        if (friendshipOptional.isPresent()) {
            Friendship friendship = friendshipOptional.get();
            friendship.setAccepted(true);
            friendshipRepository.save(friendship);
        }
    }

    public void declineFriendRequest(Long friendshipId) {
        friendshipRepository.deleteById(friendshipId);
    }

    public List<User> getFriends(User user) {
        List<Friendship> friendships = friendshipRepository.findByUserAndAccepted(user, true);
        List<User> friends = new ArrayList<>();
        for (Friendship friendship : friendships) {
            friends.add(friendship.getFriend());
        }
        return friends;
    }

    public List<Friendship> getPendingRequests(User user) {
        return friendshipRepository.findByFriendAndAccepted(user, false);
    }

    public List<User> searchByNameOrEmail(String name, String email) {
        return userRepository.findByNameContainingIgnoreCaseOrEmailContainingIgnoreCase(name, email);
    }





}
