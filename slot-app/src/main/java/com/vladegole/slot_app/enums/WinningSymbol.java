package com.vladegole.slot_app.enums;

import lombok.Getter;

@Getter
public enum WinningSymbol {
    A("A"),
    K("K"),
    Q("Q"),
    W("W");

   private final String value;

    WinningSymbol(String value) {
        this.value = value;
    }
}