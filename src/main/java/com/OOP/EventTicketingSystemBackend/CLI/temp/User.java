package com.OOP.EventTicketingSystemBackend.CLI.temp;

public abstract class User {
    protected String userName;
    protected String password;

    public User(String userName, String password) {
        this.userName = userName;
        this.password = password;
    }

    public abstract void login();
    public abstract void logout();
    public abstract void cliMenu();
}
