package com.cuscatlan.coworking.dto.response.report;

public record OccupationReportResponse(

        Long spaceId,

        String spaceName,

        Double occupationPercentage

) {
}