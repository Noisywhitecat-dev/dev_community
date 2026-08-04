package com.likelion.dev_community.domain.user.entity;

import lombok.Getter;
import org.springframework.data.annotation.Id;
import org.springframework.data.redis.core.RedisHash;
import org.springframework.data.redis.core.TimeToLive;

@Getter
@RedisHash(value = "refreshToken")
public class RefreshToken {
    @Id
    private Long userId;
    private String refreshToken;

    @TimeToLive
    private Long expiration;

    public RefreshToken(Long userId,String refreshToken, Long expiration){
        this.userId = userId;
        this.refreshToken = refreshToken;
        this.expiration = expiration / 1000;
    }
}
