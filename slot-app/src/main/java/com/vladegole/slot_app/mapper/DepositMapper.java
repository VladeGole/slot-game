package com.vladegole.slot_app.mapper;

import com.vladegole.slot_app.dto.DepositRequestDto;
import com.vladegole.slot_app.dto.DepositResponseDto;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface DepositMapper {

    DepositResponseDto toResponseDto(DepositRequestDto depositRequestDto);
}