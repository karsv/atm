package com.example.atm.controller;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AccountResponseDto;
import com.example.atm.dto.PersonRequestDto;
import com.example.atm.model.Account;
import com.example.atm.service.AccountService;
import java.util.List;
import java.util.stream.Collectors;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    @PostMapping("/status")
    public AccountResponseDto getAccountStatus(@RequestBody AccountRequestDto accountRequestDto) {
        Account account = accountService.getAccount(accountRequestDto);
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId());
        accountResponseDto.setCardNumber(account.getCardNumber());
        accountResponseDto.setMoney(account.getMoneySum());
        return accountResponseDto;
    }

    @PostMapping("/get-status-all-accounts")
    public List<AccountResponseDto> getAccountStatus(
            @RequestBody PersonRequestDto personRequestDto) {
        return accountService.getPersonAccounts(personRequestDto).stream()
                .map(a -> convertAccountToReaponseDto(a))
                .collect(Collectors.toList());
    }

    private AccountResponseDto convertAccountToReaponseDto(Account account) {
        AccountResponseDto accountResponseDto = new AccountResponseDto();
        accountResponseDto.setId(account.getId());
        accountResponseDto.setCardNumber(account.getCardNumber());
        accountResponseDto.setMoney(account.getMoneySum());
        return accountResponseDto;
    }
}
