package com.likelion.dev_community.domain.user.dto.authDto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class ReissueResponse {
    private String accessToken;

    public static ReissueResponse of(String accessToken){
        return new ReissueResponse(accessToken);
    }
}
