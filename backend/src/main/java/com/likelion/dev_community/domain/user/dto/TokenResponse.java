package com.likelion.dev_community.domain.user.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
@Getter
public class TokenResponse {
    private final String accessToken;
    private final String tokenType;

    public static TokenResponse of(String accessToken){
        return new TokenResponse(accessToken,"Bearer ");
    }
}
