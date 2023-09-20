package com.fintech.globalBank.Controller;

import com.fintech.globalBank.dto.CreditDebitRequest;
import com.fintech.globalBank.dto.EnquiryRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import com.fintech.globalBank.Service.UserService;
import com.fintech.globalBank.dto.BankResponse;
import com.fintech.globalBank.dto.UserRequest;

@RestController
@RequestMapping("/api/user")
public class UserController {


    @Autowired
    private UserService uService;

    @PostMapping("")
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
        return uService.createAccount(userRequest);
    }
    @GetMapping("")
    public String getUser(@RequestBody EnquiryRequest request){
        return uService.nameEnquiry(request);
    }
    @PostMapping("/credit")
    public BankResponse creditUser(@RequestBody CreditDebitRequest request){
        return uService.creditAccount(request);
    }
    
}
