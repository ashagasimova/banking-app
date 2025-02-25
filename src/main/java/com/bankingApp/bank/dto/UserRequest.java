package com.bankingApp.bank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter
@AllArgsConstructor
@Builder
public class UserRequest {

    private String firstName;
    private String lastName;
    private String otherName;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private String email;
    private String phoneNumber;
    private String alternativePhoneNumber;
}
