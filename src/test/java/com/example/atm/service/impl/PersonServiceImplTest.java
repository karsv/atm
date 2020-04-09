package com.example.atm.service.impl;

import com.example.atm.dto.PersonRequestDto;
import com.example.atm.exception.PersonException;
import com.example.atm.model.Account;
import com.example.atm.model.Person;
import com.example.atm.model.Role;
import com.example.atm.repository.PersonRepository;
import java.math.BigDecimal;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.when;

@SpringBootTest
class PersonServiceImplTest {
    @Mock
    private PersonRepository personRepository;

    @Mock
    private AccountServiceImpl accountService;

    @InjectMocks
    private PersonServiceImpl personService;

    private Person personWithoutId;
    private Person personWithId;
    private Person personWithAccount;

    private PersonRequestDto personRequestDto;

    private Account newAccount;

    {
        personWithoutId = new Person();
        personWithoutId.setName("personWithoutId");
        personWithoutId.setPassword("123");
        personWithoutId.setRole(Role.USER);

        personWithId = new Person();
        personWithId.setName("personWithId");
        personWithId.setPassword("123");
        personWithId.setId(1L);
        personWithId.setRole(Role.USER);

        personRequestDto = new PersonRequestDto();
        personRequestDto.setId(3L);

        personWithAccount = new Person();
        personWithAccount.setName("personWithAccount");
        personWithAccount.setPassword("123");
        personWithAccount.setId(1L);
        personWithAccount.setRole(Role.USER);
        newAccount = new Account();
        newAccount.setId(1L);
        newAccount.setMoneySum(BigDecimal.valueOf(100));
        personWithAccount.addAccount(newAccount);
    }

    @BeforeEach
    private void init() {
        when(personRepository.save(personWithoutId)).thenReturn(personWithId);
        when(personRepository.save(personWithAccount)).thenReturn(personWithAccount);
        when(personRepository.save(personWithId)).thenReturn(personWithId);

        when(personRepository.findById(1L)).thenReturn(Optional.of(personWithId));
        when(personRepository.findByName("personWithId")).thenReturn(Optional.of(personWithId));
        when(personRepository.findById(2L)).thenReturn(Optional.empty());
        when(personRepository.findById(3L)).thenReturn(Optional.of(personWithAccount));

        when(accountService.addAccount(new Account())).thenReturn(newAccount);
    }

    @Test
    void getById() {
        assertEquals(personWithId, personService.getById(1L));
        assertThrows(PersonException.class, () -> personService.getById(2L));
    }

    @Test
    void getByName() {
        assertEquals(personWithId, personService.getByName("personWithId"));
        assertThrows(PersonException.class, () -> personService.getByName("fakePerson"));
    }

    @Test
    void addPerson() {
        assertEquals(personWithId, personService.addPerson(personWithoutId));
        assertEquals(personWithAccount, personService.addPerson(personWithAccount));
    }

    @Test
    void addNewAccount() {
        assertEquals(personWithAccount, personService.addNewAccount(personRequestDto));
    }
}