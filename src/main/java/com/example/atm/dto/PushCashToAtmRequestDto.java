package com.example.atm.dto;

import lombok.Data;

@Data
public class PushCashToAtmRequestDto {
    private Long id;
    private Long note100;
    private Long note200;
    private Long note500;
}
