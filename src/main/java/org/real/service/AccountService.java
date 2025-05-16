package org.real.service;

import org.real.exceptions.EntityNotFoundException;
import org.real.exceptions.ValidationException;
import org.real.model.Account;
import org.real.repository.AccountDAO;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

public class AccountService implements IService<Account> {
    private AccountDAO repository;

    public AccountService() {
        this.repository = new AccountDAO();
    }

    @Override
    public Account getById(int id) {
        Optional<Account> accountOptional = repository.findById(id);

        if (accountOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The account with the ID: " + id + " was not found at the database");

        return accountOptional.get();
    }

    @Override
    public List<Account> getAll() {
        return repository.findAll();
    }

    @Override
    public Account save(Account account) {
        // validating the user is matching with the db schema
        if (validateUserAccounts(account.getUserId()))
            throw new ValidationException("Sorry! something while adding the new account failed." +
                    "\nBe sure that:" +
                    "\n\t- The account balance is a positive integer" +
                    "\n\t- You haven't already a checking account");

        return repository.save(account).get();
    }

    @Override
    public Account update(Account newAccount) {
        // validating the account exists at the db
        Optional<Account> accountOptional = repository.findById(newAccount.getId());

        if (accountOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The account with the ID: " + newAccount.getId() + " was not found at the database");

        if (newAccount.getBalance() < 0.0)
            throw new ValidationException("Sorry! something while adding the new account failed." +
                    "\nBe sure that:" +
                    "\n\t- The account balance is a positive integer");

        // performs the updating at the db
        repository.update(newAccount);

        return newAccount;
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Account> accountOptional = repository.findById(id);

        // validating the user exists at the db
        if (accountOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The account with the ID: " + id + " was not found at the database");

        return repository.deleteById(id);
    }

    private boolean validateUserAccounts(Integer userId) {
        // gets all accounts from the user grouped by type
        Map<String, Long> userAccountsByType = getAll().stream()
                .filter(account -> account.getUserId().equals(userId)) // filtering all the account of the user
                .collect(Collectors.groupingBy(
                        account -> account.getAccountType().toString(), Collectors.counting()
                ));

        if (userAccountsByType.get("SAVINGS") == null)
                return false;
        else
            return true;
    }
}
