package com.vladegole.slot_app.service;

import com.vladegole.slot_app.enums.WinningSymbol;
import org.springframework.stereotype.Service;

import java.security.SecureRandom;

@Service
public class ReelSpinService {

    private static final WinningSymbol[] SYMBOLS = { WinningSymbol.Q, WinningSymbol.A, WinningSymbol.K, WinningSymbol.W
            , WinningSymbol.W, WinningSymbol.A, WinningSymbol.Q, WinningSymbol.K, WinningSymbol.K, WinningSymbol.W };

    private final SecureRandom secureRandom = new SecureRandom();

    private static final int ROW = 3;
    private static final int COLUMN = 5;

    public WinningSymbol[][] reelSpin() {
        WinningSymbol[][] matrix = new WinningSymbol[ROW][COLUMN];

        for (int col = 0; col < COLUMN; col++) {
            int secureNumber = secureRandom.nextInt(SYMBOLS.length);
            int[] stopIndex = resolveStripIndexes(secureNumber);
            for (int row = 0; row < ROW; row++) {
                matrix[row][col] = SYMBOLS[stopIndex[row]];
            }
        }
        return matrix;
    }


    private int[] resolveStripIndexes(int startNumber) {
        int[] result = new int[ROW];
        for (int row = 0; row < ROW; row++) {
            result[row] = (startNumber + row) % SYMBOLS.length;
        }
        return result;
    }
}