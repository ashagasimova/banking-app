package com.bankingApp.bank.controller;

import com.bankingApp.bank.entity.Transaction;
import com.bankingApp.bank.service.impl.BankStatement;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("bankStatement")
public class BankStatementController {

    private final BankStatement bankStatement;

    @GetMapping
    public List<Transaction> generateBankStatement(@RequestParam String accountNumber,
                                                   @RequestParam String startDate,
                                                   @RequestParam String endDate){
        return bankStatement.generateStatement(accountNumber, startDate, endDate);
    }
}


