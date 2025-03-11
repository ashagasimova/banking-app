package com.bankingApp.bank.service.impl;

import com.bankingApp.bank.dto.*;
import com.bankingApp.bank.entity.User;
import com.bankingApp.bank.repository.UserRepository;
import com.bankingApp.bank.util.AccountUtils;
import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.Arrays;

@Service
@NoArgsConstructor
@AllArgsConstructor
public class UserServiceImpl implements UserService {

   @Autowired
   private UserRepository userRepository;
    @Autowired
    private  EmailService emailService;
    @Autowired
    private  TransactionService transactionService;
    @Autowired
    private  PasswordEncoder passwordEncoder;

    @Override
    public BankResponse createAccount(UserRequest userRequest) {

        if (userRepository.existsByEmail(userRequest.getEmail())) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.USER_EXIST_MESSAGE)
                    .responseCode(AccountUtils.USER_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }
        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .address(userRequest.getAddress())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtils.generateAccountNumber())
                .email(userRequest.getEmail())
                .password(passwordEncoder.encode(userRequest.getPassword()))
                .phoneNumber(userRequest.getPhoneNumber())
                .alternativePhoneNumber(userRequest.getAlternativePhoneNumber())
                .accountBalance(BigDecimal.ZERO)
                .status("ACTIVE")
                .build();


        User savedUser = userRepository.save(newUser);

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations! Your Account Has Been Successfully Created!\n " +
                        "Your Account Details \n Account Name: " + savedUser.getFirstName() + " "
                        + savedUser.getLastName() + " " + savedUser.getOtherName() +
                        "\n Account Number : " + savedUser.getAccountNumber())

                .build();
        emailService.sendEmailAlert(emailDetails);


        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREATION_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " "
                                + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())


                .build();
    }

    @Override
    public BankResponse balanceEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isExist) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.USER_NOT_EXIST_MESSAGE)
                    .responseCode(AccountUtils.USER_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_FOUND_CODE)
                .responseMessage(AccountUtils.ACCOUNT_FOUND_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(foundUser.getAccountBalance())
                        .accountName(foundUser.getFirstName() + " "
                                + foundUser.getLastName() + " " + foundUser.getOtherName())
                        .accountNumber(enquiryRequest.getAccountNumber())
                        .build())

                .build();

    }

    @Override
    public String nameEnquiry(EnquiryRequest enquiryRequest) {
        Boolean isExist = userRepository.existsByAccountNumber(enquiryRequest.getAccountNumber());
        if (!isExist) {
            return AccountUtils.USER_NOT_EXIST_MESSAGE;
        }

        User foundUser = userRepository.findByAccountNumber(enquiryRequest.getAccountNumber());
        return foundUser.getFirstName() + " "
                + foundUser.getLastName() + " " + foundUser.getOtherName();


    }

    @Override
    public BankResponse creditAccount(CreditDebitRequest creditDebitRequest) {
        Boolean isExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isExist) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.USER_NOT_EXIST_MESSAGE)
                    .responseCode(AccountUtils.USER_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }

        User userToCredit = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());
        userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditDebitRequest.getAmount()));
        userRepository.save(userToCredit);

        //Save transaction
        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(userToCredit.getAccountNumber())
                .transactionType("Credit")
                .amount(creditDebitRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        return BankResponse.builder()
                .responseCode(AccountUtils.ACCOUNT_CREDITED_SUCCESS)
                .responseMessage(AccountUtils.ACCOUNT_CREDITED_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .accountName(userToCredit.getFirstName() + " "
                                + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                        .accountBalance(userToCredit.getAccountBalance())
                        .build())
                .build();

    }

    @Override
    public BankResponse debitAccount(CreditDebitRequest creditDebitRequest) {

        Boolean isExist = userRepository.existsByAccountNumber(creditDebitRequest.getAccountNumber());
        if (!isExist) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.USER_NOT_EXIST_MESSAGE)
                    .responseCode(AccountUtils.USER_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();
        }

        User debtToUser = userRepository.findByAccountNumber(creditDebitRequest.getAccountNumber());


        if (debtToUser.getAccountBalance().compareTo(creditDebitRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();

        }
        debtToUser.setAccountBalance(debtToUser.getAccountBalance().subtract(creditDebitRequest.getAmount()));
        userRepository.save(debtToUser);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(debtToUser.getAccountNumber())
                .transactionType("Debit")
                .amount(creditDebitRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

        BankResponse response = BankResponse.builder()
                .responseMessage(AccountUtils.ACCOUNT_DEBITED_MESSAGE)
                .responseCode(AccountUtils.ACCOUNT_DEBITED_CODE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(debtToUser.getAccountBalance())
                        .accountNumber(creditDebitRequest.getAccountNumber())
                        .accountName(debtToUser.getFirstName() + " "
                                + debtToUser.getLastName() + " " + debtToUser.getOtherName())
                        .accountBalance(debtToUser.getAccountBalance())
                        .build())
                .build();

        return response;


    }

    @Override
    public BankResponse transfer(TransferRequest transferRequest) {

        Boolean isDestinationExist = userRepository.existsByAccountNumber(transferRequest.getDestinationAccountNumber());
        if (!isDestinationExist) {
            return BankResponse.builder()
                    .responseMessage(AccountUtils.USER_NOT_EXIST_MESSAGE)
                    .responseCode(AccountUtils.USER_NOT_EXIST_CODE)
                    .accountInfo(null)
                    .build();

        }


        User sourceAccount = userRepository.findByAccountNumber(transferRequest.getSourceAccountNumber());

        if (sourceAccount.getAccountBalance().compareTo(transferRequest.getAmount()) < 0) {
            return BankResponse.builder()
                    .responseCode(AccountUtils.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(AccountUtils.INSUFFICIENT_BALANCE_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        sourceAccount.setAccountBalance(sourceAccount.getAccountBalance().subtract(transferRequest.getAmount()));
        userRepository.save(sourceAccount);

        String sourceUserName = sourceAccount.getFirstName() + " " + sourceAccount.getLastName() + " " + sourceAccount.getOtherName();
//        EmailDetails debitAlert = EmailDetails.builder()
//                .subject("DEBIT ALERT")
//                .recipient(sourceAccount.getEmail())
//                .messageBody("The sum of " + transferRequest.getAmount() + " has been deducted from your account!" +
//                        "Your current balance is: " + sourceAccount.getAccountBalance())
//                .build();
//        emailService.sendEmailAlert(debitAlert);


        User destinationAccount = userRepository.findByAccountNumber(transferRequest.getDestinationAccountNumber());

        destinationAccount.setAccountBalance(destinationAccount.getAccountBalance().add(transferRequest.getAmount()));
        userRepository.save(destinationAccount);

        TransactionDto transactionDto = TransactionDto.builder()
                .accountNumber(destinationAccount.getAccountNumber())
                .transactionType("Debit")
                .amount(transferRequest.getAmount())
                .build();

        transactionService.saveTransaction(transactionDto);

//        EmailDetails creditAlert = EmailDetails.builder()
//                .subject("CREDIT ALERT")
//                .recipient(destinationAccount.getEmail())
//                .messageBody("The sum of " + transferRequest.getAmount() + " has been sent to your account" +
//                        "from " + sourceUserName +
//                        "Your current balance is: " + destinationAccount.getAccountBalance())
//                .build();
//        emailService.sendEmailAlert(creditAlert);

        return BankResponse.builder()
                .responseMessage(AccountUtils.TRANSFER_SUCCESS_CODE)
                .responseCode(AccountUtils.TRANSFER_SUCCESS_MESSAGE)
                .accountInfo(null)
                .build();


    }

}
