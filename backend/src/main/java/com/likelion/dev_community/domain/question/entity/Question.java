package com.likelion.dev_community.domain.question.entity;

import com.likelion.dev_community.common.entity.BaseTimeEntity;
import com.likelion.dev_community.domain.user.entity.User;
import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.SQLRestriction;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Getter
@Entity
@Table(name = "questions")
@SQLRestriction("deleted_at is null")
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Question extends BaseTimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "author_id", nullable = false)
    private User author;

    @Column(nullable = false, length = 100)
    private String title;

    @Column(nullable = false, columnDefinition = "TEXT")
    private String content;

    @Column(nullable = false)
    private int viewCount;

    @Column(nullable = false)
    private int likeCount;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, length = 20)
    private QuestionStatus status;

    private LocalDateTime deletedAt;

    @OneToMany(mappedBy = "question", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<QuestionTag> questionTags = new ArrayList<>();

    @Builder
    public Question(User author, String title, String content) {
        this.author = author;
        this.title = title;
        this.content = content;
        this.viewCount = 0;
        this.likeCount = 0;
        this.status = QuestionStatus.OPEN;
    }

    // ── B가 호출: 질문 수정 (F-09) ──────────────────────────
    public void update(String title, String content) {
        this.title = title;
        this.content = content;
    }

    // ── B가 호출: soft delete (F-09, F-11) ─────────────────
    // 하위 답변 비활성화는 Question이 알 필요 없는 별도 책임이라
    // 서비스 계층(QuestionService)에서 AnswerRepository를 통해 처리한다.
    public void softDelete() {
        this.deletedAt = LocalDateTime.now();
    }

    // ── C가 호출: 답변 채택 시 질문 상태 전환 (F-14) ────────
    public void resolve() {
        this.status = QuestionStatus.RESOLVED;
    }

    // ── C가 호출: 채택 취소 시 되돌리기 (선택 정책, 답변 삭제 정책과 연동) ──
    public void reopen() {
        this.status = QuestionStatus.OPEN;
    }

    // ── B가 호출: 상세 조회 시 조회수 증가 (F-08) ──────────
    public void increaseViewCount() {
        this.viewCount++;
    }

    // ── D가 호출: 추천 증감 (F-16) ──────────────────────────
    public void increaseLikeCount() {
        this.likeCount++;
    }

    public void decreaseLikeCount() {
        this.likeCount = Math.max(0, this.likeCount - 1);
    }
}
