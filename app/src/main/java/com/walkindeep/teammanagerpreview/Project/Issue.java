package com.walkindeep.teammanagerpreview.Project;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.walkindeep.teammanagerpreview.DAO.Constant;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 任务类 对应Redmine中的任务
 */
public class Issue {
    /**
     * 待完成的任务列表
     */
    private static List<Issue> toDoIssueList = new ArrayList<>();

    private int issue_id;
    private String projectId;
    private String trackerId;
    private int status_id;
    private String assigned_to_id;
    private String priority_id;
    private String description;
    private String subject;
    private String parent_issue_id;
    private String watcher_user_ids;

    /**
     * issue类的构造方法
     *
     * @param subject     标题
     * @param description 描述
     * @param issue_id    任务id
     */
    public Issue(String subject, String description, int issue_id) {
        this.subject = subject;
        this.description = description;
        this.issue_id = issue_id;
    }

    /**
     * 获取该用户所有的issue
     *
     * @return 装载有所有issue的list
     */
    public static List<Issue> getToDoIssueList() {
        return toDoIssueList;
    }


    /**
     * 获取任务所属项目的id
     *
     * @return 任务所属项目的id
     */
    public String getProjectId() {
        return projectId;
    }

    /**
     * 设置任务所属项目的id
     *
     * @param projectId 任务所属项目的id
     */
    public void setProjectId(String projectId) {
        this.projectId = projectId;
    }

    /**
     * 获取任务的跟踪者的id
     *
     * @return 任务的跟踪者的id
     */
    public String getTrackerId() {
        return trackerId;
    }

    /**
     * 设置跟踪者的id
     *
     * @param trackerId 任务的跟踪者id
     */
    public void setTrackerId(String trackerId) {
        this.trackerId = trackerId;
    }

    /**
     * 获取任务状态值
     *
     * @return 任务状态值。详情请查Redmine API
     */
    public int getStatus_id() {
        return status_id;
    }

    /**
     * 设置状态值
     *
     * @param status_id 任务的状态值
     */
    public void setStatusid(int status_id) {
        this.status_id = status_id;
    }

    /**
     * 获取任务观察者的id
     *
     * @return 观察者的id
     */
    public String getWatcher_user_ids() {
        return watcher_user_ids;
    }

    /**
     * 设置任务观察者的id
     *
     * @param watcher_user_ids 观察者的id
     */
    public void setWatcher_user_ids(String watcher_user_ids) {
        this.watcher_user_ids = watcher_user_ids;
    }

    /**
     * 获取父任务的id
     *
     * @return 父任务的id
     */
    public String getParent_issue_id() {
        return parent_issue_id;
    }

    /**
     * 设置父任务的id
     *
     * @param parent_issue_id 父任务的id
     */
    public void setParent_issue_id(String parent_issue_id) {
        this.parent_issue_id = parent_issue_id;
    }

    /**
     * 获取任务的描述
     *
     * @return 任务的描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置任务的描述
     *
     * @param description 任务的描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * 获取任务的标题
     *
     * @return 任务的标题
     */
    public String getSubject() {
        return subject;
    }

    /**
     * 设置任务的标题
     *
     * @param subject 任务的标题
     */
    public void setSubject(String subject) {
        this.subject = subject;
    }

    /**
     * 获取任务的重要性值
     *
     * @return 任务的重要性值。详情请查阅Redmine Doc
     */
    public String getPriority_id() {
        return priority_id;
    }

    /**
     * 设置任务的重要性值
     *
     * @param priority_id 任务的重要性值。详情请查阅Redmine Doc
     */
    public void setPriority_id(String priority_id) {
        this.priority_id = priority_id;
    }

    /**
     * 获取任务的执行者id
     *
     * @return 任务的执行者id
     */
    public String getAssigned_to_id() {
        return assigned_to_id;
    }

    /**
     * 设置任务的执行者id
     *
     * @param assigned_to_id 任务的执行者id
     */
    public void setAssigned_to_id(String assigned_to_id) {
        this.assigned_to_id = assigned_to_id;
    }

    /**
     * 推送任务的状态值到后端，状态值是类的成员变量  status_id
     *
     * @param context 上下文
     */
    public void pushStatusName(Context context) {
        JSONObject jsonObject = null;
        try {
            jsonObject = new JSONObject("{" +
                    "  \"issue\": {" +
                    "    \"status_id\": \"" + status_id + "\" " +
                    "  }" +
                    "}");
        } catch (JSONException e) {
            e.printStackTrace();
        }

        String parameter = "issues/" + issue_id + ".json";
        String url = Constant.WEBURL + parameter;
        final User user = User.getUser();

        //创建一个用于post数据的请求
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("!", "response -> " + response.toString());//在android studio中打印response，正式版应移除这行
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse");//在android studio中打印错误信息,正式版应移除
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
        //把post请求添加到全局网络访问队列
        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
        networkRequestController.getRequestQueue().add(jsonObjectRequest);

    }

    /**
     * 获取任务id
     *
     * @return 任务id
     */
    public int getIssue_id() {
        return issue_id;
    }

    /**
     * 设置任务id
     *
     * @param issue_id 任务id
     */
    public void setIssue_id(int issue_id) {
        this.issue_id = issue_id;

    }

    public static void updateIssueList(List<Issue> toDoIssueList, JSONObject userIssuesJSONObjectTemp) {
        JSONArray userIssuesJSONArray = null;
        try {
            userIssuesJSONArray = userIssuesJSONObjectTemp.getJSONArray("issues");

        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*用于存储所有issue的list*/
        toDoIssueList.clear();

        for (int i = 0; i < userIssuesJSONArray.length(); i++) {
            try {
                /*创建issue类*/
                Issue issue = new Issue(userIssuesJSONArray.getJSONObject(i).getString("subject"),
                        userIssuesJSONArray.getJSONObject(i).getString("description"),
                        userIssuesJSONArray.getJSONObject(i).getInt("id"));
                toDoIssueList.add(issue);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    }
}
