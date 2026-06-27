package com.example.Tasks.domain.dto;

public record ErrorResponse(
        int status,
        String message,
        String details
) {
}
