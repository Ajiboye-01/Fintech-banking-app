package com.fintech.globalBank.Util;

import com.fintech.globalBank.Repository.UserRepo;
import com.fintech.globalBank.dto.BankResponse;
import com.fintech.globalBank.dto.EnquiryRequest;
import org.springframework.beans.factory.annotation.Autowired;

import java.time.Year;

public class AccountUtil {

    /*
* This has a method that generate account number
* It uses the current year + random 6 digits
*/
    public static String generateAccountNumber(){
        Year currentYear = Year.now();
        int min = 100000;
        int max = 999999;

        //This generates the random number
        int randNum = (int) Math.floor(Math.random() * (max - min + 1));

        //convert and store the current year as string
        String year = String.valueOf(currentYear);

        //convert and stores the random number generated
        String randNumber = String.valueOf(randNum);

        return year + randNumber;
    }

//    public void findAccount(EnquiryRequest request){
//        boolean ifAccountExist = userRepo.existsByAccountNumber(request.getAccountNumber());
//        if(!ifAccountExist){
//            BankResponse.builder()
//                    .responseCode(AccountUtil.ACCOUNT_DO_NOT_EXIST_CODE)
//                    .responseMessage(AccountUtil.ACCOUNT_DOES_NOT_EXIST_MESSAGE)
//                    .accountInfo(null)
//                    .build();
//        }
//    }
}
