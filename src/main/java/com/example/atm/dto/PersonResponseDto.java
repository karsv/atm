package com.example.atm.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class PersonResponseDto {
    private Long id;
    private String name;
    private String password;
    private String role;
}
