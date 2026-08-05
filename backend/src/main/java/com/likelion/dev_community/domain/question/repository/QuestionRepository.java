package com.likelion.dev_community.domain.question.repository;

import com.likelion.dev_community.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface QuestionRepository extends JpaRepository<Question, Long> {

    // 최신순
    @EntityGraph(attributePaths = "author")
    Page<Question> findAllOrderByLatest(Pageable pageable);

    // 추천순
    @EntityGraph(attributePaths = "author")
    Page<Question> findAllByOrderByLikeCount(Pageable pageable);

    // 미해결순
    @Query(value = "select q from Question q join fetch q.author " +
            "order by case when q.status = com.likelion.dev_community.domain.question.entity.QuestionStatus.OPEN then 0 else 1 end, " +
            "q.createdAt desc",
            countQuery = "select count(q) from Question q")
    Page<Question> findAllOrderByUnresolvedFirst(Pageable pageable);
}
