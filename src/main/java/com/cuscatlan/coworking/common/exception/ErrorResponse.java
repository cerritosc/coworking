package com.cuscatlan.coworking.common.exception;

import java.time.LocalDateTime;
import java.util.List;

public record ErrorResponse(

        boolean success,

        ErrorCode code,

        String message,

        LocalDateTime timestamp,

        List<ValidationError> errors

) {}