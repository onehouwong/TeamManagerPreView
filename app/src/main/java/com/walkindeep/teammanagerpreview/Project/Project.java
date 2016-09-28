package com.walkindeep.teammanagerpreview.Project;

/**
 * Created by jiahao on 2016-05-05.
 */

import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import static com.walkindeep.teammanagerpreview.DAO.Constant.WEBURL;

/**
 * 项目类
 */
public class Project {
    /**
     * 项目名称
     */
    private String name;
    private String identifier;
    /**
     * 项目描述
     */
    private String description;
    /**
     * 跟踪者id
     */
    private String tracker_ids;
    /**
     * 父项目id
     */
    private String parent_id;

    public Project(String name, String description,String identifier) {
        this.name = name;
        this.description = description;
        this.identifier=identifier;
    }

    /**
     * @return 项目名称
     */
    public String getName() {
        return name;
    }

    /**
     * 设置项目名称
     *
     * @param name 项目名称
     */
    public void setName(String name) {
        this.name = name;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    /**
     * @return 项目描述
     */
    public String getDescription() {
        return description;
    }

    /**
     * 设置项目描述
     *
     * @param description 项目描述
     */
    public void setDescription(String description) {
        this.description = description;
    }

    /**
     * @return 父项目id
     */
    public String getParent_id() {
        return parent_id;
    }

    /**
     * 设置父项目id
     *
     * @param parent_id 父项目id
     */
    public void setParent_id(String parent_id) {
        this.parent_id = parent_id;
    }

    /**
     * @return 跟踪者id
     *
     */
    public String getTracker_ids() {
        return tracker_ids;
    }

    /**
     * 设置跟踪者id
     * @param tracker_ids 跟踪者id
     */
    public void setTracker_ids(String tracker_ids) {
        this.tracker_ids = tracker_ids;
    }

    public void postProject(final JSONObject jsproject){
        final User user=User.getUser();
        String url=WEBURL+"projects.json";
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsproject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        try {
                            Project project=new Project(jsproject.get("name").toString(),jsproject.get("description").toString(),jsproject.get("identifier").toString());
                            Log.d("!", "response -> " + response.toString());//在android studio中打印response，正式版应移除这行
                            user.insert_Projects_List(project);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }

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


    };
}
