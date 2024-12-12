package com.OOP.EventTicketingSystemBackend.Utility;

import com.OOP.EventTicketingSystemBackend.DTO.UserDTO;
import com.OOP.EventTicketingSystemBackend.CLI.models.User;
import org.springframework.stereotype.Component;

@Component
public class UserConverter implements Converter<User, UserDTO> {

    @Override
    public UserDTO convertToDTO(User user) {
        return new UserDTO(user);
    }

    @Override
    public User convertToEntity(UserDTO userDTO) {
        // Implement the conversion logic if needed
        return null;
    }
}