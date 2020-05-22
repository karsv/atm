package com.example.atm.security;

import com.example.atm.dto.PersonRegistrationDto;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

public class CustomPasswordEqualValidator implements
        ConstraintValidator<PasswordEqualValidator, PersonRegistrationDto> {
    @Override
    public boolean isValid(PersonRegistrationDto user,
                           ConstraintValidatorContext constraintValidatorContext) {
        return user.getPassword()
                .equals(user.getRepeatPassword());
    }
}
