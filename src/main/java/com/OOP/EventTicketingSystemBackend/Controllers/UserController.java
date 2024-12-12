package com.OOP.EventTicketingSystemBackend.Controllers;

import com.OOP.EventTicketingSystemBackend.DTO.UserDTO;
import com.OOP.EventTicketingSystemBackend.Services.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/users")
public class UserController {

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public ResponseEntity<UserDTO> registerUser(@RequestBody UserDTO userDTO) {
        UserDTO registeredUser = userService.registerUser(userDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<UserDTO> loginUser(@RequestBody UserDTO userDTO) {
        UserDTO loggedInUser = userService.loginUser(userDTO);
        return ResponseEntity.ok(loggedInUser);
    }
}