package org.real.controller;

import org.real.model.Account;
import org.real.model.AccountType;
import org.real.service.AccountService;

import java.util.List;

public class AccountController {
    private AccountService service;

    public AccountController() {
        this.service = new AccountService();
    }

    public Integer create(Integer userId, AccountType accountType) {
        Account account = new Account();

        account.setUserId(userId);
        account.setAccountType(accountType);

        return service.save(account).getId();
    }

    public List<Account> readAll() {
        return service.getAll();
    }

    public Account read(Integer accountId) {
        return service.getById(accountId);
    }

    public Integer update(Integer accountId, Double newBalance) {
        Account toModify = read(accountId);

        toModify.setBalance(toModify.getBalance() + newBalance);

        return service.update(toModify).getId();
    }

    public void deleteById(Integer accountId) {
        service.deleteById(accountId);
    }
}
