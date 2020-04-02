package com.example.atm.service.impl;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AtmRequestDto;
import com.example.atm.exception.AtmException;
import com.example.atm.model.Account;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import com.example.atm.repository.AccountRepository;
import com.example.atm.repository.AtmRepository;
import com.example.atm.service.AccountService;
import com.example.atm.service.AtmService;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;
    private final AccountRepository accountRepository;
    private final AccountService accountService;

    public AtmServiceImpl(AtmRepository atmRepository, AccountRepository accountRepository,
                          AccountService accountService) {
        this.atmRepository = atmRepository;
        this.accountRepository = accountRepository;
        this.accountService = accountService;
    }

    @Override
    public Atm addAtm(Atm atm) {
        return atmRepository.save(atm);
    }

    @Override
    public Atm updateAtm(AtmRequestDto atm, Map<Cash, Long> cash) {
        Atm atmTemp = atmRepository.getOne(atm.getId());
        Map<Cash, Long> temp = atmTemp.getCash();
        temp.put(Cash.NOTE100, temp.get(Cash.NOTE100) + cash.get(Cash.NOTE100));
        temp.put(Cash.NOTE200, temp.get(Cash.NOTE200) + cash.get(Cash.NOTE200));
        temp.put(Cash.NOTE500, temp.get(Cash.NOTE500) + cash.get(Cash.NOTE500));
        atmTemp.setCash(temp);
        return atmRepository.save(atmTemp);
    }

    @Override
    public Map<Cash, Long> checkCashInAtm(AtmRequestDto atm) {
        return atmRepository.getOne(atm.getId()).getCash();
    }

    @Override
    public Atm withdrawMoney(AtmRequestDto atm, BigDecimal money, AccountRequestDto account) {
        if (!checkIfWithdrawIsCorrect(money)) {
            throw new AtmException("the amount should be a multiple of 100");
        }

        Account userAccount = accountService.getAccount(account);
        if (!checkIfEnoughMoneyOnAccount(userAccount, money)) {
            throw new AtmException("The isn't enough money on the account!");
        }

        BigDecimal cahInAtm = getSumOfCash(checkCashInAtm(atm));
        if (!checkIfEnoughCashInAtm(money, cahInAtm)) {
            throw new AtmException("Not enough money at the ATM!");
        }

        accountService.getMoneyFromAccount(account, money);

        Atm tempAtm = atmRepository.getOne(atm.getId());
        Map<Cash, Long> cashAtmTemp = tempAtm.getCash();

        Long n500 = cashAtmTemp.get(Cash.NOTE500);
        Long n200 = cashAtmTemp.get(Cash.NOTE200);
        Long n100 = cashAtmTemp.get(Cash.NOTE100);

        Long exchange = money.longValue();

        while (exchange != 0) {
            if (exchange > 500 && n500 > 0) {
                exchange -= 500;
                n500--;
            } else if (exchange > 200 && n200 > 0) {
                exchange -= 200;
                n200--;
            } else {
                exchange -= 100;
                n100--;
            }
        }

        cashAtmTemp.put(Cash.NOTE500, n500);
        cashAtmTemp.put(Cash.NOTE200, n200);
        cashAtmTemp.put(Cash.NOTE100, n100);
        tempAtm.setCash(cashAtmTemp);

        return atmRepository.save(tempAtm);
    }

    private boolean checkIfEnoughCashInAtm(BigDecimal money, BigDecimal cash) {
        return money.compareTo(cash) <= 0;
    }

    private boolean checkIfWithdrawIsCorrect(BigDecimal money) {
        return money.longValue() % 100 <= 0;
    }

    private boolean checkIfEnoughMoneyOnAccount(Account account, BigDecimal money) {
        return money.compareTo(account.getMoneySum()) <= 0;
    }

    private BigDecimal getSumOfCash(Map<Cash, Long> cash) {
        Long sum = 0L;
        for (Map.Entry<Cash, Long> entry : cash.entrySet()) {
            sum += (Integer.valueOf(entry.getKey().toString().substring(4)) * entry.getValue());
        }
        return BigDecimal.valueOf(sum);
    }

    @Override
    public Atm depositMoney(AtmRequestDto atm, Map<Cash, Long> money, AccountRequestDto account) {
        Atm atmTemp = atmRepository.getOne(atm.getId());
        Map<Cash, Long> newAtmCash = atmTemp.getCash();
        for (Map.Entry<Cash, Long> entry : money.entrySet()) {
            if (newAtmCash.containsKey(entry.getKey())) {
                Long sumOfNoteInAtm = entry.getValue() + newAtmCash.get(entry.getKey());
                newAtmCash.put(entry.getKey(), sumOfNoteInAtm);
            }
        }
        atmTemp.setCash(newAtmCash);

        accountService.putMoneyOnAccount(account, getSumOfCash(money));

        return atmRepository.save(atmTemp);
    }

    @Override
    public void transferMoney(BigDecimal money,
                              AccountRequestDto ownerAccountDto,
                              AccountRequestDto destinationAccountDto) {
        Account ownerAccount = accountService.getAccount(ownerAccountDto);
        Account destinationAccount = accountService.getAccount(destinationAccountDto);

        checkIfEnoughMoneyOnAccount(ownerAccount, money);

        try {
            accountService.getMoneyFromAccount(ownerAccountDto, money);
            accountService.putMoneyOnAccount(destinationAccountDto, money);
        } catch (Exception e) {
            accountService.addAccount(ownerAccount);
            accountService.addAccount(destinationAccount);
            throw new AtmException("Can't transfer money!", e);
        }
    }
}
