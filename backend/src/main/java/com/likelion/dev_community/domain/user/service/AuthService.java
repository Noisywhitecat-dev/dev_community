package com.likelion.dev_community.domain.user.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.domain.user.dto.*;
import com.likelion.dev_community.domain.user.entity.RefreshToken;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.repository.RefreshTokenRepository;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import com.likelion.dev_community.security.JwtProvider;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.JwtException;
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

        refreshTokenRepository.deleteByUserId(user.getId());

        String accessToken = jwtProvider.createAccessToken(user.getId(), user.getUsername(), user.getNickname(), List.of(user.getRole().name()));
        String refreshToken = jwtProvider.createRefreshToken(user.getId());

        refreshTokenRepository.save(new RefreshToken(user.getId(),refreshToken,jwtProvider.getRefreshTokenExpirationMs()));

        return TokenResponse.of(accessToken, refreshToken);
    }

    @Transactional
    public ReissueResponse reissue(ReissueRequest request){

        Claims claims;

        try {
            claims = jwtProvider.parseRefreshToken(request.getRefreshToken());
        }
        catch (ExpiredJwtException e){
            throw new CustomException(ErrorCode.EXPIRED_REFRESH_TOKEN);
        }
        catch (JwtException e){
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);
        }

        Long userId = Long.valueOf(claims.getSubject());

        RefreshToken savedToken = refreshTokenRepository.findByUserId(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        if(!savedToken.getRefreshToken().equals(request.getRefreshToken()))
            throw new CustomException(ErrorCode.INVALID_REFRESH_TOKEN);

        User user = userRepository.findById(userId).orElseThrow(() -> new CustomException(ErrorCode.INVALID_REFRESH_TOKEN));

        String newAccessToken = jwtProvider.createAccessToken(
                user.getId(), user.getUsername(), user.getNickname(), List.of(user.getRole().name())
        );

        return ReissueResponse.of(newAccessToken);
    }
}
