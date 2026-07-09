package com.vladegole.slot_app.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class User {

    private Long id;

    @PositiveOrZero
    @Builder.Default
    private BigDecimal balance = BigDecimal.ZERO;
}