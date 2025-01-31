package org.example.reporting_and_analytics.controller;

import org.example.reporting_and_analytics.entity.Report;
import org.example.reporting_and_analytics.service.ReportService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/reports")
public class ReportController {

    private final ReportService reportService;

    public ReportController(ReportService reportService) {
        this.reportService = reportService;
    }

    @GetMapping("/progress")
    public ResponseEntity<Report> getProjectProgress() {
        return ResponseEntity.ok(reportService.calculateProgress());
    }

    @GetMapping("/timelines")
    public ResponseEntity<Report> getTimelinesReport() {
        return ResponseEntity.ok(reportService.calculateTimelines());
    }

    @GetMapping("/milestones")
    public ResponseEntity<Map<String, Object>> getMilestoneOverview() {
        return ResponseEntity.ok(reportService.getMilestoneOverview());
    }

    @GetMapping("/completion_rates")
    public ResponseEntity<Map<String, Object>> getTaskCompletionRates() {
        return ResponseEntity.ok(reportService.getTaskCompletionRates());
    }

    @GetMapping("/performance")
    public ResponseEntity<Report> getTeamPerformance() {
        return ResponseEntity.ok(reportService.calculateTeamPerformance());
    }
}
