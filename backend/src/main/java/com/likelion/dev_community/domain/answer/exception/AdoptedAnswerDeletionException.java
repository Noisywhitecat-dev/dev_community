package com.likelion.dev_community.domain.answer.exception;

import com.likelion.dev_community.common.exception.CustomException;
import com.likelion.dev_community.common.exception.ErrorCode;

public class AdoptedAnswerDeletionException extends CustomException {

    public AdoptedAnswerDeletionException() {
        super(ErrorCode.FORBIDDEN, "접근 권한이 없습니다. 채택된 답변은 삭제할 수 없습니다. 먼저 채택을 취소해주세요.");
    }
}
