package com.example.atm.dto;


import com.example.atm.model.Cash;
import java.util.Map;
import lombok.Data;

@Data
public class DepositMoneyRequestDto {
    private AtmRequestDto atm;
    private Map<Cash, Long> money;
    private AccountRequestDto account;
}
