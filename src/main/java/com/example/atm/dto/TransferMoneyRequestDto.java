package com.example.atm.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class TransferMoneyRequestDto {
    private BigDecimal money;
    private AccountRequestDto ownerAccount;
    private AccountRequestDto destinationAccount;
}
