package com.likelion.dev_community.domain.question.repository;

import com.likelion.dev_community.domain.question.entity.Question;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionRepository extends JpaRepository<Question, Long> {
}
