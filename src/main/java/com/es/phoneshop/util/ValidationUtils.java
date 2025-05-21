package com.es.phoneshop.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class ValidationUtils {
    public static String NAME_AND_SURNAME_REGEX = "^[A-Za-z\\s]+$";
    public static String PHONE_REGEX = "^\\+\\d{1,13}$";
    public static String DELIVERY_ADDRESS_REGEX = ".*";
    public static String PRICE_REGEX = "^\\d+(\\.\\d+)?$";

    public static boolean validateString(String input, String regex) {
        if (input == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }

    public static boolean validatePrice(String input, String regex) {
        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }
}
