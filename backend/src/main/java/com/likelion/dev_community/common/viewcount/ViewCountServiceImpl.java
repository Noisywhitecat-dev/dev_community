package com.likelion.dev_community.common.viewcount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

@Slf4j
@Service
@RequiredArgsConstructor
public class ViewCountServiceImpl implements ViewCountService {

    private static final String KEY_PREFIX = "view:";
    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean shouldIncrease(Long questionId, String viewerKey) {
        String key = KEY_PREFIX + questionId + ":" + viewerKey;

        try {
            Boolean firstView = redisTemplate.opsForValue().setIfAbsent(key, "1", TTL);
            return Boolean.TRUE.equals(firstView);
        } catch (DataAccessException e) {
            log.warn("조회수 중복 확인 실패 - questionId={}, 조회수 증가를 건너뜁니다", questionId, e);
            return false;
        }
    }
}