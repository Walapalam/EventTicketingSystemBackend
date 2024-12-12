package com.OOP.EventTicketingSystemBackend.CLI.tasks;

import com.OOP.EventTicketingSystemBackend.CLI.models.User;
import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("admin")
public class Admin extends User {
    public Admin(String username, String password, String role) {
        super(username, password, role);
    }

    public Admin() {

    }

    @Override
    public String getRole() {
        return "admin";
    }
}
