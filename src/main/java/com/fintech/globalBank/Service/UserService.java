package com.fintech.globalBank.Service;

import com.fintech.globalBank.Entity.User;
import com.fintech.globalBank.dto.*;
import org.springframework.context.annotation.Configuration;


public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);

    String nameEnquiry(EnquiryRequest request);

    BankResponse creditAccount(CreditDebitRequest creditRequest);
    BankResponse debitAccount(CreditDebitRequest debitRequest);

//    void saveVerificationToken(String token, UserRequest user);
    BankResponse transferMoney(Transfer transferRequest);
}
