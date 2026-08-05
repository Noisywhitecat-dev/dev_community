package com.likelion.dev_community.domain.question.service;

import com.likelion.dev_community.domain.question.dto.QuestionListResponse;
import com.likelion.dev_community.domain.question.dto.QuestionRequest;
import com.likelion.dev_community.domain.question.dto.QuestionResponse;
import org.springframework.data.domain.Page;

public interface QuestionService {

    // F-06
    QuestionResponse createQuestion(Long userId, QuestionRequest request);

    // F-07
    Page<QuestionListResponse> readQuestions(int page, int size, String sort);
}
