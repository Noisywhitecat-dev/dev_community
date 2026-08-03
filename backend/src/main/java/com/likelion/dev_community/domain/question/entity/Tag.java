package com.likelion.dev_community.domain.question.entity;

import com.likelion.dev_community.common.entity.BaseTimeEntity;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

// ⑥ 결정: Tag, LikeHistory도 BaseTimeEntity를 상속한다.
// 수정될 일이 없는 엔티티라 updatedAt이 항상 createdAt과 같겠지만,
// "시간 관련 필드는 전부 BaseTimeEntity에서 온다"는 규칙을 예외 없이 유지하는 편이
// 4명이 각자 다른 엔티티를 만들 때 헷갈리지 않는다. 컬럼 하나 더 생기는 비용보다
// 일관성의 이득이 크다고 판단.
@Getter
@Entity
@Table(name = "tags")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Tag extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true, length = 50)
    private String name;

    @Builder
    public Tag(String name) {
        this.name = name;
    }
}
