package com.vladegole.slot_app.service;

import com.vladegole.slot_app.config.PayoutLineConfig;
import com.vladegole.slot_app.dto.SpinRequestDto;
import com.vladegole.slot_app.dto.SpinWinningResponseDto;
import com.vladegole.slot_app.enums.WinningSymbol;
import com.vladegole.slot_app.model.PayoutLine;
import com.vladegole.slot_app.model.WinningLine;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.*;

import static java.math.BigDecimal.ZERO;

@Service
public class SpinService {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private PayoutCalculatorService payoutCalculatorService;

    @Autowired
    private ReelSpinService reelSpinService;

    public SpinWinningResponseDto spin(SpinRequestDto spinRequestDto) {

        if (spinRequestDto.getBetAmount() != 5) {
            throw new IllegalArgumentException("Bet amount must to be 5");
        }

        balanceService.reduceBalance(spinRequestDto.getBetAmount());

        WinningSymbol[][] matrix = reelSpinService.reelSpin();

        List<WinningLine> winningLineArray = new ArrayList<>();
        BigDecimal totalWin = ZERO;

        for (PayoutLine payoutLine : PayoutLineConfig.LINE) {
            WinningLine winningLine = payoutCalculatorService.evaluateLine(matrix, PayoutLineConfig.SPIN_WIN, payoutLine);

            if (Objects.nonNull(winningLine)) {
                winningLineArray.add(winningLine);
                totalWin = totalWin.add(winningLine.getPayout());
            }
        }

       BigDecimal newBalance = balanceService.addWin(totalWin);

        return SpinWinningResponseDto.builder()
                .matrix(matrix)
                .winningLines(winningLineArray)
                .totalWin(totalWin)
                .newBalance(newBalance).build();
    }
}