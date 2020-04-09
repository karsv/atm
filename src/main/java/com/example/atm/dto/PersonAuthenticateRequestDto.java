package com.example.atm.dto;

import lombok.Data;

@Data
public class PersonAuthenticateRequestDto {
    public String name;
    public String password;
}
