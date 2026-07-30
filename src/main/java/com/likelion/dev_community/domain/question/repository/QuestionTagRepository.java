package com.likelion.dev_community.domain.question.repository;

import com.likelion.dev_community.domain.question.entity.QuestionTag;
import org.springframework.data.jpa.repository.JpaRepository;

public interface QuestionTagRepository extends JpaRepository<QuestionTag, Long> {
}
