package com.walkindeep.teammanagerpreview;

import android.content.Context;
import android.util.Base64;
import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dexafree.materialList.card.Card;

import org.json.JSONArray;
import org.json.JSONException;
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
     *
     * @param parameter  Redmine API中跟在网址后面的部分，如 issues.json
     * @param context    上下文
     * @param user       传入User类的实例
     * @param jsonObject 传入jsonObject实例，即在Redmine API中需要用到的JSon
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
                        responseHandle();//对http请求后返回的信息进行处理,在DataPost类中此方法默认为空，如需要对http请求返回的数据进行处理，请继承DataPost并重写这个方法
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse");
                errorResponseHandle(error);/*对http请求后返回的信息进行处理（http请求有错误的情况下）
                在DataPost中默认为空，即不处理
                 如果需要处理，请继承DataPost并重写此方法*/
            }
        }) {

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
    private void errorResponseHandle(VolleyError error) {

    }

    /**
     * 对http请求后返回的信息进行处理（http请求无错误的情况下）
     * 在DataPost中默认为空，即不处理
     * 如果需要处理，请继承DataPost并重写此方法
     */
    private void responseHandle() {
    }


}

