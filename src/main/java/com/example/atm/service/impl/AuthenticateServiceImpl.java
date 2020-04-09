package com.example.atm.service.impl;

import com.example.atm.model.Person;
import com.example.atm.model.Role;
import com.example.atm.service.AuthenticateService;
import com.example.atm.service.PersonService;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;

    public AuthenticateServiceImpl(PersonService personService, PasswordEncoder passwordEncoder) {
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Person register(String name, String password) {
        Person person = new Person();
        person.setName(name);
        person.setPassword(passwordEncoder.encode(password));
        person.setRole(Role.USER);
        return personService.addPerson(person);
    }
}
