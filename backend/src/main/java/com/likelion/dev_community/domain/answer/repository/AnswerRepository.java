package com.likelion.dev_community.domain.answer.repository;

import com.likelion.dev_community.domain.answer.entity.Answer;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnswerRepository extends JpaRepository<Answer, Long> {
}
