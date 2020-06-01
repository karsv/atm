package com.example.atm.dto;

import java.math.BigDecimal;
import lombok.Data;

@Data
public class AccountResponseDto {
    Long id;
    String cardNumber;
    BigDecimal money;
}
