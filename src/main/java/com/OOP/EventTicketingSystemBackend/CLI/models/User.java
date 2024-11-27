package com.OOP.EventTicketingSystemBackend.CLI.models;

import lombok.Getter;
import lombok.Setter;

public abstract class User {
    @Getter
    @Setter
    private long userId;
    @Setter
    @Getter
    private String userName;
    @Setter
    @Getter
    private String password;

    public User(long userId, String userName, String password) {
        this.userId = userId;
        this.userName = userName;
        this.password = password;
    }

    public abstract String getRole();

    public void login(){
        System.out.println("Logged in as " + this.getRole());
    }
}
