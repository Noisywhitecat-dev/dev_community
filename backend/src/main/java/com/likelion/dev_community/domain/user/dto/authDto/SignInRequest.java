package com.likelion.dev_community.domain.user.dto.authDto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignInRequest {
    @NotBlank
    private final String username;

    @NotBlank
    private final String password;
}
