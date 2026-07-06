package com.cuscatlan.coworking.controller;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDateTime;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import org.springframework.test.web.servlet.MockMvc;

import com.cuscatlan.coworking.common.util.ApiPaths;
import com.cuscatlan.coworking.dto.response.report.OccupationReportResponse;
import com.cuscatlan.coworking.service.ReportService;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
class ReportControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ReportService reportService;

    @Test
    @DisplayName("Should return occupation report")
    void shouldReturnOccupationReport() throws Exception {

        LocalDateTime start =
                LocalDateTime.of(2026, 7, 1, 0, 0);

        LocalDateTime end =
                LocalDateTime.of(2026, 7, 31, 23, 59);

        OccupationReportResponse response =
                new OccupationReportResponse(
                        1L,
                        "Sala IT",
                        75.0);

        when(reportService.getOccupationReport(start, end))
                .thenReturn(List.of(response));

        mockMvc.perform(
                get(ApiPaths.REPORTS + "/occupation")
                        .param("startDate", start.toString())
                        .param("endDate", end.toString()))

                .andExpect(status().isOk())

                .andExpect(jsonPath("$.success")
                        .value(true))

                .andExpect(jsonPath("$.message")
                        .value("Occupation report generated successfully."))

                .andExpect(jsonPath("$.data.length()")
                        .value(1))

                .andExpect(jsonPath("$.data[0].spaceName")
                        .value("Sala IT"))
                .andDo(print())
                .andExpect(jsonPath("$.data[0].occupationPercentage")
                        .value(75.0));

    }

}