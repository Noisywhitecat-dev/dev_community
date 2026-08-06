package com.likelion.dev_community.domain.report.dto;

import com.likelion.dev_community.domain.report.entity.ReportTargetType;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.LocalDateTime;

@AllArgsConstructor
@Getter
public class ReportResponse {

    private final Long id;

    private final Long reporterId;

    private final String reporterNickname;

    private final ReportTargetType targetType;

    private final Long targetId;

    private final Long targetUserId;

    private final String targetUserNickname;

    private final String reason;

    private final LocalDateTime createdAt;
}
