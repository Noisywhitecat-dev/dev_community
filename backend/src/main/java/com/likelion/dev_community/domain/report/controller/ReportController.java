package com.likelion.dev_community.domain.report.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.report.dto.ReportRequest;
import com.likelion.dev_community.domain.report.dto.ReportResponse;
import com.likelion.dev_community.domain.report.service.ReportService;
import com.likelion.dev_community.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

    // 신고 접수
    @PostMapping("/reports")
    public ResponseEntity<ApiResponse<ReportResponse>> createReport(@AuthenticationPrincipal CustomUserDetails customUserDetails,
                                                                    @Valid @RequestBody ReportRequest reportRequest){
        ReportResponse reportResponse = reportService.report(customUserDetails.getId(), reportRequest);

        return ResponseEntity.status(HttpStatus.CREATED).body(ApiResponse.success("신고 접수 완료",reportResponse));
    }

   /* // 신고 목록 조회
    @GetMapping("/admin/reports")

    // 신고 개별 처리
    @PatchMapping("/admin/reports")

    // 회원 목록 전체 조회
    @GetMapping("/admin/users")

    // 특정 유저의 누적된 신고 목록 카운트
    @GetMapping("/admin/users/{id}/reports")

    // 관리자가 특정 유저 정지 수행
    @PatchMapping("/admin/users/{id}/suspend")*/
}
