package com.likelion.dev_community.domain.answer.exception;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;

// 채택된 답변 삭제 시도를 막기 위한 전용 예외.
// ErrorCode에 새 항목을 늘리지 않고 기존 FORBIDDEN을 재사용하되,
// 메시지만 이 상황에 맞게 지정한다 (③ ErrorCode 관리 부담을 줄이는 방식).
public class AdoptedAnswerDeletionException extends CustomException {

    public AdoptedAnswerDeletionException() {
        super(ErrorCode.FORBIDDEN, "접근 권한이 없습니다. 채택된 답변은 삭제할 수 없습니다. 먼저 채택을 취소해주세요.");
    }
}
