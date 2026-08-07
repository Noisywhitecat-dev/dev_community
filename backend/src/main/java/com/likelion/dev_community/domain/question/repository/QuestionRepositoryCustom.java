package com.likelion.dev_community.domain.question.repository;

import com.likelion.dev_community.domain.question.entity.Question;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface QuestionRepositoryCustom {
    // F-17
    Page<Question> search(String keyword, Pageable pageable);
}
