package com.likelion.dev_community.domain.answer.repository;

import com.likelion.dev_community.domain.answer.entity.Answer;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface AnswerRepository extends JpaRepository<Answer, Long> {

    // 질문별 답변 목록 조회 (다음 기능 대비)
    @EntityGraph(attributePaths = "author")
    List<Answer> findByQuestionIdOrderByCreatedAtAsc(Long questionId);

    // 내 답변 목록
    @EntityGraph(attributePaths = {"author", "question"})
    Page<Answer> findAllByAuthorIdOrderByCreatedAtDesc(Long authorId, Pageable pageable);


    // 질문에 대한 답변 갯수
    @Query("select a.question.id as questionId, count(a) as count " +
            "from Answer a where a.question.id in :questionIds group by a.question.id")
    List<QuestionAnswerCount> countByQuestionIdIn(List<Long> questionIds); // 인터페이스 프로젝션
}
