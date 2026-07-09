package com.vladegole.slot_app.api;

import com.vladegole.slot_app.dto.*;
import com.vladegole.slot_app.service.BalanceService;
import com.vladegole.slot_app.service.SpinService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/balances")
public class BalanceController {

    @Autowired
    private BalanceService balanceService;

    @Autowired
    private SpinService spinService;

    /**
     * Deposit funds into the player's account.
     *
     * @param depositRequestDto request containing the amount to deposit
     * @return The ResponseEntity object containing the deposited amount
     */
    @Operation(summary = "Deposit funds", description = "Adds the given amount to the player's current balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Deposit successful", content = @Content(schema = @Schema(implementation = DepositResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid deposit amount (e.g. missing or below minimum)"),
            @ApiResponse(responseCode = "500", description = "Server error") })
    @PostMapping("/deposit")
    public ResponseEntity<?> postAmount(@Valid @RequestBody DepositRequestDto depositRequestDto) {
        DepositResponseDto responseDto = balanceService.deposit(depositRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(responseDto);
    }

    /**
     * Get the player's current balance.
     *
     * @return The ResponseEntity object containing the current balance
     */
    @Operation(summary = "Get current balance", description = "Returns the player's current account balance.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Current balance", content = @Content(schema = @Schema(implementation = BalanceResponseDto.class))),
            @ApiResponse(responseCode = "500", description = "Server error") })
    @GetMapping
    public ResponseEntity<?> getBalance() {
        BalanceResponseDto balanceResponseDto = balanceService.getBalance();
        return ResponseEntity.status(HttpStatus.OK).body(balanceResponseDto);
    }
}