package com.likelion.dev_community.domain.user.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.user.dto.userDto.UserInfoResponse;
import com.likelion.dev_community.domain.user.service.UserService;
import com.likelion.dev_community.security.CustomUserDetails;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/members")
public class UserController {

    private final UserService userService;

    @GetMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> getUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails){
        UserInfoResponse userInfo = userService.getUserInfo(customUserDetails.getId());

        return ResponseEntity.ok(ApiResponse.success("회원 정보 조회 성공",userInfo));
    }
}
