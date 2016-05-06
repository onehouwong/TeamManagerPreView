package com.walkindeep.teammanagerpreview;

/**
 * Created by jiahao on 2016-05-01.
 */
public class User {
    private static volatile User user = null;
    private String username;
    private String password;

    private User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public static User getUser() {
        return user;
    }

    public static User init(String username, String password) {
        if (user == null) {
            synchronized (User.class) {
                if (user == null) {
                    user = new User(username, password);
                }
            }
        }
        return user;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }
}
