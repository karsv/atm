package com.example.atm.controller;

import com.example.atm.dto.PersonAuthenticateRequestDto;
import com.example.atm.model.Person;
import com.example.atm.service.AuthenticateService;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class AuthenticateController {
    private final AuthenticateService authenticateService;

    public AuthenticateController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/register")
    public Person register(@RequestBody PersonAuthenticateRequestDto person) {
        return authenticateService.register(person.name, person.password);
    }
}
