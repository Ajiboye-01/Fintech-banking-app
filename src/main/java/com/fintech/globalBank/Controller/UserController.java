package com.fintech.globalBank.Controller;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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
    
}
