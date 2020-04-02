package com.example.atm.service;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AtmRequestDto;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import java.math.BigDecimal;
import java.util.Map;

public interface AtmService {
    Atm addAtm(Atm atm);

    Atm updateAtm(AtmRequestDto atm, Map<Cash, Long> cash);

    Map<Cash, Long> checkCashInAtm(AtmRequestDto atm);

    Atm withdrawMoney(AtmRequestDto atm, BigDecimal money, AccountRequestDto account);

    Atm depositMoney(AtmRequestDto atm, Map<Cash, Long> money, AccountRequestDto account);

    void transferMoney(BigDecimal money,
                       AccountRequestDto ownAccount, AccountRequestDto destinationAccount);
}
