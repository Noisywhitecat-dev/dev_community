package com.likelion.dev_community.domain.question.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.common.viewcount.ViewerKeyResolver;
import com.likelion.dev_community.domain.question.dto.QuestionDetailResponse;
import com.likelion.dev_community.domain.question.dto.QuestionSummaryResponse;
import com.likelion.dev_community.domain.question.dto.QuestionRequest;
import com.likelion.dev_community.domain.question.dto.QuestionResponse;
import com.likelion.dev_community.domain.question.service.QuestionService;
import com.likelion.dev_community.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/questions")
public class QuestionController {

    private final QuestionService questionService;
    private final ViewerKeyResolver viewerKeyResolver;

    // F-06
    @PostMapping
    public ResponseEntity<ApiResponse<QuestionResponse>> createQuestion(
            @AuthenticationPrincipal CustomUserDetails userDetails,
            @Valid @RequestBody QuestionRequest request
    ) {
        QuestionResponse response = questionService.createQuestion(userDetails.getId(), request);

        return ResponseEntity.status(HttpStatus.CREATED)
                .body(ApiResponse.success("질문 등록 완료", response));
    }

    // F-07
    @GetMapping
    public ResponseEntity<ApiResponse<List<QuestionSummaryResponse>>> readQuestions(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size,
            @RequestParam(required = false) String sort
    ) {
        Page<QuestionSummaryResponse> result = questionService.readQuestions(page, size, sort);

        Map<String, Object> meta = Map.of(
                "page", result.getNumber(),
                "size", result.getSize(),
                "totalElements", result.getTotalElements(),
                "totalPages", result.getTotalPages()
        );

        return ResponseEntity.ok(ApiResponse.success("질문 목록 조회", result.getContent(), meta));
    }

    // F-08
    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<QuestionDetailResponse>> readDetailQuestion(
            @PathVariable Long id,
            @AuthenticationPrincipal CustomUserDetails userDetails,
            HttpServletRequest request
    ) {
        Long userId = (userDetails != null) ? userDetails.getId() : null;
        String viewerKey = viewerKeyResolver.resolve(userId, request);

        QuestionDetailResponse response = questionService.readQuestionDetail(id, viewerKey);

        return ResponseEntity.ok(ApiResponse.success(response));
    }
}