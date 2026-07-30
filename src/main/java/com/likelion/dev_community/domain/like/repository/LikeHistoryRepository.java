package com.likelion.dev_community.domain.like.repository;

import com.likelion.dev_community.domain.like.entity.LikeHistory;
import org.springframework.data.jpa.repository.JpaRepository;

public interface LikeHistoryRepository extends JpaRepository<LikeHistory, Long> {
}
