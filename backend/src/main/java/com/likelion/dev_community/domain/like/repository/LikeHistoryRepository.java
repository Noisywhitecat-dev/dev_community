package com.likelion.dev_community.domain.like.repository;

import com.likelion.dev_community.domain.like.entity.LikeHistory;
import com.likelion.dev_community.domain.like.entity.LikeTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeHistoryRepository extends JpaRepository<LikeHistory, Long> {

    Optional<LikeHistory> findByUserIdAndTargetTypeAndTargetId(
            Long userId, LikeTargetType targetType, Long targetId);
}
