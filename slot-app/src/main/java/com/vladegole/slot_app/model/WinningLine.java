package com.vladegole.slot_app.model;

import com.vladegole.slot_app.enums.WinningSymbol;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.math.BigDecimal;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class WinningLine {

    private Long lineId;

    private WinningSymbol winningSymbol;

    private BigDecimal payout;

}