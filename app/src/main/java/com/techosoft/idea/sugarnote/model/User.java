package com.techosoft.idea.sugarnote.model;

/**
 * Created by davidsss on 16-08-23.
 */
public class User {
    public String username;
    public int userId;
    public String password;
    public String objId;

    public User(String usr, String psw) {
        username = usr;
        password = psw;
    }
}
