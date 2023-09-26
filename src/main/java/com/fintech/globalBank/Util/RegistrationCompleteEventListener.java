//package com.fintech.globalBank.Util;
//
//import com.fintech.globalBank.Entity.User;
//import com.fintech.globalBank.Service.UserService;
//import com.fintech.globalBank.dto.UserRequest;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.context.ApplicationListener;
//import org.springframework.stereotype.Component;
//
//
//import java.util.UUID;
//
//@Component
//public class RegistrationCompleteEventListener implements ApplicationListener<RegistrationCompleteEvent> {
//
//    @Autowired
//    private UserService userService;
//    @Override
//    public void onApplicationEvent(RegistrationCompleteEvent event) {
//        UserRequest user = event.getUser();
//        String token = UUID.randomUUID().toString();
//        userService.saveVerificationToken(token, user);
//    }
//}
