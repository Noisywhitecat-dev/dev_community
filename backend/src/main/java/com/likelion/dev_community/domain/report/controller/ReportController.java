package com.likelion.dev_community.domain.report.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.report.dto.ReportRequest;
import com.likelion.dev_community.domain.report.dto.ReportResponse;
import com.likelion.dev_community.domain.report.service.ReportService;
import com.likelion.dev_community.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    @PostMapping("/reports")
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                    @Valid @RequestBody ReportRequest reportRequest){
        ReportResponse reportResponse = reportService.report(customUserDetails.getId(), reportRequest);

        return ResponseEntity.ok(ApiResponse.success("신고 처리 완료", reportResponse));
    }
}
