package com.cuscatlan.coworking.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;
import com.cuscatlan.coworking.common.util.*;
import com.cuscatlan.coworking.common.response.ApiResponse;
import com.cuscatlan.coworking.dto.response.report.OccupationReportResponse;
import com.cuscatlan.coworking.service.ReportService;

@RestController
@RequiredArgsConstructor
@RequestMapping(ApiPaths.REPORTS)
@Tag(
        name = "Reports",
        description = "Reporting API"
)
public class ReportController {

    private final ReportService reportService;

    @GetMapping("/occupation")
    @Operation(summary = "Get occupation report")
    public ResponseEntity<ApiResponse<List<OccupationReportResponse>>> getOccupationReport() {

        List<OccupationReportResponse> response =
                reportService.getOccupationReport();

        return ResponseEntity.ok(
                ApiResponse.success(
                        "Occupation report generated successfully.",
                        response));
    }

}