package com.likelion.dev_community.domain.answer.service;

import com.likelion.dev_community.domain.answer.dto.AnswerRequest;
import com.likelion.dev_community.domain.answer.dto.AnswerResponse;

import java.util.List;

public interface AnswerService {

    // F-12
    AnswerResponse createAnswer(Long userId, Long questionId, AnswerRequest request);

    //
    List<AnswerResponse> readAnswers(Long questionId);

    // F-13
    AnswerResponse updateAnswer(Long userId, Long answerId, AnswerRequest request);

    // F-13
    void deleteAnswer(Long userId, Long answerId);
}
