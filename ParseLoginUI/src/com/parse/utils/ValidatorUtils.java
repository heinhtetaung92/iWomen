package com.parse.utils;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by Toe Lie on 5/6/2015.
 */
public class ValidatorUtils {

    public static boolean isValidMobileNo(String mobileNumber){
        if(mobileNumber.length() == 7 || mobileNumber.length() == 8 || mobileNumber.length() == 9 || mobileNumber.length() == 10 || mobileNumber.length() == 11 ){
            return true;
        }else{
            return false;
        }
    }

    public static boolean isValidNrc(String input) {
        Pattern pattern;
        Matcher matcher;
        final String NRC_PATTERN = "[0-9\\u1040-\\u1049]{1,2}[/][a-zA-Z\\u1000-\\u1021]{3,9}\\([a-zA-Z\u1000-\u109F]+\\)[0-9\\u1040-\\u1049]{6}"; //"([0-9]{1,2}[/][a-zA-Z]{3}\\(N\\))"; //"([\\u1000-\\u1021]{1,2})([0-9]{6})";
        pattern = Pattern.compile(NRC_PATTERN);
        matcher = pattern.matcher(input.toUpperCase());
        return matcher.matches();
    }


    public static boolean isValidPassportNo(String input) {
        Pattern pattern;
        Matcher matcher;
        final String PASSPORT_PATTERN = "[a-zA-Z]+[a-zA-z0-9]{5,}"; //"([0-9]{1,2}[/][a-zA-Z]{3}\\(N\\))"; //"([\\u1000-\\u1021]{1,2})([0-9]{6})";
        pattern = Pattern.compile(PASSPORT_PATTERN);
        matcher = pattern.matcher(input.toUpperCase());
        return matcher.matches();
    }

    public static String toCorrectFormat(String input){
        String stateId = input.substring(input.indexOf('/')+1, input.indexOf('('));
        String formattedStateId = "";

        if(stateId.length() == 3){
            return input.toUpperCase();
        }else if(stateId.length() == 6){
            char first = stateId.charAt(0);
            char second = stateId.charAt(2);
            char third = stateId.charAt(4);

            for(int i=0; i<stateId.length(); i++){
                char c = stateId.charAt(i);
                if(i==0 || i== 2 || i==4){
                    formattedStateId += String.valueOf(c).toUpperCase();
                }else{
                    formattedStateId += String.valueOf(c).toLowerCase();
                }
            }
        }

        String formattedNrcNo = input.substring(0, input.indexOf('/')+1) + formattedStateId + input.substring(input.indexOf('('), input.length());

        return formattedNrcNo;
    }

}
