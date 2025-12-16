package com.hazerta.SpendwiseBack.repository;

import com.hazerta.SpendwiseBack.entity.UserFollow;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserFollowRepository extends JpaRepository<UserFollow, Long> {
    boolean existsByFollowerIdAndFollowedId(Long followerId, Long followedId);

    Optional<UserFollow> findByFollowerIdAndFollowedId(Long followerId, Long followedId);

    long countByFollowedId(Long followedId);
}
