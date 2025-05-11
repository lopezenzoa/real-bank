package org.real.view;

import org.real.model.Permission;

public class CredentialView {
    // I'm not waiting for the user id because this method will be called by the own user
    public static void print(Integer id, String username, String password, Permission permission) {
        System.out.println(":: CREDENTIAL information ::");
        System.out.println("\n\t ID: " + id);
        System.out.println("\t Username: " + username);
        System.out.println("\t Password: " + password);
        System.out.println("\t Permission: " + permission);
    }

    // this method will be called just by both managers and admins
    public static void printAll(Integer id, Integer userId, String username, String password, Permission permission) {
        System.out.println(":: CREDENTIAL information ::");
        System.out.println("\n\t ID: " + id);
        System.out.println("\t User ID: " + userId);
        System.out.println("\t Username: " + username);
        System.out.println("\t Password: " + password);
        System.out.println("\t Permission: " + permission);
    }
}
