package com.fintech.globalBank.Controller;

//import com.fintech.globalBank.Util.RegistrationCompleteEvent;
import com.fintech.globalBank.dto.CreditDebitRequest;
import com.fintech.globalBank.dto.EnquiryRequest;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.jetbrains.annotations.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEvent;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import com.fintech.globalBank.Service.UserService;
import com.fintech.globalBank.dto.BankResponse;
import com.fintech.globalBank.dto.UserRequest;

@RestController
@RequestMapping("/api/user")
@RequiredArgsConstructor
public class UserController {

    @Autowired
    private final UserService uService;
    @Autowired
    private ApplicationEventPublisher publish;

    @PostMapping("/register")
    @ResponseStatus(HttpStatus.CREATED)
    public BankResponse createAccount(@RequestBody UserRequest userRequest){
//        publish.publishEvent(new RegistrationCompleteEvent(userRequest, applicationUrl(mailRequest)));
        return uService.createAccount(userRequest);
    }
//    private String applicationUrl( HttpServletRequest mailRequest) {
//        return "http://" + mailRequest.getServerName() +":" + mailRequest.getServerPort() + mailRequest.getContextPath();
//    }
    @GetMapping("")
    @ResponseStatus(HttpStatus.OK)
    public String getUser(@RequestBody EnquiryRequest request){
        return uService.nameEnquiry(request);
    }
    @PostMapping("/credit")
    @ResponseStatus(HttpStatus.CREATED)
    public BankResponse creditUser(@RequestBody CreditDebitRequest request){
        return uService.creditAccount(request);
    }
    @GetMapping("/enquiry")
    @ResponseStatus(HttpStatus.OK)
    public BankResponse accountEnquiry(@NotNull EnquiryRequest request){
        return uService.balanceEnquiry(request);
    }
    @PostMapping("/debit")
    @ResponseStatus(HttpStatus.CREATED)
    public BankResponse debitUser(@RequestBody CreditDebitRequest debitRequest){
        return uService.debitAccount(debitRequest);
    }
    
}
