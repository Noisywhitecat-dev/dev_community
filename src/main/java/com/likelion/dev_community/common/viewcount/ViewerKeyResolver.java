package com.likelion.dev_community.common.viewcount;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

/**
 * 조회자 식별 키 생성 규칙 (B, D 합의 사항)
 * - 로그인 사용자: "user:{userId}"
 * - 비로그인 사용자: "ip:{ip}"
 *
 * 비로그인 사용자를 IP로 구분하므로 같은 공용 네트워크의 서로 다른 사람이
 * 하나로 묶일 수 있다. MVP1 범위에서는 이 정도 오차를 허용한다.
 */
@Component
public class ViewerKeyResolver {

    public String resolve(Long userId, HttpServletRequest request) {
        if (userId != null) {
            return "user:" + userId;
        }
        return "ip:" + extractIp(request);
    }

    private String extractIp(HttpServletRequest request) {
        String forwarded = request.getHeader("X-Forwarded-For");
        if (forwarded != null && !forwarded.isBlank()) {
            return forwarded.split(",")[0].trim();
        }
        return request.getRemoteAddr();
    }
}