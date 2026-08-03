package com.likelion.dev_community.domain.user.dto;

import com.likelion.dev_community.domain.user.entity.User;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpResponse {
    private final Long userId;
    private final String username;
    private final String nickname;

    public static SignUpResponse from(User user){
        return new SignUpResponse(
                user.getId(),
                user.getUsername(),
                user.getNickname()
        );
    }
}
