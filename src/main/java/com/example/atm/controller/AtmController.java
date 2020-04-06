package com.example.atm.controller;

import com.example.atm.dto.AccountRequestDto;
import com.example.atm.dto.DepositMoneyRequestDto;
import com.example.atm.dto.PushCashToAtmRequestDto;
import com.example.atm.dto.TransferMoneyRequestDto;
import com.example.atm.dto.WithdrawMoneyRequestDto;
import com.example.atm.model.Person;
import com.example.atm.service.AccountService;
import com.example.atm.service.AtmService;
import com.example.atm.service.PersonService;
import java.security.Principal;
import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
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

    @PostMapping("/push-cash-to-atm")
    public String putCashToAtm(@RequestBody PushCashToAtmRequestDto pushCashToAtmRequestDto) {
        try {
            return atmService.putCashToAtm(pushCashToAtmRequestDto.getAtm(),
                    pushCashToAtmRequestDto.getCash()).toString();
        } catch (RuntimeException e) {
            logger.error(e.getMessage());
            return e.getMessage();
        }
    }

    @PostMapping("/withdraw-money")
    public String withdrawMoney(@RequestBody WithdrawMoneyRequestDto withdrawMoneyrequestDto, Principal principal) {
        Person person = personService.getByName(principal.getName());
        if (checkAccountsOwner(person, withdrawMoneyrequestDto.getAccount())) {
            try {
                return atmService.withdrawMoney(withdrawMoneyrequestDto.getAtm(),
                        withdrawMoneyrequestDto.getMoney(),
                        withdrawMoneyrequestDto.getAccount()).toString();
            } catch (RuntimeException e) {
                logger.error("Error in AtmController -> withdraw money" + e);
                return e.getMessage();
            }
        }
        return person.getName() + " doesn't have permission for this account!";
    }

    @PostMapping("/deposit-money")
    public String depositMoney(@RequestBody DepositMoneyRequestDto depositMoneyRequestDto) {
        try {
            return atmService.depositMoney(depositMoneyRequestDto.getAtm(),
                    depositMoneyRequestDto.getMoney(), depositMoneyRequestDto.getAccount()).toString();
        } catch (RuntimeException e) {
            logger.error("Error in AtmController -> deposit money" + e);
            return e.getMessage();
        }
    }

    @PostMapping("/transfer-money")
    public String transferMoney(@RequestBody TransferMoneyRequestDto transferMoneyRequestDto,
                                Principal principal) {
        Person person = personService.getByName(principal.getName());
        if (checkAccountsOwner(person, transferMoneyRequestDto.getOwnerAccount())) {
            try {
                return accountService.transferMoney(transferMoneyRequestDto.getMoney(),
                        transferMoneyRequestDto.getOwnerAccount(),
                        transferMoneyRequestDto.getDestinationAccount()).toString();
            } catch (RuntimeException e) {
                logger.error("Error in AtmController -> deposit money" + e);
                return e.getMessage();
            }
        }
        return person.getName() + " doesn't have permission for this account!";
    }

    private boolean checkAccountsOwner(Person person, AccountRequestDto accountRequestDto) {
        return person.getAccounts().contains(accountService.getAccount(accountRequestDto));
    }
}
