package com.maximbuza.appaston.dto;

public class User {
    private String username;
    private String password;
    private String NewPassword;

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

    public String getNewPassword() {
        return NewPassword;
    }

    public void setNewPassword(String newPassword) {
        NewPassword = newPassword;
    }

    @Override
    public String toString() {
        return "username='" + username + '\'' +
                ", password='" + password;
    }
}
