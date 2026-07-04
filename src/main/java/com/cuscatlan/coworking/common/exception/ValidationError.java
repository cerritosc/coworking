package com.cuscatlan.coworking.common.exception;

public record ValidationError(

        String field,

        Object rejectedValue,

        String message

) {}