package com.example.atm.service.impl;

import com.example.atm.model.Account;
import com.example.atm.model.Person;
import com.example.atm.model.Role;
import com.example.atm.service.AccountService;
import com.example.atm.service.PersonService;
import com.example.atm.util.CardNumberGenerator;
import java.math.BigDecimal;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticateServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PersonService personService;

    @Mock
    private AccountService accountService;

    @Mock
    private CardNumberGenerator cardNumberGenerator;

    @InjectMocks
//    @Spy
    private AuthenticateServiceImpl authenticateService;

    private Person personWithoutId;
    private Person personWithId;
    private Account accountWithoutId;
    private Account accountWithId;

    @BeforeEach
    private void init() {
        when(cardNumberGenerator.randomCardGenerate()).thenReturn("1234567890");
        when(passwordEncoder.encode("123")).thenReturn("secret");

        accountWithoutId = new Account();
        accountWithoutId.setCardNumber(cardNumberGenerator.randomCardGenerate());
        accountWithoutId.setMoneySum(BigDecimal.valueOf(0));

        accountWithId = new Account();
        accountWithId.setMoneySum(accountWithoutId.getMoneySum());
        accountWithId.setCardNumber(accountWithoutId.getCardNumber());
        accountWithId.setId(1L);

        personWithoutId = new Person();
        personWithoutId.setName("user");
        personWithoutId.setPassword(passwordEncoder.encode("123"));
        personWithoutId.setRole(Role.USER);
        personWithoutId.addAccount(accountWithId);

        personWithId = new Person();
        personWithId.setName(personWithoutId.getName());
        personWithId.setPassword(personWithoutId.getPassword());
        personWithId.setRole(personWithoutId.getRole());
        personWithId.setAccounts(personWithoutId.getAccounts());
        personWithId.setId(1L);

        when(accountService.addAccount(accountWithoutId)).thenReturn(accountWithId);
        when(personService.addPerson(personWithoutId)).thenReturn(personWithId);

        MockitoAnnotations.initMocks(this);
    }

    @Test
    void register() {
        assertEquals(personWithId, authenticateService.register("user", "123"));
    }
}

