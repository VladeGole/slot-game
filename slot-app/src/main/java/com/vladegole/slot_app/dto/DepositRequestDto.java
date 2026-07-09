package com.vladegole.slot_app.dto;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class DepositRequestDto {

    @NotNull(message = "Amount is required")
    @Min(value = 10, message = "Minimum deposit must be 10")
    private BigDecimal amount;

}