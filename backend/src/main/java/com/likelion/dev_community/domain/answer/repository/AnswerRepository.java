package com.likelion.dev_community.domain.answer.repository;

import com.likelion.dev_community.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 질문별 답변 목록 조회 (다음 기능 대비)
    @EntityGraph(attributePaths = "author")
    List<Answer> findByQuestionIdOrderByCreatedAtAsc(Long questionId);
}
