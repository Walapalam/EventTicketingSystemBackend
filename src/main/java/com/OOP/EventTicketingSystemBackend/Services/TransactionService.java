package com.OOP.EventTicketingSystemBackend.Services;

import com.OOP.EventTicketingSystemBackend.DTO.TransactionDTO;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

@Service
public class TransactionService {

    private List<TransactionDTO> transactionStore = new ArrayList<>();

    public List<TransactionDTO> getAllTransactions() {
        return new ArrayList<>(transactionStore);
    }

    public TransactionDTO getTransactionById(long id) {
        return transactionStore.stream()
                .filter(transaction -> transaction.getTransactionId() == id)
                .findFirst()
                .orElse(null);
    }

    public void logTransaction(TransactionDTO transactionDTO) {
        transactionStore.add(transactionDTO);
    }
}