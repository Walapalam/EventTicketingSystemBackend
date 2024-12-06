package com.OOP.EventTicketingSystemBackend.CLI.models;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
public abstract class User {
    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
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


    @Transient
    private static long userIdCounter = 0;

    public User(String userName, String password, String role) {
        this.userId = generateUserId();
        this.userName = userName;
        this.password = password;
        this.role = role;
    }

    public User() {

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