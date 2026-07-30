package com.likelion.dev_community.common.viewcount;

import jakarta.servlet.http.HttpServletRequest;
import org.springframework.stereotype.Component;

// ④ viewerKey 생성 규칙 (B, D 공동 합의)
// - 로그인 사용자: "user:{userId}" → 로그아웃 후 재로그인해도 동일 인물로 인식됨
// - 비로그인 사용자: "ip:{ip}" → 같은 공용 와이파이의 다른 사람과 섞일 수 있다는
//   한계가 있지만, MVP1 규모에서는 이 정도 오차를 감수하기로 함(과도한 설계 방지)
// B는 컨트롤러/서비스에서 이 클래스를 통해 key를 만들어 ViewCountService에 넘기고,
// D는 이 key의 "형식"만 알면 되므로 내부 로직(로그인 여부 판단)은 몰라도 된다.
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
