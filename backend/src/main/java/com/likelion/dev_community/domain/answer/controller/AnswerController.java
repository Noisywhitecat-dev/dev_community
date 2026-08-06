package com.likelion.dev_community.domain.answer.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.answer.dto.AnswerRequest;
import com.likelion.dev_community.domain.answer.dto.AnswerResponse;
import com.likelion.dev_community.domain.answer.service.AnswerService;
import com.likelion.dev_community.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions/{questionId}/answers")
public class AnswerController {

    private final AnswerService answerService;

    // F-12
    @PostMapping
    public ResponseEntity<ApiResponse<AnswerResponse>> createAnswer(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @PathVariable Long questionId,
            @Valid @RequestBody AnswerRequest request
    ) {
        AnswerResponse response = answerService.createAnswer(userDetails.getId(), questionId, request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("답변 등록 완료", response));
    }
}
