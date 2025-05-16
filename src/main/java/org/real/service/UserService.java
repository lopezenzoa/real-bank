package org.real.service;

import org.real.exceptions.EntityNotFoundException;
import org.real.exceptions.ValidationException;
import org.real.model.User;
import org.real.repository.UserDAO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

// the aim of each service is to add a security layer when reaching rows from the db tables
public class UserService implements IService<User> {
    private UserDAO repository; // I preferred to name it 'repository' since is more comprehensible

    public UserService() {
        this.repository = new UserDAO();
    }

    @Override
    public User getById(int id) {
        Optional<User> userOptional = repository.findById(id);

        if (userOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The user with the ID: " + id + " was not found at the database");

        return userOptional.get();
    }

    @Override
    public List<User> getAll() {
        return repository.findAll();
    }

    @Override
    public User save(User user) {
        System.out.println(validateDNI(user.getDni()));
        System.out.println(validateEmail(user.getEmail()));

        // validating the user is matching with the db schema
        if (user.getFirstName().isBlank() || user.getLastName().isBlank() || validateDNI(user.getDni()) || validateEmail(user.getEmail()))
            throw new ValidationException("Sorry! something while adding the new user failed." +
                    "\nBe sure that:" +
                    "\n\t- Both first name and last name are not in blank" +
                    "\n\t- Both DNI and email are yours");

        return repository.save(user).get();
    }

    @Override
    public User update(User newUser) {
        // validating the user exists at the db
        Optional<User> userOptional = repository.findById(newUser.getId());

        if (userOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The user with the ID: " + newUser.getId() + " was not found at the database");

        // validating the user is matching with the db schema
        if (newUser.getFirstName().isBlank() || newUser.getLastName().isBlank() || validateDNI(newUser.getDni()) || validateEmail(newUser.getEmail()))
            throw new ValidationException("Sorry! something while updating the user failed." +
                    "\nBe sure that:" +
                    "\n\t- Both first name and last name are not in blank" +
                    "\n\t- Both DNI and email are yours");

        // performs the updating at the db
        repository.update(newUser);

        return newUser;
    }

    @Override
    public boolean deleteById(int id) {
        Optional<User> userOptional = repository.findById(id);

        // validating the user exists at the db
        if (userOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The user with the ID: " + id + " was not found at the database");

        return repository.deleteById(id);
    }

    // maybe this method can be extracted to another class
    private boolean validateDNI(String dni) {
        // gets all the users from the db and applies stream to map into a set of theirs DNIs
        Set<String> DNIs = repository.findAll().stream()
                .map(User::getDni)
                .collect(Collectors.toSet());

        return DNIs.contains(dni); // returns a boolean whether the para is in the set or not
    }

    // maybe this method can be extracted to another class
    private boolean validateEmail(String email) {
        // gets all the users from the db and applies stream to map into a set of theirs emails
        Set<String> emails = repository.findAll().stream()
                .map(User::getEmail)
                .collect(Collectors.toSet());

        return emails.contains(email);
    }
}
