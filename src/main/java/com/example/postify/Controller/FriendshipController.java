package com.example.postify.Controller;

import org.springframework.ui.Model;
import com.example.postify.Model.Friendship;
import com.example.postify.Model.User;
import com.example.postify.Service.FriendshipService;
import com.example.postify.Service.UserService;
import jakarta.servlet.http.HttpSession;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/friends")
public class FriendshipController {

    private final UserService userService;
    private final FriendshipService friendshipService;

    public FriendshipController(UserService userService, FriendshipService friendshipService) {
        this.userService = userService;
        this.friendshipService = friendshipService;
    }

    @GetMapping("/list")
    public String viewFriends(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<User> friends = friendshipService.getFriends(user);
            model.addAttribute("friends", friends);
            return "friend/friends_list";
        }
        return "redirect:/users/login";
    }

    @GetMapping("/requests")
    public String viewPendingRequests(Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<Friendship> pendingRequests = friendshipService.getPendingRequests(user);
            model.addAttribute("pendingRequests", pendingRequests);
            return "friend/friends_requests";
        }
        return "redirect:/users/login";
    }

    @PostMapping("/send-request")
    public String sendFriendRequest(@RequestParam String friendEmail, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/users/login";
        }

        User friend = userService.findByEmail(friendEmail);

        if (friend == null) {
            return "redirect:/friends/list";
        }

        friendshipService.sendFriendRequest(user, friend);

        return "redirect:/friends/requests";
    }

    @PostMapping("/accept-request/{id}")
    public String acceptFriendRequest(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/users/login";
        }

        friendshipService.acceptFriendRequest(id);

        return "redirect:/friends/requests";
    }

    @PostMapping("/decline-request/{id}")
    public String declineFriendRequest(@PathVariable Long id, HttpSession session) {
        User user = (User) session.getAttribute("user");

        if (user == null) {
            return "redirect:/users/login";
        }

        friendshipService.declineFriendRequest(id);

        return "redirect:/friends/requests";
    }

    @GetMapping("/search")
    public String showSearchPage() {
        return "friend/friends_search";
    }

    @GetMapping("/search-results")
    public String searchFriends(@RequestParam(value = "searchTerm", required = false) String searchTerm, Model model, HttpSession session) {
        User user = (User) session.getAttribute("user");
        if (user != null) {
            List<User> foundUsers = userService.searchByNameOrEmail(searchTerm);
            model.addAttribute("foundUsers", foundUsers);
        }
        return "friend/friends_search_results";
    }



}
