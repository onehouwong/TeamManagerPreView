package com.walkindeep.teammanagerpreview.DAO;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.walkindeep.teammanagerpreview.Project.User;

import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;


/**
 * 用于从客户端向后端传输数据的类。
 * 实现了Redmine API。
 */
public class DataPost {
    /**
     * 根据Redmine API 向后端传输数据。
     * <p/>
     * 注意：本方法只适用于注册，因此本方法没有参数<code>user</code>
     * 如果不是用于注册，请使用另一个post方法
     *
     * @param parameter  Redmine API中跟在网址后面的部分，如 issues.json
     * @param context    上下文
     * @param jsonObject 传入jsonObject实例，即在Redmine API中需要用到的JSON
     *                   使用示范：创建issue
     *                   <p/>
     *                   <blockquote><pre>
     *                   Datapost datapost = new Datapost();
     *                   JSONObject insideJsonObject = new JSONObject();
     *                   JSONObject outsideJsonObject = new JSONObject();
     *                   insideJsonObject.put("project_id",1);
     *                   insideJsonObject.put("subject","subjectName");
     *                   insideJsonObject.put("tracker_id",1);
     *                   insideJsonObject.put("status_id",1);
     *                   outsideJsonObject.put("issue",insideJsonObject);
     *                   datapost.post("issues.json",this,user,outsideJsonObject);
     *                   </pre></blockquote>
     *                   注意：处理JSONObject时需要try...catch
     */
    public void post(String parameter, Context context, JSONObject jsonObject) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://teammanager.tk/" + parameter;
        // Request a string response from the provided URL.

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("!", "response -> " + response.toString());//在android studio中打印response，正式版应移除这行
                        responseHandle(response);//对http请求后返回的信息进行处理,在DataPost类中此方法默认为空，如需要对http请求返回的数据进行处理，请继承DataPost并重写这个方法
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse");//在android studio中打印错误信息,正式版应移除
                errorResponseHandle(error);/*对http请求后返回的信息进行处理（http请求有错误的情况下）
                在DataPost中默认为空，即不处理
                 如果需要处理，请继承DataPost并重写此方法*/
            }
        });
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }


    /**
     * 根据Redmine API 向后端传输数据。
     *
     * @param parameter  Redmine API中跟在网址后面的部分，如 issues.json
     * @param context    上下文
     * @param user       传入User类的实例
     * @param jsonObject 传入jsonObject实例，即在Redmine API中需要用到的JSON
     *                   <p/>
     *                   使用示范：创建issue
     *                   <p/>
     *                   <p/>
     *                   <pre>
     *                                                       Datapost datapost = new Datapost();
     *                                                       JSONObject insideJsonObject = new JSONObject();
     *                                                       JSONObject outsideJsonObject = new JSONObject();
     *                                                       insideJsonObject.put("project_id",1);
     *                                                       insideJsonObject.put("subject","subjectName");
     *                                                       insideJsonObject.put("tracker_id",1);
     *                                                       insideJsonObject.put("status_id",1);
     *
     *                                                       outsideJsonObject.put("issue",insideJsonObject);
     *
     *                                                       datapost.post("issues.json",this,user,outsideJsonObject);
     *
     *                                                       </pre>
     *                   注意：处理JSONObject时需要try...catch
     */
    public void post(String parameter, Context context, final User user, JSONObject jsonObject) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://teammanager.tk/" + parameter;
        // Request a string response from the provided URL.

        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonObject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("!", "response -> " + response.toString());//在android studio中打印response，正式版应移除这行
                        responseHandle(response);//对http请求后返回的信息进行处理,在DataPost类中此方法默认为空，如需要对http请求返回的数据进行处理，请继承DataPost并重写这个方法
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse");//在android studio中打印错误信息,正式版应移除
                errorResponseHandle(error);/*对http请求后返回的信息进行处理（http请求有错误的情况下）
                在DataPost中默认为空，即不处理
                 如果需要处理，请继承DataPost并重写此方法*/
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
// Add the request to the RequestQueue.
        queue.add(jsonObjectRequest);
    }

    /**
     * 对http请求后返回的信息进行处理（http请求有错误的情况下）
     * 在DataPost中默认为空，即不处理
     * 如果需要处理，请继承DataPost并重写此方法
     * <p/>
     * 获取HTTP状态码的方法：error.networkResponse.statusCode
     */
    protected void errorResponseHandle(VolleyError error) {

    }

    /**
     * 对http请求后返回的信息进行处理（http请求无错误的情况下）
     * 在DataPost中默认为空，即不处理
     * 如果需要处理，请继承DataPost并重写此方法
     */
    protected void responseHandle(JSONObject response) {
    }


}

