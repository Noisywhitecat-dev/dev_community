package com.likelion.dev_community.domain.user.service;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;
import com.likelion.dev_community.domain.user.dto.userDto.UserInfoResponse;
import com.likelion.dev_community.domain.user.entity.User;
import com.likelion.dev_community.domain.user.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UserService {

    private final UserRepository userRepository;

    // 회원 정보 조회
    public UserInfoResponse getUserInfo(Long userId){
        User user = userRepository.findById(userId).orElseThrow(()->new CustomException(ErrorCode.NOT_FOUND, "유저를 찾을 수 없습니다. "+userId));

        return UserInfoResponse.from(user);
    }
}
