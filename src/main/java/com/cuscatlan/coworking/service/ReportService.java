package com.cuscatlan.coworking.service;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

import com.cuscatlan.coworking.dto.response.report.OccupationReportResponse;

public interface ReportService {

    List<OccupationReportResponse> getOccupationReport(LocalDateTime startDate, LocalDateTime endDate);

}