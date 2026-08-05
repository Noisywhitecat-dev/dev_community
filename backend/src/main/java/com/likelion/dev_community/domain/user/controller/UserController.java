package com.likelion.dev_community.domain.user.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.user.dto.userDto.UserInfoRequest;
import com.likelion.dev_community.domain.user.dto.userDto.UserInfoResponse;
import com.likelion.dev_community.domain.user.dto.userDto.UserPwRequest;
import com.likelion.dev_community.domain.user.dto.userDto.UserWithdrawRequest;
import com.likelion.dev_community.domain.user.service.UserService;
import com.likelion.dev_community.security.CustomUserDetails;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

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

    @PutMapping("/me")
    public ResponseEntity<ApiResponse<UserInfoResponse>> updateUserInfo(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                        @Valid @RequestBody UserInfoRequest userInfoRequest){
        UserInfoResponse userInfo = userService.updateUserInfo(userInfoRequest, customUserDetails.getId());

        return ResponseEntity.ok(ApiResponse.success("회원 정보 수정 성공",userInfo));
    }

    @PutMapping("/me/password")
    public ResponseEntity<ApiResponse<Void>> updateUserPassword(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                @Valid @RequestBody UserPwRequest userPwRequest,
                                                                HttpServletResponse httpServletResponse){
        userService.updateUserPassword(userPwRequest, customUserDetails.getId(),httpServletResponse);

        return ResponseEntity.ok(ApiResponse.success("비밀번호 변경 성공",null));
    }

    @DeleteMapping("/me")
    public ResponseEntity<ApiResponse<Void>> softDelete(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                        @Valid @RequestBody UserWithdrawRequest request,
                                                        HttpServletResponse httpServletResponse){
        userService.deleteUser(customUserDetails.getId(), request.getCurrentPassword(), httpServletResponse);

        return ResponseEntity.noContent().build();
    }
}
