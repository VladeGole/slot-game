package com.vladegole.slot_app.model;

import jakarta.validation.constraints.PositiveOrZero;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@AllArgsConstructor
@NoArgsConstructor
public class WinningSpin {

    private String[][] matrix;

    private WinningLine[] winningLines;

    private BigDecimal totalWin;

    @PositiveOrZero
    private BigDecimal newBalance;
}
