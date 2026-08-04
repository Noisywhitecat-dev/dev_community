package com.likelion.dev_community.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Getter;

@Getter
public class ReissueRequest {

    @NotBlank
    private final String refreshToken;

    public ReissueRequest(String refreshToken) {
        this.refreshToken = refreshToken;
    }
}
