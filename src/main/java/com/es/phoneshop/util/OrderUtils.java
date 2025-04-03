package com.es.phoneshop.util;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class OrderUtils {
    public static String nameAndSurnameRegex = "^[A-Za-z\\s]+$";
    public static String phoneRegex = "^\\+\\d{1,13}$";
    public static String deliveryAddressRegex = ".*";

    public static boolean validateString(String input, String regex) {
        if (input == null) {
            return false;
        }

        Pattern pattern = Pattern.compile(regex);

        Matcher matcher = pattern.matcher(input);

        return matcher.matches();
    }
}
