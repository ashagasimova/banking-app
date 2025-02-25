package com.bankingApp.bank.service.impl;

import com.bankingApp.bank.dto.EmailDetails;

public interface EmailService {
    void sendEmailAlert(EmailDetails emailDetails);
}
