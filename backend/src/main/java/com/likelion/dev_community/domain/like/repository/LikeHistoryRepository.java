package com.likelion.dev_community.domain.like.repository;

import com.likelion.dev_community.domain.like.entity.LikeHistory;
import com.likelion.dev_community.domain.like.entity.LikeTargetType;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeHistoryRepository extends JpaRepository<LikeHistory, Long> {

    // LikeServiceImpl의 toggleLike에서 사용 — 이미 눌렀는지 확인하는 조회.
    // 이 조회 자체는 동시성에서 최종 방어선이 아니다(레이스 컨디션 가능).
    // 최종 방어선은 LikeHistory 엔티티의 UNIQUE(user_id, target_type, target_id) 제약.
    Optional<LikeHistory> findByUserIdAndTargetTypeAndTargetId(
            Long userId, LikeTargetType targetType, Long targetId);
}
