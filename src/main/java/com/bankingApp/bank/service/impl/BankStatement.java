package com.bankingApp.bank.service.impl;

import com.bankingApp.bank.entity.Transaction;
import com.bankingApp.bank.repository.TransactionRepository;
import lombok.AllArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

@Component
@RequiredArgsConstructor
public class BankStatement {

    private final TransactionRepository transactionRepository;

    public List<Transaction> generateStatement(String accountNumber, String startDate, String endDate){
        LocalDate start = LocalDate.parse(startDate, DateTimeFormatter.ISO_DATE);
        LocalDate end = LocalDate.parse(endDate, DateTimeFormatter.ISO_DATE);
        List<Transaction> transactionList = transactionRepository.findAll()
                .stream().filter(transaction -> transaction.getAccountNumber()
                        .equals(accountNumber)).filter(transaction -> transaction.getCreatedAt()
                        .isEqual(start.atStartOfDay())).filter(transaction ->
                        transaction.getModifiedAt().isEqual(end.atStartOfDay())).toList();

        return transactionList;
    }

}
