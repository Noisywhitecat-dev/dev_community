package com.likelion.dev_community.domain.user.dto.authDto;

import com.likelion.dev_community.domain.user.entity.User;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class SignUpResponse {
    private final String username;
    private final String nickname;

    public static SignUpResponse from(User user){
        return new SignUpResponse(
                user.getUsername(),
                user.getNickname()
        );
    }
}
