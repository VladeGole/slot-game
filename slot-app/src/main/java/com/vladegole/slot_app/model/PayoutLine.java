package com.vladegole.slot_app.model;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
@Data
public class PayoutLine {

    private Long id;

    private int[] rows;
}