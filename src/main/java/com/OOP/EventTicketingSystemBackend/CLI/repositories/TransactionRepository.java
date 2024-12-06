package com.OOP.EventTicketingSystemBackend.CLI.repositories;

import com.OOP.EventTicketingSystemBackend.CLI.models.Transaction;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TransactionRepository extends JpaRepository<Transaction, Integer> {
}