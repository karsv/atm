package com.example.atm.service.impl;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AtmRequestDto;
import com.example.atm.exception.AccountException;
import com.example.atm.exception.AtmException;
import com.example.atm.model.Account;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import com.example.atm.repository.AtmRepository;
import com.example.atm.service.AccountService;
import java.math.BigDecimal;
import java.util.LinkedHashMap;
import java.util.Map;
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
class AtmServiceImplTest {
    @Mock
    private AccountService accountService;

    @Mock
    private AtmRepository atmRepository;

    @InjectMocks
    private AtmServiceImpl atmService;

    private Atm atmWithoutId;
    private Atm atmWithId;
    private Atm atmWithPutedCash;

    private AtmRequestDto realAtmRequestDto;
    private AtmRequestDto fakeAtmRequestDto;

    private Account account;

    private AccountRequestDto realAccountRequestDto;
    private AccountRequestDto fakeAccountRequestDto;

    private Map<Cash, Long> cash;
    private Map<Cash, Long> putedCash;
    private Map<Cash, Long> newCash;

    @BeforeEach
    private void init() {
        newCash = new LinkedHashMap<>();
        newCash.put(Cash.NOTE500, 12L);
        newCash.put(Cash.NOTE200, 12L);
        newCash.put(Cash.NOTE100, 12L);

        putedCash = new LinkedHashMap<>();
        putedCash.put(Cash.NOTE500, 2L);
        putedCash.put(Cash.NOTE200, 2L);
        putedCash.put(Cash.NOTE100, 2L);

        cash = new LinkedHashMap<>();
        cash.put(Cash.NOTE500, 10L);
        cash.put(Cash.NOTE200, 10L);
        cash.put(Cash.NOTE100, 10L);

        atmWithoutId = new Atm();
        atmWithoutId.setCash(cash);

        atmWithId = new Atm();
        atmWithId.setId(1L);
        atmWithId.setCash(cash);

        atmWithPutedCash = new Atm();
        atmWithPutedCash.setId(1L);
        atmWithPutedCash.setCash(newCash);

        account = new Account();
        account.setId(1L);
        account.setMoneySum(BigDecimal.valueOf(2000));

        realAccountRequestDto = new AccountRequestDto();
        realAccountRequestDto.setId(1L);

        fakeAccountRequestDto = new AccountRequestDto();
        fakeAccountRequestDto.setId(2L);

        realAtmRequestDto = new AtmRequestDto();
        realAtmRequestDto.setId(1L);

        fakeAtmRequestDto = new AtmRequestDto();
        fakeAtmRequestDto.setId(2L);
        when(atmRepository.save(atmWithoutId)).thenReturn(atmWithId);
        when(atmRepository.findById(1L)).thenReturn(Optional.of(atmWithId));
        when(atmRepository.findById(2L)).thenReturn(Optional.empty());
        when(atmRepository.save(atmWithPutedCash)).thenReturn(atmWithPutedCash);
        when(accountService.getAccount(realAccountRequestDto)).thenReturn(account);
        when(accountService.getAccount(fakeAccountRequestDto)).thenThrow(new AccountException("No such account!"));
    }

    @Test
    void addAtm() {
        assertEquals(atmWithId, atmService.addAtm(atmWithoutId));
    }

    @Test
    void getAtmById() {
        assertEquals(atmWithId, atmService.getAtmById(1L));
        assertThrows(AtmException.class, () -> atmService.getAtmById(2L));
    }

    @Test
    void putCashToAtm() {
        assertEquals(atmWithPutedCash, atmService.putCashToAtm(realAtmRequestDto, putedCash));
        assertThrows(AtmException.class, () -> atmService.putCashToAtm(fakeAtmRequestDto, putedCash));
    }

    @Test
    void getCashFromAtm() {
        assertEquals(cash, atmService.getCashFromAtm(realAtmRequestDto));
        assertThrows(AtmException.class, () -> atmService.getCashFromAtm(fakeAtmRequestDto));
    }

    @Test
    void withdrawMoney() {
        Map<Cash, Long> withdrawCash = new LinkedHashMap<>();
        withdrawCash.put(Cash.NOTE500, 8L);
        withdrawCash.put(Cash.NOTE200, 9L);
        withdrawCash.put(Cash.NOTE100, 9L);
        Atm atmWithdrawCash = new Atm();
        atmWithdrawCash.setId(1L);
        atmWithdrawCash.setCash(withdrawCash);
        when(atmRepository.save(atmWithdrawCash)).thenReturn(atmWithdrawCash);
        BigDecimal withdrawMoney = BigDecimal.valueOf(1300);
        BigDecimal overWithdrawMoney = BigDecimal.valueOf(5000);
        BigDecimal overAtmMoney = BigDecimal.valueOf(500000);

        assertEquals(atmWithdrawCash,
                atmService.withdrawMoney(realAtmRequestDto, withdrawMoney, realAccountRequestDto));
        assertThrows(AtmException.class, ()
                -> atmService.withdrawMoney(fakeAtmRequestDto, withdrawMoney, realAccountRequestDto));
        assertThrows(AccountException.class, ()
                -> atmService.withdrawMoney(realAtmRequestDto, withdrawMoney, fakeAccountRequestDto));
        assertThrows(AtmException.class, ()
                -> atmService.withdrawMoney(realAtmRequestDto, overWithdrawMoney, realAccountRequestDto));
        assertThrows(AtmException.class, ()
                -> atmService.withdrawMoney(realAtmRequestDto, overAtmMoney, realAccountRequestDto));
    }

    @Test
    void depositMoney() {
        Map<Cash, Long> depositedCash = new LinkedHashMap<>();
        depositedCash.put(Cash.NOTE500, 12L);
        depositedCash.put(Cash.NOTE200, 12L);
        depositedCash.put(Cash.NOTE100, 12L);
        Atm atmDepositedCash = new Atm();
        atmDepositedCash.setId(1L);
        atmDepositedCash.setCash(depositedCash);
        when(atmRepository.save(atmDepositedCash)).thenReturn(atmDepositedCash);
        when(accountService.putMoneyOnAccount(fakeAccountRequestDto,
                atmService.getSumOfCash(putedCash))).
                thenThrow(new AccountException("No such account exception!"));

        assertEquals(atmDepositedCash,
                atmService.depositMoney(realAtmRequestDto, putedCash, realAccountRequestDto));
        assertThrows(AccountException.class, ()
                -> atmService.depositMoney(realAtmRequestDto, putedCash, fakeAccountRequestDto));
        assertThrows(AtmException.class, ()
                -> atmService.depositMoney(fakeAtmRequestDto, putedCash, realAccountRequestDto));
    }
}
