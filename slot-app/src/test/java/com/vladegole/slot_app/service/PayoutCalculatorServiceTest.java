package com.vladegole.slot_app.service;

import com.vladegole.slot_app.enums.WinningSymbol;
import com.vladegole.slot_app.model.PayoutLine;
import com.vladegole.slot_app.model.WinningLine;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.util.EnumMap;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;

public class PayoutCalculatorServiceTest {

        private PayoutCalculatorService payoutCalculatorService;
        private Map<WinningSymbol, Map<Integer, BigDecimal>> priceTable;

        @BeforeEach
        void setUp() {
            payoutCalculatorService = new PayoutCalculatorService();
            priceTable = buildPriceTable();
        }

        /**
         * Payout table exactly as defined in the task spec:
         * A: 5=10x, 4=5x,  3=2x
         * K: 5=5x,  4=3x,  3=1x
         * Q: 5=2x,  4=1x,  3=0.5x
         */
        private Map<WinningSymbol, Map<Integer, BigDecimal>> buildPriceTable() {
            Map<WinningSymbol, Map<Integer, BigDecimal>> table = new EnumMap<>(WinningSymbol.class);

            Map<Integer, BigDecimal> a = new HashMap<>();
            a.put(5, BigDecimal.valueOf(10));
            a.put(4, BigDecimal.valueOf(5));
            a.put(3, BigDecimal.valueOf(2));
            table.put(WinningSymbol.A, a);

            Map<Integer, BigDecimal> k = new HashMap<>();
            k.put(5, BigDecimal.valueOf(5));
            k.put(4, BigDecimal.valueOf(3));
            k.put(3, BigDecimal.valueOf(1));
            table.put(WinningSymbol.K, k);

            Map<Integer, BigDecimal> q = new HashMap<>();
            q.put(5, BigDecimal.valueOf(2));
            q.put(4, BigDecimal.valueOf(1));
            q.put(3, BigDecimal.valueOf(0.5));
            table.put(WinningSymbol.Q, q);

            return table;
        }

        @Test
        @DisplayName("Scenario A: pure win - Line 1 [K,K,K,Q,A] pays 3-of-a-kind for K = 1")
        void scenarioA_pureWin() {
            PayoutLine line1 = new PayoutLine(1L, new int[]{0, 0, 0, 0, 0}); // top row

            WinningSymbol[][] matrix = new WinningSymbol[3][5];
            matrix[0] = new WinningSymbol[]{
                    WinningSymbol.K, WinningSymbol.K, WinningSymbol.K, WinningSymbol.Q, WinningSymbol.A
            };

            WinningLine result = payoutCalculatorService.evaluateLine(matrix, priceTable, line1);

            assertNotNull(result, "Line 1 should win");
            assertEquals(1L, result.getLineId());
            assertEquals(WinningSymbol.K, result.getWinningSymbol());
            assertEquals(0, BigDecimal.ONE.compareTo(result.getPayout()),
                    "Payout should be 1 (3-of-a-kind K = 1x)");
        }

        @Test
        @DisplayName("Scenario B: wild substitution - Line 3 [A,W,A,A,Q] pays 4-of-a-kind for A = 5")
        void scenarioB_wildSubstitution() {
            PayoutLine line3 = new PayoutLine(3L, new int[]{2, 2, 2, 2, 2}); // bottom row

            WinningSymbol[][] matrix = new WinningSymbol[3][5];
            matrix[2] = new WinningSymbol[]{
                    WinningSymbol.A, WinningSymbol.W, WinningSymbol.A, WinningSymbol.A, WinningSymbol.Q
            };

            WinningLine result = payoutCalculatorService.evaluateLine(matrix, priceTable, line3);

            assertNotNull(result, "Line 3 should win");
            assertEquals(WinningSymbol.A, result.getWinningSymbol(),
                    "The Wild substitutes for A here");
            assertEquals(0, BigDecimal.valueOf(5).compareTo(result.getPayout()),
                    "Payout should be 5 (4-of-a-kind A = 5x)");
        }

        @Test
        @DisplayName("Scenario C: all wilds - Line 4 (V-shape) [W,W,W,W,W] scores as A, 5-of-a-kind = 10")
        void scenarioC_allWilds() {
            PayoutLine line4 = new PayoutLine(4L, new int[]{0, 1, 2, 1, 0}); // V-shape

            WinningSymbol[][] matrix = new WinningSymbol[3][5];
            matrix[0] = new WinningSymbol[5];
            matrix[1] = new WinningSymbol[5];
            matrix[2] = new WinningSymbol[5];

            matrix[0][0] = WinningSymbol.W;
            matrix[1][1] = WinningSymbol.W;
            matrix[2][2] = WinningSymbol.W;
            matrix[1][3] = WinningSymbol.W;
            matrix[0][4] = WinningSymbol.W;

            WinningLine result = payoutCalculatorService.evaluateLine(matrix, priceTable, line4);

            assertNotNull(result, "Line 4 should win");
            assertEquals(WinningSymbol.A, result.getWinningSymbol(),
                    "An all-Wild line is scored as the highest paying symbol (A)");
            assertEquals(0, BigDecimal.valueOf(10).compareTo(result.getPayout()),
                    "Payout should be 10 (5-of-a-kind A = 10x)");
        }

        @Test
        @DisplayName("Losing line - fewer than 3 consecutive matches returns null")
        void losingLine_returnsNull() {
            PayoutLine line1 = new PayoutLine(1L, new int[]{0, 0, 0, 0, 0});

            WinningSymbol[][] matrix = new WinningSymbol[3][5];
            matrix[0] = new WinningSymbol[]{
                    WinningSymbol.A, WinningSymbol.K, WinningSymbol.Q, WinningSymbol.A, WinningSymbol.A
            };

            WinningLine result = payoutCalculatorService.evaluateLine(matrix, priceTable, line1);

            assertNull(result, "Only 1 leading match (A) - fewer than 3 consecutive should not win");
        }
    }