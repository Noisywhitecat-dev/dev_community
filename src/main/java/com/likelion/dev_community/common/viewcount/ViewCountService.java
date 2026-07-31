package com.likelion.dev_community.common.viewcount;

public interface ViewCountService {

    /**
     * 해당 조회자가 이 질문의 조회수를 증가시켜야 하는지 판단한다.
     *
     * @param questionId 조회 대상 질문 ID
     * @param viewerKey  조회자 식별 키 (ViewerKeyResolver로 생성)
     * @return 최초 조회면 true, 24시간 내 재조회면 false
     */
    boolean shouldIncrease(Long questionId, String viewerKey);
}