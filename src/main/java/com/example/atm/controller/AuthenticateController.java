package com.example.atm.controller;

import com.example.atm.dto.PersonAuthenticateRequestDto;
import com.example.atm.dto.PersonRegistrationDto;
import com.example.atm.dto.PersonResponseDto;
import com.example.atm.exception.AuthenticateException;
import com.example.atm.model.Person;
import com.example.atm.service.AuthenticateService;
import java.util.List;
import javax.naming.AuthenticationException;
import javax.validation.Valid;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
public class AuthenticateController {
    private final AuthenticateService authenticateService;

    public AuthenticateController(AuthenticateService authenticateService) {
        this.authenticateService = authenticateService;
    }

    @PostMapping("/register")
    public Person register(@Valid @RequestBody PersonRegistrationDto person)
            throws AuthenticationException {
        try {
            return authenticateService.register(person.getName(), person.getPassword());
        } catch (DataIntegrityViolationException e) {
            throw new AuthenticationException("There is user with such name!");
        } catch (RuntimeException e) {
            throw new AuthenticationException(e.getMessage());
        }
    }

    @PostMapping("/login")
    public PersonResponseDto login(@RequestBody PersonAuthenticateRequestDto person) {
        return authenticateService
                .login(person.getName(), person.getPassword())
                .orElseThrow(() -> new AuthenticateException());
    }

    @ExceptionHandler(MethodArgumentNotValidException.class)
    @ResponseStatus(HttpStatus.BAD_REQUEST)
    public AuthenticateException validationError(MethodArgumentNotValidException exception) {
        String message =exception.getBindingResult().getFieldErrors().get(1).getDefaultMessage();
        return new AuthenticateException(message);
    }
}
