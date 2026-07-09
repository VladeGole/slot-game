package com.vladegole.slot_app.dto;

import com.vladegole.slot_app.enums.WinningSymbol;
import com.vladegole.slot_app.model.WinningLine;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;
import java.util.List;

@NoArgsConstructor
@AllArgsConstructor
@Data
@Builder
public class SpinWinningResponseDto {

    private WinningSymbol[][] matrix;

    private List<WinningLine> winningLines;

    private BigDecimal totalWin;

    private BigDecimal newBalance;

}