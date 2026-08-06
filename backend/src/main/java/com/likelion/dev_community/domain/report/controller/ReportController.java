package com.likelion.dev_community.domain.report.controller;

import com.likelion.dev_community.common.ApiResponse;
import com.likelion.dev_community.domain.report.dto.ReportRequest;
import com.likelion.dev_community.domain.report.dto.ReportResponse;
import com.likelion.dev_community.domain.report.entity.ReportStatus;
import com.likelion.dev_community.domain.report.service.ReportService;
import com.likelion.dev_community.security.CustomUserDetails;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

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

    // 신고 목록 조회
    @GetMapping("/admin/reports")
    public ResponseEntity<ApiResponse<List<ReportResponse>>> getReports(@RequestParam(required = false) ReportStatus status,
                                                                        @PageableDefault(size = 10) Pageable pageable){
        Page<ReportResponse> reports = reportService.getReports(status, pageable);

        Map<String, Object> meta = Map.of(
                "totalElements", reports.getTotalElements(),
                "totalPages", reports.getTotalPages(),
                "page", reports.getNumber(),
                "size", reports.getSize()
        );

        return ResponseEntity.ok(ApiResponse.success("신고 목록 조회 성공", reports.getContent(), meta));
    }

    /*// 신고 개별 처리
    @PatchMapping("/admin/reports")*/

    /*// 회원 목록 전체 조회
    @GetMapping("/admin/users")

    // 특정 유저의 누적된 신고 목록 카운트
    @GetMapping("/admin/users/{id}/reports")

    // 관리자가 특정 유저 정지 수행
    @PatchMapping("/admin/users/{id}/suspend")*/
}
