package com.vladegole.slot_app.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@AllArgsConstructor
@NoArgsConstructor
@Data
@Builder
public class ErrorResponseDto {

    private LocalDateTime localDateTime;

    private String message;

    private String error;
}