package com.hazerta.SpendwiseBack.service;

import com.hazerta.SpendwiseBack.entity.User;
import com.hazerta.SpendwiseBack.entity.UserFollow;
import com.hazerta.SpendwiseBack.repository.UserFollowRepository;
import com.hazerta.SpendwiseBack.repository.UserRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserFollowService {

    private final UserFollowRepository userFollowRepository;
    private final UserRepository userRepository;

    public UserFollowService(UserFollowRepository userFollowRepository, UserRepository userRepository) {
        this.userFollowRepository = userFollowRepository;
        this.userRepository = userRepository;
    }

    @Transactional
    public void follow(Long followerId, Long followedId) {
        if (followerId.equals(followedId)) {
            throw new IllegalArgumentException("User cannot follow themselves");
        }
        // Ensure both users exist
        User follower = userRepository.findById(followerId)
                .orElseThrow(() -> new IllegalArgumentException("Follower not found"));
        User followed = userRepository.findById(followedId)
                .orElseThrow(() -> new IllegalArgumentException("User to follow not found"));
        // Check if already following
        if (userFollowRepository.existsByFollowerIdAndFollowedId(followerId, followedId)) {
            throw new IllegalStateException("Already following this user");
        }
        UserFollow follow = new UserFollow();
        follow.setFollower(follower);
        follow.setFollowed(followed);
        userFollowRepository.save(follow);
    }

    @Transactional
    public void unfollow(Long followerId, Long followedId) {
        UserFollow follow = userFollowRepository.findByFollowerIdAndFollowedId(followerId, followedId)
                .orElseThrow(() -> new IllegalArgumentException("Follow relationship does not exist"));
        userFollowRepository.delete(follow);
    }

    public boolean isFollowing(Long followerId, Long followedId) {
        return userFollowRepository.existsByFollowerIdAndFollowedId(followerId, followedId);
    }

    public long countFollowers(Long userId) {
        return userFollowRepository.countByFollowedId(userId);
    }
}
