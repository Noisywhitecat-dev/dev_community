package com.likelion.dev_community.domain.user.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.domain.user.dto.SignInRequest;
import com.likelion.dev_community.domain.user.dto.SignUpRequest;
import com.likelion.dev_community.domain.user.dto.SignUpResponse;
import com.likelion.dev_community.domain.user.dto.TokenResponse;
import com.likelion.dev_community.domain.user.entity.RefreshToken;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.repository.RefreshTokenRepository;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import com.likelion.dev_community.security.JwtProvider;
import lombok.RequiredArgsConstructor;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class AuthService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtProvider jwtProvider;
    private final RefreshTokenRepository refreshTokenRepository;

    // 회원가입
    @Transactional
    public SignUpResponse signUp(SignUpRequest request){

        if(userRepository.existsByUsername(request.getUsername()))
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "사용중인 아이디입니다." + request.getUsername());

        if(userRepository.existsByNickname(request.getNickname()))
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "사용중인 닉네임입니다." + request.getNickname());

        User user = User.createUser(
                request.getUsername(),
                passwordEncoder.encode(request.getPassword()),
                request.getNickname()
        );

        userRepository.save(user);

        return SignUpResponse.from(user);
    }

    // 로그인
    @Transactional
    public TokenResponse signIn(SignInRequest request){
        User user = userRepository.findByUsername(request.getUsername()).orElseThrow(() -> new CustomException(ErrorCode.INVALID_CREDENTIALS));

        if(!passwordEncoder.matches(request.getPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getUsername(), user.getNickname(), List.of(user.getRole().name()));
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getId(),refreshToken,jwtProvider.getRefreshTokenExpirationMs()));

        return TokenResponse.of(accessToken, refreshToken);
    }
}
