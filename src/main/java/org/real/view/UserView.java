package org.real.view;

import java.time.LocalDate;

public class UserView {
    public static void print(Integer id, String firstName, String lastName, String dni, String email, LocalDate creationDate) {
        System.out.println(":: USER information ::");
        System.out.println("\n\t ID: " + id);
        System.out.println("\t Name: " + firstName + " " + lastName);
        System.out.println("\t 'DNI': " + dni);
        System.out.println("\t Email: " + email);
        System.out.println("\t Created on: " + creationDate);
    }
}