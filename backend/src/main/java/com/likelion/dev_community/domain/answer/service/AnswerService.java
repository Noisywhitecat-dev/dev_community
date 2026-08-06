package com.likelion.dev_community.domain.answer.service;

import com.likelion.dev_community.domain.answer.dto.AnswerRequest;
import com.likelion.dev_community.domain.answer.dto.AnswerResponse;

import java.util.List;

public interface AnswerService {

    // F-12
    AnswerResponse createAnswer(Long userId, Long questionId, AnswerRequest request);

    // F-13
    List<AnswerResponse> readAnswers(Long questionId);

    // F-14
    AnswerResponse updateAnswer(Long userId, Long answerId, AnswerRequest request);

    // F-14
    void deleteAnswer(Long userId, Long answerId);

    // F-14 채택
    AnswerResponse adoptAnswer(Long userId, Long answerId);
}
