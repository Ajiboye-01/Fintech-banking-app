package com.fintech.globalBank.Service;

import java.math.BigDecimal;

//import com.fintech.globalBank.Entity.VerificationToken;
//import com.fintech.globalBank.Repository.VerificationRepository;
import com.fintech.globalBank.Util.AccountUtil;
import com.fintech.globalBank.Util.Response;
//import com.fintech.globalBank.Util.WebSecurityConfig;
import com.fintech.globalBank.dto.*;
import lombok.extern.slf4j.Slf4j;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;

import org.springframework.stereotype.Service;

import com.fintech.globalBank.Entity.User;
import com.fintech.globalBank.Repository.UserRepo;

@Service
@Slf4j
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepo userRepo;

    @Autowired
    private EmailService emailService;

//    @Autowired
//    private VerificationRepository verificationRepository;
//
//    @Autowired
//    private WebSecurityConfig config;

    /**
     * it save a user into the Database, since an account is assign to a new user
     * so it instanciate the creates user
     * Check is user already have an account
     */
    public BankResponse createAccount(@NotNull UserRequest userRequest) {
        /*
          this checks if the user already has an account number
          then it respond with account exist code and message with the NULL account info since it was not created
         */
        if(userRepo.existsByEmail(userRequest.getEmail())){
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_EXISTS_CODE)
                    .responseMessage(Response.ACCOUNT_EXISTS_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        /*
          This creates an instance of a new user to be saved
          inside the Database
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
                .type(AccountType.SAVINGS)
                .address(userRequest.getAddress())
                .altPhoneNumber(userRequest.getAltPhoneNumber())
                .phoneNumber(userRequest.getPhoneNumber())
                .Status("ACTIVE")
                .password(userRequest.getPassword())
                .build();

        /*
          After all the properties have been build using the builder method
          It is then saved into the database using the repository
         */
        User savedUser = userRepo.save(newUser);

        /*
         Send the email alert to user after saving into the database;
          This is achieved by building the email details instance, adding the recipient, subject and body
          And passing it into the Send-email method that is in the email service that was autowired
         */

        EmailDetails emailDetails = EmailDetails.builder()
                .recipient(savedUser.getEmail())
                .subject("ACCOUNT CREATION")
                .messageBody("Congratulations!!!!, Account created Successfully.\nYour Account Details: \n" +
                        "Account Name: " + savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName() +
                        "\nAccount Number: " + savedUser.getAccountNumber())
                //.attachment(null)
                .build();

        emailService.sendEmail(emailDetails);

        /*
          The response of the method is the success code and message alongside the account information
         */
        return BankResponse.builder()
                .responseCode(Response.ACCOUNT_CREATION_SUCCESS_CODE)
                .responseMessage(Response.ACCOUNT_CREATION_MESSAGE)
                .accountInfo(AccountInfo.builder()
                        //.type(AccountType.SAVINGS)
                        .accountBalance(savedUser.getAccountBalance())
                        .accountNumber(savedUser.getAccountNumber())
                        .accountName(savedUser.getFirstName() + " " + savedUser.getLastName() + " " + savedUser.getOtherName())
                        .build())
                .build();
    }
//    @Override
//    public void saveVerificationToken(String token, UserRequest user) {
//        VerificationToken verificationToken = new VerificationToken(token, user);
//        verificationRepository.save(verificationToken);
//    }

    @Override
    public BankResponse balanceEnquiry(@NotNull EnquiryRequest request) {
        boolean ifAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
        if(!ifAccountExist){
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_DO_NOT_EXIST_CODE)
                    .responseMessage(Response.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else{
            User foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_FOUND_CODE)
                    .responseMessage(Response.ACCOUNT_FOUND_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountName(foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName())
                            .accountNumber(request.getAccountNumber())
                            .accountBalance(foundUser.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public String nameEnquiry(@NotNull EnquiryRequest request) {
        boolean ifAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
        User foundUser = null;
        if (!ifAccountExist) {
            BankResponse.builder()
                    .responseCode(Response.ACCOUNT_DO_NOT_EXIST_CODE)
                    .responseMessage(Response.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        } else {
            foundUser = userRepo.findByAccountNumber(request.getAccountNumber());
        }
        assert foundUser != null;
        return foundUser.getFirstName() + " " + foundUser.getLastName() + " " + foundUser.getOtherName();
    }

    /**
     * Checks
     * @param creditRequest: This is the account number
     * @return BankResponse
     */
    @Override
    public BankResponse creditAccount(@NotNull CreditDebitRequest creditRequest) {
        boolean ifAccountExist = userRepo.existsByAccountNumber(creditRequest.getAccountNumber());
        if(!ifAccountExist){
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_DO_NOT_EXIST_CODE)
                    .responseMessage(Response.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }else {

            User userToCredit = userRepo.findByAccountNumber(creditRequest.getAccountNumber());
            userToCredit.setAccountBalance(userToCredit.getAccountBalance().add(creditRequest.getAmount()));

            userRepo.save(userToCredit);

            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(userToCredit.getEmail())
                    .subject("CREDIT ALERT")
                    .messageBody("Your Account " + userToCredit.getAccountNumber() + " has been credited successfully \n" +
                            "Your Account balance is " + userToCredit.getAccountBalance() + "\nThank you for choosing Global Bank")
                    //.attachment(null)
                    .build();

            emailService.sendEmail(emailDetails);
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_CREDITED_CODE)
                    .responseMessage(Response.ACCOUNT_CREDITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(userToCredit.getAccountNumber())
                            .accountName(userToCredit.getFirstName() + " " + userToCredit.getLastName() + " " + userToCredit.getOtherName())
                            .accountBalance(userToCredit.getAccountBalance())
                            .build())
                    .build();
        }
    }

    @Override
    public BankResponse debitAccount(@NotNull CreditDebitRequest debitRequest) {
        boolean ifAccountExist = userRepo.existsByAccountNumber(debitRequest.getAccountNumber());
        if(!ifAccountExist){
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_DO_NOT_EXIST_CODE)
                    .responseMessage(Response.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User userToDebit = userRepo.findByAccountNumber(debitRequest.getAccountNumber());
        int currentBalance = userToDebit.getAccountBalance().compareTo(debitRequest.getAmount());
        if (currentBalance < 0) {
            return BankResponse.builder()
                    .responseCode(Response.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(Response.INSUFFICIENT_BALANCE_MESSAGE)
                    .build();
        } else {
            userToDebit.setAccountBalance(userToDebit.getAccountBalance().subtract(debitRequest.getAmount()));
            userRepo.save(userToDebit);
            EmailDetails emailDetails = EmailDetails.builder()
                    .recipient(userToDebit.getEmail())
                    .subject("DEBIT ALERT")
                    .messageBody("Your Account " + userToDebit.getAccountNumber() + " has been debited successfully \n" +
                            "Your Account balance is " + userToDebit.getAccountBalance() + "\nThank you for choosing Global Bank")
                    //.attachment(null)
                    .build();
            emailService.sendEmail(emailDetails);
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_DEBITED_CODE)
                    .responseMessage(Response.ACCOUNT_DEBITED_MESSAGE)
                    .accountInfo(AccountInfo.builder()
                            .accountNumber(userToDebit.getAccountNumber())
                            .accountName(userToDebit.getFirstName() + " " + userToDebit.getLastName() + " " + userToDebit.getOtherName())
                            .accountBalance(userToDebit.getAccountBalance())
                            .build())
                    .build();
            }
        }

    @Override
    public BankResponse transferMoney(@NotNull Transfer transferRequest) {

        boolean ifAccountExist = userRepo.existsByAccountNumber(transferRequest.getReceiver());
        if(!ifAccountExist){
            return BankResponse.builder()
                    .responseCode(Response.ACCOUNT_DO_NOT_EXIST_CODE)
                    .responseMessage(Response.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
                    .accountInfo(null)
                    .build();
        }
        User senderAccount = userRepo.findByAccountNumber(transferRequest.getSender());
        if(transferRequest.getAmount().compareTo(senderAccount.getAccountBalance()) < 0){
            return BankResponse.builder()
                    .responseCode(Response.INSUFFICIENT_BALANCE_CODE)
                    .responseMessage(Response.INSUFFICIENT_BALANCE_MESSAGE)
                    .build();
        }else{
            senderAccount.setAccountBalance(senderAccount.getAccountBalance().subtract(transferRequest.getAmount()));
            userRepo.save(senderAccount);
            EmailDetails debitAlert = EmailDetails.builder()
                    .recipient(senderAccount.getEmail())
                    .subject("DEBIT ALERT")
                    .messageBody("Your Account " + senderAccount.getAccountNumber() + " has been debited successfully \n" +
                            "Your Account balance is " + senderAccount.getAccountBalance() + "\nThank you for choosing Global Bank")
                    //.attachment(null)
                    .build();
            emailService.sendEmail(debitAlert);

            User receiverAccount = userRepo.findByAccountNumber(transferRequest.getReceiver());
            receiverAccount.setAccountBalance(senderAccount.getAccountBalance().add(transferRequest.getAmount()));
            userRepo.save(receiverAccount);
            EmailDetails creditAlert = EmailDetails.builder()
                    .recipient(receiverAccount.getEmail())
                    .subject("CREDIT ALERT")
                    .messageBody("Your Account " + receiverAccount.getAccountNumber() + " has been credited successfully \n" +
                            "Your Account balance is " + receiverAccount.getAccountBalance() + "\nThank you for choosing Global Bank")
                    //.attachment(null)
                    .build();
            emailService.sendEmail(creditAlert);

        }
        return BankResponse.builder()
                .responseCode(Response.TRANSFER_SUCCESSFUL_CODE)
                .responseMessage(Response.TRANSFER_SUCCESSFUL_MESSAGE)
                .build();
    }

}
