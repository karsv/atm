package com.example.atm.service;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AtmRequestDto;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

public interface AtmService {
    Atm add(Atm atm);

    Atm get(Long id);

    List<Atm> getAll();

    Atm putCash(AtmRequestDto atm, Map<Cash, Long> cash);

    Map<Cash, Long> cashWithdrwal(AtmRequestDto atm);

    Atm withdrawMoney(AtmRequestDto atm, BigDecimal money, AccountRequestDto account);

    Atm depositMoney(AtmRequestDto atm, Map<Cash, Long> money, AccountRequestDto account);
}
