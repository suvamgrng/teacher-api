package com.suvam.teacherapi.exception;

import java.time.LocalDateTime;
import java.util.Map;

public record ErrorResponse(
        LocalDateTime timestamp,
        int status,
        String error,
        String message,
        Map<String, String> errors,
        String path
) {
    public ErrorResponse(
            LocalDateTime timestamp,
            int status,
            String error,
            String message,
            String path
    ) {
        this(
                timestamp,
                status,
                error,
                message,
                null,  // Set value of errors null if this constructor is called
                path
        );
    }
}
