package com.example.atm.dto;

import com.example.atm.security.PasswordEqualValidator;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import lombok.Data;

@Data
@PasswordEqualValidator
public class PersonRegistrationDto {
    @NotNull(message = "User must have name!")
    @Size(min = 4)
    private String name;
    @NotNull(message = "You must type password!")
    @Size(min = 3, message = "Password must be more than 3 symbols!")
    private String password;
    @NotNull(message = "You must repeat password!")
    @Size(min = 3)
    private String repeatPassword;
}
