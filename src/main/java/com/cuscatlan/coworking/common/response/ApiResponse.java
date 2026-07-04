package com.cuscatlan.coworking.common.response;

import java.time.LocalDateTime;

public record ApiResponse<T>(

        boolean success,

        String message,

        LocalDateTime timestamp,

        T data

) {

    public static <T> ApiResponse<T> success(T data) {

        return new ApiResponse<>(

                true,

                "Operation completed successfully.",

                LocalDateTime.now(),

                data

        );

    }

    public static <T> ApiResponse<T> success(String message, T data) {

        return new ApiResponse<>(

                true,

                message,

                LocalDateTime.now(),

                data

        );

    }

}