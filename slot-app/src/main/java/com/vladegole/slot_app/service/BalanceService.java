package com.vladegole.slot_app.service;

import com.vladegole.slot_app.dto.BalanceResponseDto;
import com.vladegole.slot_app.dto.DepositRequestDto;
import com.vladegole.slot_app.dto.DepositResponseDto;
import com.vladegole.slot_app.mapper.DepositMapper;
import com.vladegole.slot_app.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;

@Service
public class BalanceService {

    private final User user = new User();

    @Autowired
    private DepositMapper depositMapper;


    public synchronized DepositResponseDto deposit(DepositRequestDto depositRequestDto) {
        user.setBalance(user.getBalance().add(depositRequestDto.getAmount()));
        return depositMapper.toResponseDto(depositRequestDto);
    }

    public BalanceResponseDto getBalance() {
        return BalanceResponseDto.builder().balance(user.getBalance()).build();
    }

    public synchronized void reduceBalance(int betAmount) {
       BigDecimal currentBalance = user.getBalance();
       if (currentBalance.compareTo(BigDecimal.valueOf(betAmount)) < 0) {
           throw new IllegalArgumentException("You need to pay a deposit");
       }
        user.setBalance(user.getBalance().subtract(BigDecimal.valueOf(betAmount)));
    }

    public synchronized BigDecimal addWin(BigDecimal win) {
        user.setBalance(user.getBalance().add(win));
        return user.getBalance();
    }
}