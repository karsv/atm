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
import com.example.atm.util.CardNumberGenerator;
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
    private final CardNumberGenerator cardNumberGenerator;

    public InitController(AccountService accountService, AtmService atmService,
                          PasswordEncoder passwordEncoder, PersonService personService,
                          CardNumberGenerator cardNumberGenerator) {
        this.accountService = accountService;
        this.atmService = atmService;
        this.passwordEncoder = passwordEncoder;
        this.personService = personService;
        this.cardNumberGenerator = cardNumberGenerator;
    }

    @PostConstruct
    private void firstInit() {
        Account account1 = new Account();
        account1.setMoneySum(BigDecimal.valueOf(1000000.11));
        account1.setCardNumber(cardNumberGenerator.randomCardGenerate());
        accountService.addAccount(account1);

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

        Atm atm1 = new Atm();
        atmService.add(atm1);
        Map<Cash, Long> cash = new HashMap<>();
        cash.put(Cash.NOTE100, 10L);
        cash.put(Cash.NOTE200, 10L);
        cash.put(Cash.NOTE500, 10L);
        AtmRequestDto atmRequestDto = new AtmRequestDto();
        atmRequestDto.setId(1L);
        atmService.putCash(atmRequestDto, cash);

        Atm atm2 = new Atm();
        atmService.add(atm2);
        Map<Cash, Long> cash2 = new HashMap<>();
        cash2.put(Cash.NOTE100, 123L);
        cash2.put(Cash.NOTE200, 123L);
        cash2.put(Cash.NOTE500, 123L);
        AtmRequestDto atmRequestDto2 = new AtmRequestDto();
        atmRequestDto2.setId(2L);
        atmService.putCash(atmRequestDto2, cash2);
    }
}
