package com.example.atm.service;

import com.example.atm.model.Person;

public interface AuthenticateService {
    Person register(String name, String password);
}
