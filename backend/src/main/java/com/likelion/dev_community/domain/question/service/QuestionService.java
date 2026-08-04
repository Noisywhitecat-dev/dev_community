package com.likelion.dev_community.domain.question.service;

import com.likelion.dev_community.domain.question.dto.QuestionRequest;
import com.likelion.dev_community.domain.question.dto.QuestionResponse;

public interface QuestionService {
    QuestionResponse createQuestion(Long userId, QuestionRequest request);
}
