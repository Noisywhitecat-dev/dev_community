package com.likelion.dev_community.domain.question.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Entity
@Table(
        name = "question_tags",
        uniqueConstraints = @UniqueConstraint(columnNames = {"question_id", "tag_id"})
)
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class QuestionTag {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "question_id", nullable = false)
    private Question question;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "tag_id", nullable = false)
    private Tag tag;

    @Builder
    public QuestionTag(Question question, Tag tag) {
        this.question = question;
        this.tag = tag;
    }
}
