package com.likelion.dev_community.domain.report.dto;

import com.likelion.dev_community.domain.report.entity.ReportStatus;
import lombok.Getter;

@Getter
public class ReportProcessRequest {
    private final ReportStatus status;

    public ReportProcessRequest(ReportStatus status) {
        this.status = status;
    }
}
