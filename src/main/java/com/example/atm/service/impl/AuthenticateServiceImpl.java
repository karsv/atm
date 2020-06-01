package com.example.atm.service.impl;

import com.example.atm.dto.PersonResponseDto;
import com.example.atm.model.Account;
import com.example.atm.model.Person;
import com.example.atm.model.Role;
import com.example.atm.service.AccountService;
import com.example.atm.service.AuthenticateService;
import com.example.atm.service.PersonService;
import com.example.atm.util.CardNumberGenerator;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AuthenticateServiceImpl implements AuthenticateService {
    private final PersonService personService;
    private final PasswordEncoder passwordEncoder;
    private final AccountService accountService;
    private final CardNumberGenerator cardNumberGenerator;

    public AuthenticateServiceImpl(PersonService personService,
                                   PasswordEncoder passwordEncoder,
                                   AccountService accountService,
                                   CardNumberGenerator cardNumberGenerator) {
        this.personService = personService;
        this.passwordEncoder = passwordEncoder;
        this.accountService = accountService;
        this.cardNumberGenerator = cardNumberGenerator;
    }

    @Override
    public Person register(String name, String password) {
        Person person = new Person();
        person.setName(name);
        person.setPassword(passwordEncoder.encode(password));
        person.setRole(Role.USER);
        Account account = new Account();
        account.setMoneySum(BigDecimal.valueOf(0));
        account.setCardNumber(cardNumberGenerator.randomCardGenerate());
        accountService.addAccount(account);
        person.addAccount(account);
        return personService.addPerson(person);
    }

    @Override
    public Optional<PersonResponseDto> login(String name, String password) {
        Person person = personService.getByName(name);
        if (passwordEncoder.matches(password, person.getPassword())) {
            PersonResponseDto personResponseDto = new PersonResponseDto();
            personResponseDto.setName(name);
            personResponseDto.setPassword(password);
            personResponseDto.setId(person.getId());
            personResponseDto.setRole(person.getRole().toString());
            return Optional.of(personResponseDto);
        }
        return Optional.empty();
    }
}
