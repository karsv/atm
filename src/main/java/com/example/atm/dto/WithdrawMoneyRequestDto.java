package com.example.atm.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WithdrawMoneyRequestDto {
    private AtmRequestDto atm;
    private BigDecimal money;
    private AccountRequestDto account;
}
