package com.example.atm.controller;

import com.example.atm.dto.AtmRequestDto;
import com.example.atm.model.Account;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import com.example.atm.model.Person;
import com.example.atm.model.Role;
import com.example.atm.service.AccountService;
import com.example.atm.service.AtmService;
import com.example.atm.service.PersonService;
import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;
import javax.annotation.PostConstruct;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Component;

@Component
public class InitController {
    private final AccountService accountService;
    private final AtmService atmService;
    private final PasswordEncoder passwordEncoder;
    private final PersonService personService;

    public InitController(AccountService accountService, AtmService atmService,
                          PasswordEncoder passwordEncoder, PersonService personService) {
        this.accountService = accountService;
        this.atmService = atmService;
        this.passwordEncoder = passwordEncoder;
        this.personService = personService;
    }

    @PostConstruct
    private void firstInit() {
        Account account1 = new Account();
        account1.setMoneySum(BigDecimal.valueOf(1000000.11));
        accountService.addAccount(account1);

        Account account2 = new Account();
        account2.setMoneySum(BigDecimal.valueOf(0));
        accountService.addAccount(account2);

        Person admin = new Person();
        admin.setName("admin");
        admin.setPassword(passwordEncoder.encode("123"));
        admin.setRole(Role.ADMIN);
        personService.addPerson(admin);

        Person person = new Person();
        person.setName("user");
        person.setPassword(passwordEncoder.encode("123"));
        person.addAccount(account1);
        person.setRole(Role.USER);
        personService.addPerson(person);


        Atm atm = new Atm();
        atmService.addAtm(atm);
        Map<Cash, Long> cash = new HashMap<>();
        cash.put(Cash.NOTE100, 10L);
        cash.put(Cash.NOTE200, 10L);
        cash.put(Cash.NOTE500, 10L);
        AtmRequestDto atmRequestDto = new AtmRequestDto();
        atmRequestDto.setId(1L);
        atmService.putCashToAtm(atmRequestDto, cash);
    }
}
