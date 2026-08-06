package com.likelion.dev_community.domain.report.repository;

import com.likelion.dev_community.domain.report.entity.Report;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ReportRepository extends JpaRepository<Report,Long> {
}
