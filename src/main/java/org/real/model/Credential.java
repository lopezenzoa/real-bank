package org.real.model;

public class Credential {
    private Integer id;
    private Integer userId;
    private String username;
    private String password;
    private Permission permission;

    public Credential() {}

    public Credential(Integer id, Integer userId, String username, String password, Permission permission) {
        this.id = id;
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.permission = permission;
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

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Permission getPermission() {
        return permission;
    }

    public void setPermission(Permission permission) {
        this.permission = permission;
    }
}
