package com.likelion.dev_community.domain.report.dto;

import com.likelion.dev_community.domain.report.entity.ReportStatus;
import jakarta.validation.constraints.NotNull;
import lombok.Getter;

@Getter
public class ReportProcessRequest {

    @NotNull
    private final ReportStatus status;

    public ReportProcessRequest(ReportStatus status) {
        this.status = status;
    }
}
