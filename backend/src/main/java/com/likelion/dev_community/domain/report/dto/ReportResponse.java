package com.likelion.dev_community.domain.report.dto;

import com.likelion.dev_community.domain.report.entity.Report;
import com.likelion.dev_community.domain.report.entity.ReportStatus;
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

    private final ReportStatus status;

    private final LocalDateTime createdAt;

    public static ReportResponse from(Report report, String targetUserNickname){
        return new ReportResponse(
                report.getId(),
                report.getReporter().getId(),
                report.getReporter().getNickname(),
                report.getTargetType(),
                report.getTargetId(),
                report.getTargetUserId(),
                targetUserNickname,
                report.getReason(),
                report.getStatus(),
                report.getCreatedAt()
        );
    }
}
