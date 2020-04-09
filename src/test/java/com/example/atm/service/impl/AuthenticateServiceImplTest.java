package com.example.atm.service.impl;

import com.example.atm.model.Person;
import com.example.atm.model.Role;
import com.example.atm.service.PersonService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.when;

@SpringBootTest
class AuthenticateServiceImplTest {
    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private PersonService personService;

    @InjectMocks
    private AuthenticateServiceImpl authenticateService;

    private Person personWithoutId;
    private Person personWithId;

    {
        personWithoutId = new Person();
        personWithoutId.setName("user");
        personWithoutId.setRole(Role.USER);

        personWithId = new Person();
        personWithId.setName("user");
        personWithId.setRole(Role.USER);
        personWithId.setId(1L);
    }

    @BeforeEach
    private void init() {
        when(personService.addPerson(personWithoutId)).thenReturn(personWithId);
        when(passwordEncoder.encode("123")).thenReturn("secret");
    }

    @Test
    void register() {
        personWithId.setPassword(passwordEncoder.encode("123"));
        assertEquals(personWithId, authenticateService.register("user", "132"));
    }
}