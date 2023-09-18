package com.fintech.globalBank.Service;

import com.fintech.globalBank.dto.BankResponse;
import com.fintech.globalBank.dto.EnquiryRequest;
import com.fintech.globalBank.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
    BankResponse balanceEnquiry(EnquiryRequest request);
    String nameEnquiry(EnquiryRequest request);
}
