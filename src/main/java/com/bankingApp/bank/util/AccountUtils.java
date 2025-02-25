package com.bankingApp.bank.util;

import java.time.Year;

public class AccountUtils {

    public static final String USER_EXIST_CODE = "001";
    public static final String USER_EXIST_MESSAGE = "This user already has an account created!";
    public static final String ACCOUNT_CREATION_SUCCESS = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account has been successfully created!";
    public static final String USER_NOT_EXIST_CODE = "003";
    public static final String USER_NOT_EXIST_MESSAGE = "User with the provided Account Number does not exist!";
    public static final String ACCOUNT_FOUND_CODE = "004";
    public static final String ACCOUNT_FOUND_MESSAGE = "Account found!";
    public static final String ACCOUNT_CREDITED_SUCCESS = "005";
    public static final String ACCOUNT_CREDITED_MESSAGE = "User account successfully credited!";
    public static final String INSUFFICIENT_BALANCE_CODE = "006";
    public static final String INSUFFICIENT_BALANCE_MESSAGE = "Insufficient Balance!";
    public static final String ACCOUNT_DEBITED_CODE = "007";
    public static final String ACCOUNT_DEBITED_MESSAGE = "Account has been successfully debited!";
    public static final String TRANSFER_SUCCESS_CODE = "008";
    public static final String TRANSFER_SUCCESS_MESSAGE = "Transfer Success!";



    Year currentYear = Year.now();

    int min = 100000;
    int max = 999999;

    int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

    String year = String.valueOf(currentYear);
    String randomNumber = String.valueOf(randNumber);
    StringBuilder accountNumber = new StringBuilder();

    public static  String generateAccountNumber() {
        Year currentYear = Year.now();

        int min = 100000;
        int max = 999999;

        int randNumber = (int) Math.floor(Math.random() * (max - min + 1) + min);

        String year = String.valueOf(currentYear);
        String randomNumber = String.valueOf(randNumber);
        StringBuilder accountNumber = new StringBuilder();
        return  accountNumber.append(year).append(randomNumber).toString();
    }
}
