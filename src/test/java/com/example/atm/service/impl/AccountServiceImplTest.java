package com.example.atm.service.impl;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.exception.AccountException;
import com.example.atm.model.Account;
import com.example.atm.repository.AccountRepository;
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
class AccountServiceImplTest {
    @Mock
    private AccountRepository accountRepository;

    @InjectMocks
    private AccountServiceImpl accountService;

    private Account account;
    private Account accountWithoutId;
    private Account accountWithAddedMoney;
    private Account accountWithGettedMoney;

    private AccountRequestDto realAccountRequestDto;
    private AccountRequestDto destinationAccountRequestDto;
    private AccountRequestDto fakeAccountRequestDto;

    private BigDecimal oldSum;
    private BigDecimal newSumWithAddedMoney;
    private BigDecimal newSumWithGettedMoney;
    private BigDecimal operatedMoneyOnAccount;
    private BigDecimal overDraftMoney;

    @BeforeEach
    public void init() {
        oldSum = BigDecimal.valueOf(100);
        newSumWithAddedMoney = BigDecimal.valueOf(120);
        newSumWithGettedMoney = BigDecimal.valueOf(80);
        operatedMoneyOnAccount = BigDecimal.valueOf(20);
        overDraftMoney = BigDecimal.valueOf(210);

        account = new Account();
        account.setMoneySum(oldSum);
        account.setId(1L);

        accountWithoutId = new Account();
        accountWithoutId.setMoneySum(oldSum);

        accountWithAddedMoney = new Account();
        accountWithAddedMoney.setMoneySum(newSumWithAddedMoney);
        accountWithAddedMoney.setId(1L);

        accountWithGettedMoney = new Account();
        accountWithGettedMoney.setId(1L);
        accountWithGettedMoney.setMoneySum(newSumWithGettedMoney);

        realAccountRequestDto = new AccountRequestDto();
        realAccountRequestDto.setId(1L);
        fakeAccountRequestDto = new AccountRequestDto();
        fakeAccountRequestDto.setId(2L);
        destinationAccountRequestDto = new AccountRequestDto();
        destinationAccountRequestDto.setId(3L);
        when(accountRepository.save(accountWithoutId)).thenReturn(account);
        when(accountRepository.save(accountWithAddedMoney)).thenReturn(accountWithAddedMoney);
        when(accountRepository.save(accountWithGettedMoney)).thenReturn(accountWithGettedMoney);
        when(accountRepository.findById(1L)).thenReturn(Optional.of(account));
        when(accountRepository.findById(2L)).thenReturn(Optional.empty());
        when(accountRepository.findById(3L)).thenReturn(Optional.of(account));
    }

    @Test
    void addAccount() {
        assertEquals(account, accountService.addAccount(accountWithoutId));
        assertEquals(accountWithAddedMoney, accountService.addAccount(accountWithAddedMoney));
    }

    @Test
    void getAccount() {
        assertEquals(account, accountService.getAccount(realAccountRequestDto));
        assertThrows(AccountException.class, () -> {
            accountService.getAccount(fakeAccountRequestDto);
        });
    }

    @Test
    void putMoneyOnAccount() {
        assertEquals(accountWithAddedMoney,
                accountService.putMoneyOnAccount(realAccountRequestDto, operatedMoneyOnAccount));
        assertThrows(AccountException.class,
                () -> accountService.putMoneyOnAccount(fakeAccountRequestDto, operatedMoneyOnAccount));
    }

    @Test
    void getMoneyFromAccount() {
        assertThrows(AccountException.class,
                () -> accountService.getMoneyFromAccount(fakeAccountRequestDto, operatedMoneyOnAccount));
        assertThrows(AccountException.class,
                () -> accountService.getMoneyFromAccount(realAccountRequestDto, overDraftMoney));
        assertEquals(accountWithGettedMoney,
                accountService.getMoneyFromAccount(realAccountRequestDto, operatedMoneyOnAccount));
    }

    @Test
    void transferMoney() {
        assertEquals(accountWithGettedMoney,
                accountService.
                        transferMoney(operatedMoneyOnAccount, realAccountRequestDto, destinationAccountRequestDto));
        assertThrows(AccountException.class,
                () -> accountService.
                        transferMoney(operatedMoneyOnAccount, fakeAccountRequestDto, destinationAccountRequestDto));
        assertThrows(AccountException.class,
                () -> accountService.
                        transferMoney(operatedMoneyOnAccount, realAccountRequestDto, fakeAccountRequestDto));
        assertThrows(AccountException.class,
                () -> accountService.
                        transferMoney(overDraftMoney, realAccountRequestDto, destinationAccountRequestDto));
    }
}

