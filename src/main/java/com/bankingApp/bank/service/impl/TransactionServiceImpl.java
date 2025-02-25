package com.bankingApp.bank.service.impl;

import com.bankingApp.bank.dto.TransactionDto;
import com.bankingApp.bank.entity.Transaction;
import com.bankingApp.bank.repository.TransactionRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class TransactionServiceImpl implements TransactionService{

    private final TransactionRepository transactionRepository;
    @Override
    public void saveTransaction(TransactionDto transactionDto) {
        Transaction saveTransaction = Transaction.builder()
                .amount(transactionDto.getAmount())
                .transactionType(transactionDto.getTransactionType())
                .accountNumber(transactionDto.getAccountNumber())
                .status("SUCCESS")
                .build();

        transactionRepository.save(saveTransaction);
        System.out.println("Transaction saved successfully!");
    }

}
