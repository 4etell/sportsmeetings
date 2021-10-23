package com.foretell.sportsmeetings.util.string;

public class NamingUtil {
    public static String generateCapitalizedString(String s) {
        return s.substring(0, 1).toUpperCase() + s.substring(1).toLowerCase();
    }
}
