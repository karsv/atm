package com.example.atm.service;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.model.Account;
import java.math.BigDecimal;

public interface AccountService {
    Account addAccount(Account account);

    Account getAccount(AccountRequestDto account);

    Account putMoneyOnAccount(AccountRequestDto accountRequestDto, BigDecimal money);

    Account getMoneyFromAccount(AccountRequestDto accountRequestDto, BigDecimal money);

    Account transferMoney(BigDecimal money,
                          AccountRequestDto ownerAccountDto,
                          AccountRequestDto destinationAccountDto);
}
