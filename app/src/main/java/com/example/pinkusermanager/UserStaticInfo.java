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
        User u1 = new User("Рома", "Я устал", 19, 0);
        u1.setProfileId("id_roma");

        User u2 = new User("Настя", "В сети", 19, 1);
        u2.setProfileId("id_nastya1");

        User u3 = new User("Вика", "Я усталь", 19, 2);
        u3.setProfileId("id_vika");

        User u4 = new User("Настя", "Привет", 19, 0);
        u4.setProfileId("id_nastya2");

        users.add(u1);
        users.add(u2);
        users.add(u3);
        users.add(u4);
    }
}