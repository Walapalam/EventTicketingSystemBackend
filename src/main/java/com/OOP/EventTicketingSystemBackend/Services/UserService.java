package com.OOP.EventTicketingSystemBackend.Services;

import com.OOP.EventTicketingSystemBackend.CLI.models.User;
import com.OOP.EventTicketingSystemBackend.CLI.repositories.UserRepository;
import com.OOP.EventTicketingSystemBackend.DTO.UserDTO;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Vendor;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Customer;
import com.OOP.EventTicketingSystemBackend.CLI.tasks.Admin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class UserService {

    @Autowired
    private UserRepository userRepository;

    public UserDTO registerUser(UserDTO userDTO) {
        User user;
        if ("vendor".equalsIgnoreCase(userDTO.getRole())) {
            user = new Vendor();
        } else if ("customer".equalsIgnoreCase(userDTO.getRole())) {
            user = new Customer();
        } else if ("admin".equalsIgnoreCase(userDTO.getRole())) {
            user = new Admin();
        } else {
            throw new IllegalArgumentException("Invalid user role");
        }
        user.setUsername(userDTO.getUsername());
        user.setPassword(userDTO.getPassword());
        user.setRole(userDTO.getRole());
        userRepository.save(user);
        userDTO.setUserId(user.getUserId());
        return userDTO;
    }

    public UserDTO loginUser(UserDTO userDTO) {
        User user = userRepository.findByUsernameAndPassword(userDTO.getUsername(), userDTO.getPassword());
        if (user != null) {
            userDTO.setUserId(user.getUserId());
            userDTO.setRole(user.getRole());
            return userDTO;
        } else {
            throw new IllegalArgumentException("Invalid username or password");
        }
    }

    public Object getUserByRole(UserDTO userDTO) {
        User user = userRepository.findById((int) userDTO.getUserId())
                .orElseThrow(() -> new IllegalArgumentException("User not found"));

        if ("vendor".equalsIgnoreCase(user.getRole())) {
            Vendor vendor = new Vendor();
            vendor.setUserId(user.getUserId());
            vendor.setUsername(user.getUsername());
            vendor.setPassword(user.getPassword());
            vendor.setRole(user.getRole());
            return vendor;
        } else if ("customer".equalsIgnoreCase(user.getRole())) {
            Customer customer = new Customer();
            customer.setUserId(user.getUserId());
            customer.setUsername(user.getUsername());
            customer.setPassword(user.getPassword());
            customer.setRole(user.getRole());
            return customer;
        } else if ("admin".equalsIgnoreCase(user.getRole())) {
            Admin admin = new Admin();
            admin.setUserId(user.getUserId());
            admin.setUsername(user.getUsername());
            admin.setPassword(user.getPassword());
            admin.setRole(user.getRole());
            return admin;
        } else {
            throw new IllegalArgumentException("Invalid user role");
        }
    }
}