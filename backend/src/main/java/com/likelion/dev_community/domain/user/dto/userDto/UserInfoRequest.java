package com.likelion.dev_community.domain.user.dto.userDto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class UserInfoRequest {

    @NotBlank
    private final String nickname;

    public UserInfoRequest(String nickname) {
        this.nickname = nickname;
    }
}
