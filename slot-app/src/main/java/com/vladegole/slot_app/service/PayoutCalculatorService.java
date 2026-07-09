package com.vladegole.slot_app.service;

import com.vladegole.slot_app.enums.WinningSymbol;
import com.vladegole.slot_app.model.PayoutLine;
import com.vladegole.slot_app.model.WinningLine;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Map;

@Service
public class PayoutCalculatorService {

    private WinningSymbol[] extractSymbolForLine(WinningSymbol[][] matrix, PayoutLine payoutLine) {
        WinningSymbol[] lineSymbols = new WinningSymbol[5];
        for (int col = 0; col < lineSymbols.length; col++) {
            int row = payoutLine.getRows()[col];
            lineSymbols[col] = matrix[row][col];
        }
        return lineSymbols;
    }

    private  WinningSymbol determineBaseSymbol(WinningSymbol[] winningSymbols) {
        for (WinningSymbol symbol : winningSymbols) {
            if (symbol != WinningSymbol.W) {
                return symbol;
            }
        }
        return WinningSymbol.A;
    }

    public WinningLine evaluateLine(WinningSymbol[][] matrix, Map<WinningSymbol, Map<Integer, BigDecimal>> priceTable, PayoutLine payoutLine) {
        WinningSymbol[] winningSymbols = extractSymbolForLine(matrix, payoutLine);
        WinningSymbol basedSymbol = determineBaseSymbol(winningSymbols);
        int count = countConsecutiveMatches(basedSymbol, winningSymbols);

        if (count < 3) {
            return null;
        }

        BigDecimal price = priceTable.get(basedSymbol).get(count);

        return WinningLine.builder().lineId(payoutLine.getId()).winningSymbol(basedSymbol).payout(price).build();
    }

    private int countConsecutiveMatches(WinningSymbol basedSymbol, WinningSymbol[] winningSymbols) {
        int count = 1;
        for (int i = 1; i < winningSymbols.length; i++) {
            boolean matches = basedSymbol == winningSymbols[i] || WinningSymbol.W == winningSymbols[i];
            if (!matches) {
                break;
            }
            count++;
        }
        return count;
    }
}