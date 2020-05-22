package com.example.atm.service;

import com.example.atm.dto.PersonResponseDto;
import com.example.atm.model.Person;
import java.util.Optional;

public interface AuthenticateService {
    Person register(String name, String password);

    Optional<PersonResponseDto> login(String name, String password);
}
