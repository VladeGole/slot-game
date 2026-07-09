package com.vladegole.slot_app.api;

import com.vladegole.slot_app.dto.SpinRequestDto;
import com.vladegole.slot_app.dto.SpinWinningResponseDto;
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
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/spins")
public class SpinController {

    @Autowired
    private SpinService spinService;

    /**
     * Execute a slot spin with a fixed wager.
     *
     * @param spinRequestDto request for placing a spin, must contain the fixed bet amount
     * @return The ResponseEntity object containing the resulting matrix, winning lines,
     *         total win, and updated balance
     */
    @Operation(summary = "Spin the reels", description = "Charges the fixed wager, spins the 5x3 reel matrix, evaluates all paylines, and credits any winnings.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Spin result", content = @Content(schema = @Schema(implementation = SpinWinningResponseDto.class))),
            @ApiResponse(responseCode = "400", description = "Invalid bet amount or insufficient balance"),
            @ApiResponse(responseCode = "500", description = "Server error") })
    @PostMapping
    public ResponseEntity<?> spin(@Valid @RequestBody SpinRequestDto spinRequestDto) {
        SpinWinningResponseDto spinWinningResponseDto = spinService.spin(spinRequestDto);
        return ResponseEntity.status(HttpStatus.OK).body(spinWinningResponseDto);
    }
}