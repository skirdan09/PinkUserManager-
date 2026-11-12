package com.example.pinkusermanager;

import android.content.SharedPreferences;

public class Transform {
    public static final String APP_PREFERENCES = "mysettings";
    public static final String LOGIN = "login";
    public static final String PASSWORD = "password";

    public static int parseIntOrDefault(String whatParse, int defaultValue) {
        int parse;
        try { parse = Integer.parseInt(whatParse); }
        catch (NumberFormatException e) { parse = defaultValue; }
        return parse;
    }

    public static Boolean StringNoNull(String string) {
        if (string == null) return false;
        return string.length() != 0;
    }

    public static void SaveUser(SharedPreferences sp, String login, String password) {
        SharedPreferences.Editor e = sp.edit();
        e.putString(LOGIN, login);
        e.putString(PASSWORD, password);
        e.apply();
    }
}