package com.likelion.dev_community.domain.report.controller;

import com.likelion.dev_community.domain.report.dto.ReportResponse;
import com.likelion.dev_community.domain.report.service.ReportService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api")
public class ReportController {

    private final ReportService reportService;

   /* @PostMapping("/reports")
    public ReportResponse*/
}
