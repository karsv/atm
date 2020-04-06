package com.example.atm.service.impl;

import com.example.atm.dto.PersonRequestDto;
import com.example.atm.exception.PersonException;
import com.example.atm.model.Account;
import com.example.atm.model.Person;
import com.example.atm.repository.PersonRepository;
import com.example.atm.service.AccountService;
import com.example.atm.service.PersonService;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class PersonServiceImpl implements PersonService {
    private final PersonRepository personRepository;
    private final AccountService accountService;

    public PersonServiceImpl(PersonRepository personRepository, AccountService accountService) {
        this.personRepository = personRepository;
        this.accountService = accountService;
    }

    @Override
    public Person getById(Long id) {
        Optional<Person> person = personRepository.findById(id);
        if (person.isEmpty()) {
            throw new PersonException("No person with such id");
        }
        return person.get();
    }

    @Override
    public Person getByName(String name) {
        return personRepository.findByName(name);
    }

    @Override
    public Person addPerson(Person person) {
        return personRepository.save(person);
    }

    @Override
    public Person addNewAccount(PersonRequestDto personRequestDto) {
        Person person = getById(personRequestDto.getId());
        Account account = new Account();
        person.addAccount(accountService.addAccount(account));
        return personRepository.save(person);
    }
}
