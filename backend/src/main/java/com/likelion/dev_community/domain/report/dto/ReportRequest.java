package com.likelion.dev_community.domain.report.dto;

import com.likelion.dev_community.domain.report.entity.ReportTargetType;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class ReportRequest {

    @NotNull
    private final ReportTargetType targetType;

    @NotNull
    private final Long targetId;

    @NotBlank
    private final String reason;
}
