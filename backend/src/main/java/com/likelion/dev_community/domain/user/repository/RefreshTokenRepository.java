package com.likelion.dev_community.domain.user.repository;


import com.likelion.dev_community.domain.user.entity.RefreshToken;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface RefreshTokenRepository extends CrudRepository<RefreshToken,Long> {
    Optional<RefreshToken> findByRefreshToken(String refreshToken);

    void deleteByUserId(Long userId);

    Optional<RefreshToken> findByUserId(Long userId);
}
