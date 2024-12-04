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
    @Setter
    @Getter
    protected String role;

    private static long userIdCounter = 0;

    public User(String userName, String password, String role) {
        this.userId = generateUserId();
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    private synchronized long generateUserId() {
        return ++userIdCounter;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public abstract String getRole();

    public void login(){
        System.out.println("Logged in as " + this.getRole());
    }

    @Override
    public String toString() {
        return String.format("User ID: %d, Username: %s, Role: %s", this.userId, this.userName, this.role);
    }
}