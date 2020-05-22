package com.example.atm.dto;

import lombok.Data;

@Data
public class AtmResponseDto {
    private Long id;
    private String note100;
    private String note200;
    private String note500;
}
