package com.example.pinkusermanager;

import java.util.ArrayList;
import java.util.List;

public class UserStaticInfo {
    public static List<User> users = new ArrayList<>();
    public final static String POSITION = "position";
    public final static String USERS_SIGN_IN_INFO = "UsersSignInInfo";
    public final static String USERS_PROFILE_INFO = "UsersProfileInfo";
    public final static String PROFILE_ID = "profileId";
    public final static String PASSWORD = "password";
    public final static String AGE = "age";
    public final static String NAME = "name";
    public final static String STATE = "state";
    public static String profileId;

    public UserStaticInfo() {
        if (users.size() == 0) {
            AddUsersInList();
        }
    }

    private void AddUsersInList() {
        users.add(new User("Настя", "Я усталь", 19, 0));
        users.add(new User("Настя", "В сети", 19, 1));
        users.add(new User("Настя", "Я усталь", 19, 2));
        users.add(new User("Настя", "Привет", 19, 0));
    }
}