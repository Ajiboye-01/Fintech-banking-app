package com.fintech.globalBank.Util;

import java.time.Year;

public class AccountUtil {
/*
 * This throws custom message if account exists
 */
    public static final String ACCOUNT_EXISTS_CODE = "001";
    public static final String ACCOUNT_EXISTS_MESSAGE = "This User already has an account";

    public static final String ACCOUNT_CREATION_SUCCESS_CODE = "002";
    public static final String ACCOUNT_CREATION_MESSAGE = "Account Created successfully";

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

        StringBuilder accNumber = new StringBuilder();

        return accNumber.append(year).append(randNumber).toString();
    }

}
