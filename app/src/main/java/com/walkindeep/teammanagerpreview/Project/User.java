package com.walkindeep.teammanagerpreview.Project;

/**
 * Created by jiahao on 2016-05-01.
 */

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.dexafree.materialList.card.Card;
import com.walkindeep.teammanagerpreview.DAO.AbstractDataQuery;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;
import com.walkindeep.teammanagerpreview.MyApplication;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

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

    /**
     * 用户的待完成的任务的列表
     */
    private List<Issue> toDoIssueList =null;


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
     * 更新用户的待完成任务列表
     */
    public void updateToDoIssueList() {
//        IssueDataGetter issueDataGetter = new IssueDataGetter();
//        issueDataGetter.getData("issues.json?assigned_to_id=me" + "&status_id=1", this, user);

        String parameter = "issues.json?assigned_to_id=me" + "&status_id=1";
        String url = "http://teammanager.tk/" + parameter;

        StringRequest updateToDoIssueListStringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject userIssueJSONObject = null;
                        try {
                            userIssueJSONObject = new JSONObject(response);
                            Issue.updateIssueList(toDoIssueList, userIssueJSONObject);
                            //从json分析数据，更新toDoIssueList
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                error.printStackTrace();
                error.getMessage();
                System.out.println(error.networkResponse.statusCode);
                Log.e("error",error.networkResponse.toString());
            }
        }) {

            //            在头部添加用户的账号密码以便进行HTTP基本认证
            @Override
            public Map<String, String> getHeaders() throws AuthFailureError {
                HashMap<String, String> params = new HashMap<String, String>();
                String creds = String.format("%s:%s", user.getUsername(), user.getPassword());
                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
                params.put("Authorization", auth);
                return params;
            }
        };

        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
        RequestQueue requestQueue = networkRequestController.getRequestQueue();
        requestQueue.add(updateToDoIssueListStringRequest);//在请求队列中添加更新issue的请求
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

    /**
     * 获取用户的issue数据
     */
    class IssueDataGetter extends AbstractDataQuery {
        /**
         *
         * @param userIssuesJSONObject
         */
        @Override
        protected void work(JSONObject userIssuesJSONObject) {

        }

    }
}
