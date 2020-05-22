package com.example.atm.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransferMoneyRequestDto {
    private Long ownerAccountId;
    private BigDecimal money;
    private Long destinationAccountId;
}
