package com.likelion.dev_community.common.viewcount;

import lombok.RequiredArgsConstructor;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

// ④ D가 구현: Redis 기반 조회수 중복 방지 (F-08)
// key 예시: view:{questionId}:{viewerKey}
// SETNX(있으면 실패, 없으면 성공)로 "최초 조회 여부"를 원자적으로 판단한다.
// 이 방식을 쓰는 이유: "조회 여부 확인 → 있으면 무시 → 없으면 저장"을
// 두 단계로 나누면 동시 요청 시 경쟁 조건이 생길 수 있는데,
// setIfAbsent 한 번으로 확인과 저장을 동시에 처리해 이를 막는다.
@Service
@RequiredArgsConstructor
public class ViewCountServiceImpl implements ViewCountService {

    private static final Duration TTL = Duration.ofHours(24);

    private final StringRedisTemplate redisTemplate;

    @Override
    public boolean shouldIncrease(Long questionId, String viewerKey) {
        String key = "view:" + questionId + ":" + viewerKey;
        Boolean firstView = redisTemplate.opsForValue().setIfAbsent(key, "1", TTL);
        return Boolean.TRUE.equals(firstView);
    }
}
