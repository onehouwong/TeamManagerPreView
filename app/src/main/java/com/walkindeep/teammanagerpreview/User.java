package com.walkindeep.teammanagerpreview;

/**
 * Created by jiahao on 2016-05-01.
 */

/**
 * 用户类
 * 实现了单例模式
 */
public class User {
    private static volatile User user = null;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;

    private User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    /**
     * @return 已登录的用户
     */
    public static User getUser() {
        return user;
    }

    /**
     * 初始化用户
     *
     * @param username 账号
     * @param password 密码
     * @return 用户
     */
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

    /**
     * 获取账号
     *
     * @return 账号
     */
    public String getUsername() {
        return username;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public String getPassword() {
        return password;
    }

    /**
     * 用来登录，未完成，可删，可加工
     * by 郭嘉豪创建于5月 注释于2016年7月11日
     * @return
     */
    public boolean authenticate() {
        return false;
    }
}
