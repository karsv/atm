package com.example.atm.service;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AtmRequestDto;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import java.math.BigDecimal;
import java.util.Map;

public interface AtmService {
    Atm addAtm(Atm atm);

    Atm getAtmById(Long id);

    Atm putCashToAtm(AtmRequestDto atm, Map<Cash, Long> cash);

    Map<Cash, Long> getCashFromAtm(AtmRequestDto atm);

    Atm withdrawMoney(AtmRequestDto atm, BigDecimal money, AccountRequestDto account);

    Atm depositMoney(AtmRequestDto atm, Map<Cash, Long> money, AccountRequestDto account);
}
