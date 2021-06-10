package com.neonflame.projectQ.Utility;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmailValidator {
    private static final String regex = "^(.+)@(.+)$";
    private static final Pattern pattern = Pattern.compile(regex);

    public static boolean isValid (String email) {
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();
    }
}
