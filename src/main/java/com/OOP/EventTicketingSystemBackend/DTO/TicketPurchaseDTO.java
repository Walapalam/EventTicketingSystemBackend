package com.OOP.EventTicketingSystemBackend.DTO;

public class TicketPurchaseDTO {
    private UserDTO userDTO;
    private Long eventID;

    // Getters and setters
    public UserDTO getUserDTO() {
        return userDTO;
    }

    public void setUserDTO(UserDTO userDTO) {
        this.userDTO = userDTO;
    }

    public Long getEventID() {
        return eventID;
    }

    public void setEventID(Long eventID) {
        this.eventID = eventID;
    }
}