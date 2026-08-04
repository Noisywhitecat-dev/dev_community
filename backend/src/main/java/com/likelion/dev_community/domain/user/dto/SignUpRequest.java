package com.likelion.dev_community.domain.user.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public class SignUpRequest {
    @NotBlank(message = "아이디를 입력해주세요.")
    private final String username;

    @NotBlank
    @Size(min = 8,max = 64,message = "비밀번호는 8자 이상, 64자 이하여야 합니다.")
    private final String password;

    private final String nickname;
}
