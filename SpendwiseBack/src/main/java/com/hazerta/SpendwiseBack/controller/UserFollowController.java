package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.service.UserFollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserFollowController {

    private final UserFollowService userFollowService;

    public UserFollowController(UserFollowService userFollowService) {
        this.userFollowService = userFollowService;
    }

    @PostMapping("/follow/{followedId}")
    public ResponseEntity<?> follow(@PathVariable Long followedId, Authentication authentication) {
        userFollowService.follow(authentication.getName(), followedId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/unfollow/{followedId}")
    public ResponseEntity<?> unfollow(@PathVariable Long followedId, Authentication authentication) {
        userFollowService.unfollow(authentication.getName(), followedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> countFollowers(@PathVariable Long userId) {
        long count = userFollowService.countFollowers(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/isFollowing/{followedId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long followedId, Authentication authentication) {
        boolean following = userFollowService.isFollowing(authentication.getName(), followedId);
        return ResponseEntity.ok(following);
    }
}
