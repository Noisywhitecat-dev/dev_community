package com.likelion.dev_community.common.viewcount;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.data.redis.core.StringRedisTemplate;
import org.springframework.stereotype.Service;

import java.time.Duration;

/**
 * Redis 기반 조회수 중복 방지 구현 (F-08)
 *
 * key   : view:{questionId}:{viewerKey}
 * TTL   : 24시간 (만료되면 자동 삭제되므로 별도 정리 작업이 필요 없다)
 *
 * setIfAbsent(SETNX)를 쓰는 이유:
 * "조회 기록이 있는지 확인" → "없으면 저장"을 두 단계로 나누면
 * 같은 사용자가 새로고침을 연타할 때 두 요청이 모두 '최초 조회'로 통과할 수 있다.
 * setIfAbsent는 확인과 저장을 원자적으로 수행해 이 경쟁 조건을 막는다.
 */
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
            // Redis 장애가 질문 상세 조회 전체를 막으면 안 되므로,
            // 실패 시에는 조회수를 올리지 않고 넘어간다(중복 집계보다 누락이 안전).
            log.warn("조회수 중복 확인 실패 - questionId={}, 조회수 증가를 건너뜁니다", questionId, e);
            return false;
        }
    }
}