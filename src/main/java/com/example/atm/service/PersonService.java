package com.example.atm.service;

import com.example.atm.dto.PersonRequestDto;
import com.example.atm.model.Person;

public interface PersonService {
    Person getById(Long id);

    Person getByName(String name);

    Person addPerson(Person person);

    Person addNewAccount(PersonRequestDto personRequestDto);
}
