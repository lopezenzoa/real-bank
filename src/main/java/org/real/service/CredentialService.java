package org.real.service;

import org.real.exceptions.EntityNotFoundException;
import org.real.exceptions.ValidationException;
import org.real.model.Credential;
import org.real.model.User;
import org.real.repository.CredentialDAO;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

public class CredentialService implements IService<Credential> {
    private CredentialDAO repository;

    public CredentialService() {
        this.repository = new CredentialDAO();
    }

    @Override
    public Credential getById(int id) {
        Optional<Credential> credentialOptional = repository.findById(id);

        if (credentialOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The credential with the ID: " + id + " was not found at the database");

        return credentialOptional.get();
    }

    @Override
    public List<Credential> getAll() {
        return repository.findAll();
    }

    @Override
    public Credential save(Credential credential) {
        // validating the user is matching with the db schema
        if (!validateUserId(credential.getUserId()) || !validateUsername(credential.getUsername()) || credential.getPassword().isBlank() || credential.getUsername().isBlank())
            throw new ValidationException("Sorry! something while adding the new credential failed." +
                    "\nBe sure that:" +
                    "\n\t- Both username and password are not in blank" +
                    "\n\t- Both userId and username are yours");

        return repository.save(credential).get();
    }

    @Override
    public Credential update(Credential newCredential) {
        // validating the credential exists at the db
        Optional<Credential> credentialOptional = repository.findById(newCredential.getId());

        if (credentialOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The credential with the ID: " + newCredential.getId() + " was not found at the database");

        if (!validateUserId(newCredential.getUserId()) || !validateUsername(newCredential.getUsername()) || newCredential.getPassword().isBlank() || newCredential.getUsername().isBlank())
            throw new ValidationException("Sorry! something while adding the new credential failed." +
                    "\nBe sure that:" +
                    "\n\t- Both username and password are not in blank" +
                    "\n\t- Both userId and username are yours");

        // performs the updating at the db
        repository.update(newCredential);

        return newCredential;
    }

    @Override
    public boolean deleteById(int id) {
        Optional<Credential> credentialOptional = repository.findById(id);

        // validating the user exists at the db
        if (credentialOptional.isEmpty())
            throw new EntityNotFoundException("Sorry! The credential with the ID: " + id + " was not found at the database");

        return repository.deleteById(id);
    }

    // validates that there aren't any user with the new credentials
    private boolean validateUserId(Integer userId) {
        Set<Integer> userIds = getAll().stream()
                .map(Credential::getUserId)
                .collect(Collectors.toSet());

        return userIds.contains(userId); // returns a boolean whether the para is in the set or not
    }

    // validates that the username is unique
    private boolean validateUsername(String username) {
        Set<String> usernames = getAll().stream()
                .map(Credential::getUsername)
                .collect(Collectors.toSet());

        return usernames.contains(username);
    }
}
