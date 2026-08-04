package com.likelion.dev_community.domain.user.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.user.dto.*;
import com.likelion.dev_community.domain.user.service.AuthService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/auth")
public class AuthController {

    private final AuthService authService;

    @PostMapping("/signup")
    public ResponseEntity<ApiResponse<SignUpResponse>> signUp(@Valid @RequestBody SignUpRequest signUpRequest){
        SignUpResponse signUpResponse = authService.signUp(signUpRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("회원가입 성공",signUpResponse));
    }

    @PostMapping("/login")
    public ResponseEntity<ApiResponse<TokenResponse>> login(@Valid @RequestBody SignInRequest request){
        TokenResponse tokenResponse = authService.signIn(request);

        return ResponseEntity.ok(ApiResponse.success("로그인 성공",tokenResponse));
    }

    @PostMapping("/reissue")
    public ResponseEntity<ApiResponse<ReissueResponse>> reissue(@Valid @RequestBody ReissueRequest request){
        ReissueResponse reissueResponse = authService.reissue(request);

        return ResponseEntity.ok(ApiResponse.success("토큰 재발급 성공",reissueResponse));
    }
}
