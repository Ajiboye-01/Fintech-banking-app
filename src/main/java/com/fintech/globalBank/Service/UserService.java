package com.fintech.globalBank.Service;

import com.fintech.globalBank.dto.BankResponse;
import com.fintech.globalBank.dto.UserRequest;

public interface UserService {
    BankResponse createAccount(UserRequest userRequest);
}
