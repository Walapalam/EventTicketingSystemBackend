package com.OOP.EventTicketingSystemBackend.Utility;

public interface Converter<E, D> {
    D convertToDTO(E entity);
    E convertToEntity(D dto);
}