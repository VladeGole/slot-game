package com.vladegole.slot_app.config;

import com.vladegole.slot_app.enums.WinningSymbol;
import com.vladegole.slot_app.model.PayoutLine;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public class PayoutLineConfig {
    public final static List<PayoutLine> LINE = List.of( new PayoutLine(1L, new int[] {0,0,0,0,0}),
                                                         new PayoutLine(2L, new int[] {1,1,1,1,1}),
                                                         new PayoutLine(3L, new int[] {2,2,2,2,2}),
                                                         new PayoutLine(4L, new int[] {0,1,2,1,0}),
                                                         new PayoutLine(5L, new int[] {2,1,0,1,2}));

    public static final Map<WinningSymbol, Map<Integer, BigDecimal>> SPIN_WIN = Map.of(
            WinningSymbol.A, Map.of(
                    5, BigDecimal.valueOf(10),
                    4, BigDecimal.valueOf(5),
                    3, BigDecimal.valueOf(2)
            ),
            WinningSymbol.K, Map.of(
                    5, BigDecimal.valueOf(5),
                    4, BigDecimal.valueOf(3),
                    3, BigDecimal.valueOf(1)
            ),
            WinningSymbol.Q, Map.of(
                    5, BigDecimal.valueOf(2),
                    4, BigDecimal.valueOf(1),
                    3, BigDecimal.valueOf(0.5)
            )
    );
}