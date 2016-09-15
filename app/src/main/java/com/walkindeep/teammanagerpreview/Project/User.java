package com.walkindeep.teammanagerpreview.Project;

/**
 * Created by jiahao on 2016-05-01.
 */

/**
 * 用户类
 * 实现了单例模式
 */
public class User {
    private int idcount;
    private String name;
    private String identifier;
    private String description;
    private String tracker_ids;
    private String parent_id;

    private static volatile User user = null;
    /**
     * 用户账号
     */
    private String username;
    /**
     * 用户密码
     */
    private String password;
    /**
     * 用于与服务器后台交互的key
     */
    private String key;

    private User(String username, String password) {
        this.username = username;
        this.password = password;

    }

    public int  getidcount(){return idcount;}
    public void setidcount(int a){this.idcount=a;};

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public String getParent_id() {
        return parent_id;
    }

    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    public String getTracker_ids() {
        return tracker_ids;
    }

    public void setTracker_ids(String tracker_ids) {
        this.tracker_ids = tracker_ids;
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
    public static String getUsername() {
        return user.username;
    }

    /**
     * 获取密码
     *
     * @return 密码
     */
    public static String getPassword() {
        return user.password;
    }

    /**
     * 获取key
     * @return key
     */
    public static String getKey() {
        return user.key;
    }

    /**
     * 设置key
     *
     * @param k
     */
    public static void setKey(String k) {
        user.key = k;
    }
}
