package com.likelion.dev_community.common.exception;

import lombok.Getter;
import org.springframework.http.HttpStatus;

// ③ ErrorCode 관리 규칙
// 이 enum은 4명이 공유하는 파일이라 동시에 줄을 추가하면 병합 충돌이 잦다.
// 그래서 담당자별로 추가할 위치를 아래처럼 구간으로 나눠둔다.
// 코드가 이미 존재하는 상황(NOT_FOUND, FORBIDDEN 등)이면 새로 만들지 말고
// 기존 코드 + 커스텀 메시지(예: AdoptedAnswerDeletionException 참고)로 해결한다.
// 정말 새 코드가 필요하면 자기 구간에만 추가하고, 구간을 넘어서는 추가는
// D(관리자)에게 요청한다.
@Getter
public enum ErrorCode {

    // ── 공통 (전원 사용) ─────────────────────────────────
    INVALID_INPUT(HttpStatus.BAD_REQUEST, "INVALID_INPUT", "입력값이 올바르지 않습니다."),
    UNAUTHORIZED(HttpStatus.UNAUTHORIZED, "UNAUTHORIZED", "인증이 필요합니다."),
    FORBIDDEN(HttpStatus.FORBIDDEN, "FORBIDDEN", "접근 권한이 없습니다."),
    NOT_FOUND(HttpStatus.NOT_FOUND, "NOT_FOUND", "요청한 리소스를 찾을 수 없습니다."),
    DUPLICATE_RESOURCE(HttpStatus.CONFLICT, "DUPLICATE_RESOURCE", "이미 존재하는 리소스입니다."),
    INTERNAL_ERROR(HttpStatus.INTERNAL_SERVER_ERROR, "INTERNAL_ERROR", "서버 내부 오류가 발생했습니다."),

    // ── A 구간 (회원/인증) ──────────────────────────────
    // 예: INVALID_CREDENTIALS, TOKEN_EXPIRED 등 필요 시 여기에 추가

    // ── B 구간 (질문/태그/검색) ─────────────────────────
    // 예: INVALID_TAG 등 필요 시 여기에 추가

    // ── C 구간 (답변) ────────────────────────────────────
    // 예: ALREADY_RESOLVED 등 필요 시 여기에 추가

    // ── D 구간 (추천/인프라) ────────────────────────────
    // 예: ALREADY_LIKED 등 필요 시 여기에 추가
    ;

    private final HttpStatus status;
    private final String code;
    private final String message;

    ErrorCode(HttpStatus status, String code, String message) {
        this.status = status;
        this.code = code;
        this.message = message;
    }
}
