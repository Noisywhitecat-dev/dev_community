package com.likelion.dev_community.domain.report.entity;

public enum ReportStatus {
    PENDING, // 신고 접수 직후 상태 (관리자가 아직 처리하지 않은 상태)
    RESOLVED, // 관리자가 신고 검토 후 정당하다 판단한 상태
    REJECTED // 관리자가 신고 검토 후 부당하다 판단한 상태
}
