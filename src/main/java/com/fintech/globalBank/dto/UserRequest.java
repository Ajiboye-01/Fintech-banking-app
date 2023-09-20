package com.fintech.globalBank.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class UserRequest {
    private String firstName;
    private String lastName;
    private String otherName;
    private String email;
    private String phoneNumber;
    private String altPhoneNumber;
    private String gender;
    private String address;
    private String stateOfOrigin;
    private String password;
    private String matchingPassword;
}
