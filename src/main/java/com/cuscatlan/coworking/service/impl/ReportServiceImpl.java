package com.cuscatlan.coworking.service.impl;

import java.util.List;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import lombok.RequiredArgsConstructor;
import com.cuscatlan.coworking.dto.response.report.OccupationReportResponse;
import com.cuscatlan.coworking.repository.SpaceRepository;
import com.cuscatlan.coworking.service.ReportService;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ReportServiceImpl implements ReportService {

    private final SpaceRepository spaceRepository;

    @Override
    public List<OccupationReportResponse> getOccupationReport() {

        return spaceRepository.getOccupationReport();

    }

}