package com.example.atm.dto;

import lombok.Data;

@Data
public class DepositMoneyRequestDto {
    private Long atmId;
    private Long note100;
    private Long note200;
    private Long note500;
    private Long accountId;
}
