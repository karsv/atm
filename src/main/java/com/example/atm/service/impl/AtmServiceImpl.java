package com.example.atm.service.impl;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AtmRequestDto;
import com.example.atm.exception.AtmException;
import com.example.atm.model.Account;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import com.example.atm.repository.AtmRepository;
import com.example.atm.service.AccountService;
import com.example.atm.service.AtmService;
import java.math.BigDecimal;
import java.util.Map;
import org.springframework.stereotype.Service;

@Service
public class AtmServiceImpl implements AtmService {
    private final AtmRepository atmRepository;
    private final AccountService accountService;

    public AtmServiceImpl(AtmRepository atmRepository,
                          AccountService accountService) {
        this.atmRepository = atmRepository;
        this.accountService = accountService;
    }

    @Override
    public Atm addAtm(Atm atm) {
        return atmRepository.save(atm);
    }

    @Override
    public Atm getAtmById(Long id) {
        if (atmRepository.findById(id).isEmpty()) {
            throw new AtmException("No such ATM!");
        }
        return atmRepository.findById(id).get();
    }

    @Override
    public Atm putCashToAtm(AtmRequestDto atm, Map<Cash, Long> cash) {
        Atm atmTemp = getAtmById(atm.getId());

        Map<Cash, Long> temp = atmTemp.getCash();
        for (Map.Entry<Cash, Long> entry : cash.entrySet()) {
            if (temp.containsKey(entry.getKey())) {
                temp.put(entry.getKey(), (temp.get(entry.getKey()) + cash.get(entry.getKey())));
            }
        }
        atmTemp.setCash(temp);
        return atmRepository.save(atmTemp);
    }

    @Override
    public Map<Cash, Long> getCashFromAtm(AtmRequestDto atm) {
        return getAtmById(atm.getId()).getCash();
    }

    @Override
    public Atm withdrawMoney(AtmRequestDto atm, BigDecimal money, AccountRequestDto account) {
        Atm atmTemp = getAtmById(atm.getId());
        if (!checkIfWithdrawIsCorrect(money, atmTemp)) {
            throw new AtmException("the amount should be divider by "
                    + getCashDivider(atmTemp, money));
        }

        Account userAccount = accountService.getAccount(account);
        if (!checkIfEnoughMoneyOnAccount(userAccount, money)) {
            throw new AtmException("The isn't enough money on the account!");
        }

        BigDecimal cashInAtm = getSumOfCash(getCashFromAtm(atm));
        if (!checkIfEnoughCashInAtm(money, cashInAtm)) {
            throw new AtmException("Not enough money at the ATM!");
        }

        accountService.getMoneyFromAccount(account, money);

        return atmRepository.save(getNotesFromAtm(atmTemp, money));
    }

    @Override
    public Atm depositMoney(AtmRequestDto atm, Map<Cash, Long> money, AccountRequestDto account) {
        Atm atmTemp = getAtmById(atm.getId());
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

    private Atm getNotesFromAtm(Atm atm, BigDecimal money) {
        Map<Cash, Long> cashAtmTemp = atm.getCash();
        Long exchange = money.longValue();

        for (Map.Entry<Cash, Long> entry : cashAtmTemp.entrySet()) {
            int nominal = Integer.valueOf(entry.getKey().toString().substring(4));
            long numberOfNotes = entry.getValue();
            while (numberOfNotes > 0 && exchange >= nominal) {
                exchange -= nominal;
                numberOfNotes--;
            }
            entry.setValue(numberOfNotes);
        }
        atm.setCash(cashAtmTemp);
        return atm;
    }

    private boolean checkIfEnoughCashInAtm(BigDecimal money, BigDecimal cash) {
        return money.compareTo(cash) <= 0;
    }

    private boolean checkIfWithdrawIsCorrect(BigDecimal money, Atm atm) {
        return money.longValue() % getCashDivider(atm, money) <= 0;
    }

    private int getCashDivider(Atm atm, BigDecimal money) {
        int divider = 0;
        if (atm.getCash().get(Cash.NOTE100) > 0
                || (money.longValue() >= 700
                && atm.getCash().get(Cash.NOTE500) > 0
                && atm.getCash().get(Cash.NOTE200) > 0)) {
            divider = 100;
        } else if (atm.getCash().get(Cash.NOTE200) > 0
                || (money.longValue() % 1000 == 0
                && atm.getCash().get(Cash.NOTE500) % 2 == 0)) {
            divider = 200;
        } else if (atm.getCash().get(Cash.NOTE500) > 0) {
            divider = 500;
        }
        return divider;
    }

    private boolean checkIfEnoughMoneyOnAccount(Account account, BigDecimal money) {
        return money.compareTo(account.getMoneySum()) <= 0;
    }

    protected BigDecimal getSumOfCash(Map<Cash, Long> cash) {

        Long sum = 0L;
        for (Map.Entry<Cash, Long> entry : cash.entrySet()) {
            sum += (Integer.valueOf(entry.getKey().toString().substring(4)) * entry.getValue());
        }
        return BigDecimal.valueOf(sum);
    }
}
