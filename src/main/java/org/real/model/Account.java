package org.real.model;

import java.time.LocalDate;

public class Account {
    private Integer id;
    private Integer userId;
    private AccountType accountType;
    private Double balance;
    private LocalDate creationDate;

    public Account() {
    }

    public Account(Integer id, Integer userId, AccountType accountType, Double balance, LocalDate creationDate) {
        this.id = id;
        this.userId = userId;
        this.accountType = accountType;
        this.balance = balance;
        this.creationDate = creationDate;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public AccountType getAccountType() {
        return accountType;
    }

    public void setAccountType(AccountType accountType) {
        this.accountType = accountType;
    }

    public Double getBalance() {
        return balance;
    }

    public void setBalance(Double balance) {
        this.balance = balance;
    }

    public LocalDate getCreationDate() {
        return creationDate;
    }

    public void setCreationDate(LocalDate creationDate) {
        this.creationDate = creationDate;
    }
}
