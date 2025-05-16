package org.real.controller;

import org.real.model.Account;
import org.real.model.Credential;
import org.real.model.Permission;
import org.real.service.CredentialService;
import org.real.view.CredentialView;

import java.util.List;

public class CredentialController {
    private CredentialService service;

    public CredentialController() {
        this.service = new CredentialService();
    }

    public Integer create(Integer userId, String username, String password) {
        Credential credential = new Credential();

        credential.setUserId(userId);
        credential.setUsername(username);
        credential.setPassword(password);
        credential.setPermission(Permission.CLIENT); // by default, all users when registered are clients

        credential = service.save(credential);

        return credential.getId();
    }

    public List<Credential> readAll() {
        return service.getAll();
    }

    public Credential read(Integer credentialId) {
        return service.getById(credentialId);
    }

    public Integer update(Integer credentialId, String newUsername, String newPassword, Permission newPermission) {
        Credential toModify = read(credentialId);

        toModify.setUsername(newUsername);
        toModify.setPassword(newPassword);
        toModify.setPermission(newPermission);

        return service.update(toModify).getId();
    }

    public void deleteById(Integer crendentialId) {
        service.deleteById(crendentialId);
    }
}
