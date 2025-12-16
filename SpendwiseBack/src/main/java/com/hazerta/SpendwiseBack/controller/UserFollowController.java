package com.hazerta.SpendwiseBack.controller;

import com.hazerta.SpendwiseBack.service.UserFollowService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserFollowController {

    private final UserFollowService userFollowService;

    public UserFollowController(UserFollowService userFollowService) {
        this.userFollowService = userFollowService;
    }

    @PostMapping("/{followerId}/follow/{followedId}")
    public ResponseEntity<?> follow(@PathVariable Long followerId, @PathVariable Long followedId) {
        userFollowService.follow(followerId, followedId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping("/{followerId}/unfollow/{followedId}")
    public ResponseEntity<?> unfollow(@PathVariable Long followerId, @PathVariable Long followedId) {
        userFollowService.unfollow(followerId, followedId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/{userId}/followers/count")
    public ResponseEntity<Long> countFollowers(@PathVariable Long userId) {
        long count = userFollowService.countFollowers(userId);
        return ResponseEntity.ok(count);
    }

    @GetMapping("/{followerId}/isFollowing/{followedId}")
    public ResponseEntity<Boolean> isFollowing(@PathVariable Long followerId, @PathVariable Long followedId) {
        boolean following = userFollowService.isFollowing(followerId, followedId);
        return ResponseEntity.ok(following);
    }
}
