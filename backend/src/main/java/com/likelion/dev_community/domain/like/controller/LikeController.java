package com.likelion.dev_community.domain.like.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.like.entity.LikeTargetType;
import com.likelion.dev_community.domain.like.service.LikeService;
import com.likelion.dev_community.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class LikeController {

    private final LikeService likeService;

    @PostMapping("/api/questions/{id}/like")
    public ApiResponse<Boolean> toggleQuestionLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                     @PathVariable Long id) {
        boolean liked = likeService.toggleLike(userDetails.getId(), LikeTargetType.QUESTION, id);
        return ApiResponse.success(liked);
    }

    @PostMapping("/api/answers/{id}/like")
    public ApiResponse<Boolean> toggleAnswerLike(@AuthenticationPrincipal CustomUserDetails userDetails,
                                                  @PathVariable Long id) {
        boolean liked = likeService.toggleLike(userDetails.getId(), LikeTargetType.ANSWER, id);
        return ApiResponse.success(liked);
    }
}
