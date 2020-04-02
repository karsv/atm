package com.example.atm.controller;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.model.Account;
import com.example.atm.service.AccountService;
import com.example.atm.service.AtmService;
import java.math.BigDecimal;
import javax.annotation.PostConstruct;
import org.springframework.stereotype.Component;

@Component
public class InitController {
    private final AccountService accountService;
    private final AtmService atmService;

    public InitController(AccountService accountService, AtmService atmService) {
        this.accountService = accountService;
        this.atmService = atmService;
    }

    @PostConstruct
    private void firstInit() {
        Account account = new Account();
        account.setMoneySum(BigDecimal.valueOf(1000000.11));
        accountService.addAccount(account);
        AccountRequestDto accountRequestDto = new AccountRequestDto();
        accountRequestDto.setId(1L);
        accountService.putMoneyOnAccount(accountRequestDto, BigDecimal.valueOf(111));
        accountService.getMoneyFromAccount(accountRequestDto, BigDecimal.valueOf(1));

    }
}
