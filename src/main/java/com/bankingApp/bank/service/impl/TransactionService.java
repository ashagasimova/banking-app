package com.bankingApp.bank.service.impl;

import com.bankingApp.bank.dto.TransactionDto;
import com.bankingApp.bank.entity.Transaction;

public interface TransactionService {
    void saveTransaction(TransactionDto transactionDto);
}
