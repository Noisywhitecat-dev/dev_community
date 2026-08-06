package com.likelion.dev_community.domain.answer.service;

import com.likelion.dev_community.domain.answer.dto.AnswerRequest;
import com.likelion.dev_community.domain.answer.dto.AnswerResponse;

public interface AnswerService {

    // F-12
    AnswerResponse createAnswer(Long userId, Long questionId, AnswerRequest request);
}
