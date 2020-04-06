package com.example.atm.security;

import com.example.atm.model.Person;
import com.example.atm.service.PersonService;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

@Service
public class CustomUserDetailService implements UserDetailsService {
    private final PersonService personService;

    public CustomUserDetailService(PersonService personService) {
        this.personService = personService;
    }

    @Override
    public UserDetails loadUserByUsername(String name) throws UsernameNotFoundException {
        Person person = personService.getByName(name);
        User.UserBuilder userBuilder = User.withUsername(name);
        userBuilder.password(person.getPassword());
        userBuilder.roles(person.getRole().toString());
        return userBuilder.build();
    }
}
