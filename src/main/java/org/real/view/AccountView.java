package org.real.view;

import org.real.model.AccountType;

import java.time.LocalDate;

public class AccountView {
    public static void print(Integer id, String ownerFirstName, String ownerLastName, AccountType accountType, Double balance, LocalDate creationDate) {
        System.out.println(":: ACCOUNT information ::");
        System.out.println("\n\t ID: " + id);
        System.out.println("\t Owner: " + ownerFirstName + " " + ownerLastName); // be sure to join with 'users' table to show the owner name and not just an id
        System.out.println("\t Account type: " + accountType);
        System.out.println("\t Balance: $" + balance);
        System.out.println("\t Created on: " + creationDate);
    }
}
