package com.likelion.dev_community.domain.user.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.domain.user.dto.userDto.UserInfoRequest;
import com.likelion.dev_community.domain.user.dto.userDto.UserInfoResponse;
import com.likelion.dev_community.domain.user.dto.userDto.UserPwRequest;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.entity.UserStatus;
import com.likelion.dev_community.domain.user.repository.RefreshTokenRepository;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import com.likelion.dev_community.security.jwt.CookieProvider;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseCookie;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
    private final RefreshTokenRepository refreshTokenRepository;
    private final CookieProvider cookieProvider;

    // 회원 정보 조회
    public UserInfoResponse getUserInfo(Long userId){
        User user = findUserById(userId);

        return UserInfoResponse.from(user);
    }

    // 회원 정보 수정 (닉네임)
    @Transactional
    public UserInfoResponse updateUserInfo(UserInfoRequest request, Long userId){
        User user = findUserById(userId);

        String newNickname = request.getNickname();

        if(!user.getNickname().equals(newNickname) && userRepository.existsByNickname(newNickname))
            throw new CustomException(ErrorCode.DUPLICATE_RESOURCE, "이미 사용중인 닉네임입니다." + request.getNickname());


        user.updateUser(request.getNickname());

        return UserInfoResponse.from(user);
    }

    // 비밀번호 변경
    @Transactional
    public void updateUserPassword(UserPwRequest request, Long userId, HttpServletResponse httpServletResponse){
        User user = findUserById(userId);

        if(!passwordEncoder.matches(request.getCurrentPassword(), user.getPassword()))
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);

        user.updatePassword(passwordEncoder.encode(request.getNewPassword()));

        refreshTokenRepository.deleteByUserId(userId);

        ResponseCookie cookie = cookieProvider.clearCookie("refreshToken");
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // 회원 탈퇴
    @Transactional
    public void deleteUser(Long userId, String currentPassword, HttpServletResponse httpServletResponse){
        User user = findUserById(userId);

        if(!passwordEncoder.matches(currentPassword, user.getPassword()))
            throw new CustomException(ErrorCode.INVALID_CREDENTIALS);

        user.withdraw(); // 사용자 상태 withdrawn

        refreshTokenRepository.deleteByUserId(userId);

        ResponseCookie cookie = cookieProvider.clearCookie("refreshToken");
        httpServletResponse.addHeader(HttpHeaders.SET_COOKIE, cookie.toString());
    }

    // 모든 유저 조회 (관리자)
    @Transactional(readOnly = true)
    public Page<UserInfoResponse> getAllUsersInfo(Pageable pageable){
        Page<User> allUsers = userRepository.findAll(pageable);

        return allUsers.map(UserInfoResponse::from);
    }

    // 유저 정지
    @Transactional(readOnly = true)
    public UserInfoResponse userSuspension(Long userId){
        User user = findUserById(userId);

        if(user.getStatus().equals(UserStatus.SUSPENDED))
            throw new CustomException()

        user.suspend();


    }

    public User findUserById(Long userId){
        return userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, "유저를 찾을 수 없습니다. "+userId));
    }
}
