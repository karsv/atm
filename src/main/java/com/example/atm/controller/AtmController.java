package com.example.atm.controller;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.AtmRequestDto;
import com.example.atm.dto.AtmResponseDto;
import com.example.atm.dto.DepositMoneyRequestDto;
import com.example.atm.dto.PushCashToAtmRequestDto;
import com.example.atm.dto.TransferMoneyRequestDto;
import com.example.atm.dto.WithdrawMoneyRequestDto;
import com.example.atm.exception.AccountException;
import com.example.atm.exception.AtmException;
import com.example.atm.model.Account;
import com.example.atm.model.Atm;
import com.example.atm.model.Cash;
import com.example.atm.model.Person;
import com.example.atm.service.AccountService;
import com.example.atm.service.AtmService;
import com.example.atm.service.PersonService;
import java.security.Principal;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@CrossOrigin(origins = "http://localhost:4200")
@RequestMapping("/atm")
public class AtmController {
    private static final Logger logger = LogManager.getLogger(AtmController.class);
    private final AtmService atmService;
    private final AccountService accountService;
    private final PersonService personService;

    public AtmController(AtmService atmService, AccountService accountService,
                         PersonService personService) {
        this.atmService = atmService;
        this.accountService = accountService;
        this.personService = personService;
    }

    @GetMapping
    public List<AtmResponseDto> getAtms() {
        return atmService.getAll().stream()
                .map(x -> convertAtmToResponseDto(x))
                .collect(Collectors.toList());
    }

    @PostMapping("/push-cash")
    public Atm putCashToAtm(@RequestBody PushCashToAtmRequestDto pushCashToAtmRequestDto) {
        Map<Cash, Long> cash = new LinkedHashMap<>();
        cash.put(Cash.NOTE100, pushCashToAtmRequestDto.getNote100());
        cash.put(Cash.NOTE200, pushCashToAtmRequestDto.getNote200());
        cash.put(Cash.NOTE500, pushCashToAtmRequestDto.getNote500());

        for (Map.Entry<Cash, Long> e : cash.entrySet()) {
            if (e.getValue() < 0) {
                throw new AtmException("The number of " + e.getKey()
                        + " must be bigger than 0!");
            }
        }

        var atmRequestDto = new AtmRequestDto();
        atmRequestDto.setId(pushCashToAtmRequestDto.getId());
        return atmService.putCash(atmRequestDto,
                cash);
    }

    @PostMapping("/withdraw-money")
    public Atm withdrawMoney(@RequestBody WithdrawMoneyRequestDto withdrawMoneyrequestDto,
                             Principal principal) {
        Person person = personService.getByName(principal.getName());
        var account = new AccountRequestDto();
        account.setId(withdrawMoneyrequestDto.getAccountId());
        var atm = new AtmRequestDto();
        atm.setId(withdrawMoneyrequestDto.getAtmId());
        return atmService.withdrawMoney(atm,
                withdrawMoneyrequestDto.getMoney(),
                account);
    }

    @PostMapping("/deposit-money")
    public Atm depositMoney(@RequestBody DepositMoneyRequestDto depositMoneyRequestDto) {
        var atmRequestDto = new AtmRequestDto();
        atmRequestDto.setId(depositMoneyRequestDto.getAtmId());

        Map<Cash, Long> map = new LinkedHashMap<>();
        map.put(Cash.NOTE100, depositMoneyRequestDto.getNote100());
        map.put(Cash.NOTE200, depositMoneyRequestDto.getNote200());
        map.put(Cash.NOTE500, depositMoneyRequestDto.getNote500());

        var accountRequestDto = new AccountRequestDto();
        accountRequestDto.setId(depositMoneyRequestDto.getAccountId());

        return atmService.depositMoney(atmRequestDto, map, accountRequestDto);
    }

    @PostMapping("/transfer-money")
    public Account transferMoney(@RequestBody TransferMoneyRequestDto transferMoneyRequestDto,
                                 Principal principal) {
        Person person = personService.getByName(principal.getName());
        AccountRequestDto ownerAccountRequestDto = new AccountRequestDto();
        ownerAccountRequestDto.setId(transferMoneyRequestDto.getOwnerAccountId());
        AccountRequestDto destinationAccountRequestDto = new AccountRequestDto();
        destinationAccountRequestDto.setId(transferMoneyRequestDto.getDestinationAccountId());

        if (!checkAccountsOwner(person, ownerAccountRequestDto)) {
            throw new AccountException("No such account!");
        }
        return accountService.transferMoney(transferMoneyRequestDto.getMoney(),
                ownerAccountRequestDto,
                destinationAccountRequestDto);
    }

    private boolean checkAccountsOwner(Person person, AccountRequestDto accountRequestDto) {
        return person.getAccounts().contains(accountService.getAccount(accountRequestDto));
    }

    private AtmResponseDto convertAtmToResponseDto(Atm atm) {
        var atmResponseDto = new AtmResponseDto();
        atmResponseDto.setId(atm.getId());
        atmResponseDto.setNote100(atm.getCash().get(Cash.NOTE100).toString());
        atmResponseDto.setNote200(atm.getCash().get(Cash.NOTE200).toString());
        atmResponseDto.setNote500(atm.getCash().get(Cash.NOTE500).toString());
        return atmResponseDto;
    }
}
