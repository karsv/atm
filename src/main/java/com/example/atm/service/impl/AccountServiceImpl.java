package com.example.atm.service.impl;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.exception.AccountException;
import com.example.atm.model.Account;
import com.example.atm.repository.AccountRepository;
import com.example.atm.service.AccountService;
import java.math.BigDecimal;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class AccountServiceImpl implements AccountService {
    private final AccountRepository accountRepository;

    public AccountServiceImpl(AccountRepository accountRepository) {
        this.accountRepository = accountRepository;
    }

    @Override
    public Account addAccount(Account account) {
        return accountRepository.save(account);
    }

    @Override
    public Account getAccount(AccountRequestDto account) {
        if (accountRepository.findById(account.getId()).isPresent()) {
            return accountRepository.findById(account.getId()).get();
        } else {
            throw new AccountException("There isn't such account!");
        }
    }

    @Override
    public Account putMoneyOnAccount(AccountRequestDto accountRequestDto, BigDecimal money) {
        Account account = getAccount(accountRequestDto);
        account.setMoneySum(account.getMoneySum().add(money));
        return accountRepository.save(account);
    }

    @Override
    public Account getMoneyFromAccount(AccountRequestDto accountRequestDto, BigDecimal money) {
        Optional<Account> temp = accountRepository.findById(accountRequestDto.getId());
        if (temp.isEmpty()) {
            throw new AccountException("There isn't such account!");
        }
        Account account = temp.get();
        if (money.compareTo(account.getMoneySum()) > 0) {
            throw new AccountException("Not enough money at account: " + accountRequestDto.getId());
        }
        account.setMoneySum(account.getMoneySum().subtract(money));
        return accountRepository.save(account);
    }
}
