package com.example.atm.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AccountResponseDto {
    String cardNumber;
    BigDecimal money;
}
