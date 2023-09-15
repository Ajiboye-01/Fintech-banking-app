package com.fintech.globalBank.Service;

import java.math.BigDecimal;

import com.fintech.globalBank.dto.EmailDetails;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.fintech.globalBank.Entity.User;
import com.fintech.globalBank.Repository.UserRepo;
import com.fintech.globalBank.Util.AccountUtil;
import com.fintech.globalBank.dto.AccountInfo;
import com.fintech.globalBank.dto.BankResponse;
import com.fintech.globalBank.dto.UserRequest;

@Service
public class UserServiceImpl implements UserService {

    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

    /**
     * it save a user into the Database, since an account is assign to a new user
     * so it instanciate the creates user
     * Check is user already have an account
     */
    @Override
    public BankResponse createAccount(@NotNull UserRequest userRequest) {

        /**
         * This checks if the user already has an account number
         * then it respond with account exist code and message with the NULL account info since it was not created
         */

        if(userRepo.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(AccountUtil.ACCOUNT_EXISTS_CODE)
                    .responseMessage(AccountUtil.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        /**
         * This creates an instance of a new user to be saved
         * inside the Database
         */

        User newUser = User.builder()
                .firstName(userRequest.getFirstName())
                .lastName(userRequest.getLastName())
                .otherName(userRequest.getOtherName())
                .gender(userRequest.getGender())
                .stateOfOrigin(userRequest.getStateOfOrigin())
                .accountNumber(AccountUtil.generateAccountNumber())
                .email(userRequest.getEmail())
                .accountBalance(BigDecimal.ZERO)
                .address(userRequest.getAddress())
                .altPhoneNumber(userRequest.getAltPhoneNumber())
                .phoneNumber(userRequest.getPhoneNumber())
                .Status("ACTIVE")
        .build();

        /**
         * After all the properties have been build using the builder method
         * It is then saved into the database using the repository
         */
        User savedUser = userRepo.save(newUser);

        /**
         *Send the email alert to user after saving into the database;
         * This is achieved by building the email details instance, adding the recipient, subject and body
         * And passing it into the Send-email method that is in the email service that was autowired
         */

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations!!!!, Account created Successfully.\nYour Account Details: \n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() +
                        "\nAccount Number: " + savedUser.getAccountNumber())
                        .build();

        emailService.sendEmail(emailDetails);

        /**
         * The response of the method is the success code and message alongside the account information
         */
        return BankResponse.builder()
                .responseCode(AccountUtil.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(AccountUtil.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();
    }

}
