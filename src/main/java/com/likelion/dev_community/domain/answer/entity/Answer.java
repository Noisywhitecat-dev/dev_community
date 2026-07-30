package com.likelion.dev_community.domain.answer.entity;

import com.likelion.dev_community.common.entity.BaseTimeEntity;
import com.likelion.dev_community.domain.answer.exception.AdoptedAnswerDeletionException;
import com.likelion.dev_community.domain.question.entity.Question;
import com.likelion.dev_community.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;

@Getter
@Entity
@Table(name = "answers")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Answer extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private boolean isAdopted;

    @Column(nullable = false)
    private int likeCount;

    private LocalDateTime deletedAt;

    @Builder
    public Answer(Question question, User author, String content) {
        this.question = question;
        this.author = author;
        this.content = content;
        this.isAdopted = false;
        this.likeCount = 0;
    }

    // ── C가 호출: 답변 수정 (F-13) ──────────────────────────
    public void update(String content) {
        this.content = content;
    }

    // ── C가 호출: soft delete (F-13) ────────────────────────
    // ⑥ 정책 결정: 채택된 답변은 삭제할 수 없다.
    // 이유: 삭제를 허용하면 "채택된 답변이 사라진 해결된 질문"이라는
    // 눈에 보이는 데이터 상태가 생기고, 질문 상태를 되돌릴지 말지까지
    // 추가로 결정해야 한다. 삭제를 막는 쪽이 규칙이 가장 단순하고,
    // 답변자가 정말 지우고 싶다면 "채택 취소 → 삭제"로 두 단계를 거치면 된다.
    public void softDelete() {
        if (this.isAdopted) {
            throw new AdoptedAnswerDeletionException();
        }
        this.deletedAt = LocalDateTime.now();
    }

    // ── C가 호출: 채택 (F-14) ───────────────────────────────
    public void adopt() {
        this.isAdopted = true;
    }

    public void cancelAdoption() {
        this.isAdopted = false;
    }

    // ── D가 호출: 추천 증감 (F-16) ──────────────────────────
    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount = Math.max(0, this.likeCount - 1);
    }
}
