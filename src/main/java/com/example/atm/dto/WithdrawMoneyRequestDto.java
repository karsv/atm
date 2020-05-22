package com.example.atm.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class WithdrawMoneyRequestDto {
    private Long atmId;
    private BigDecimal money;
    private Long accountId;
}
