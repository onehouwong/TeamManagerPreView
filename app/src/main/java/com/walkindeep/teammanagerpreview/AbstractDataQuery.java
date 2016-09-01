package com.walkindeep.teammanagerpreview;

import android.content.Context;
import android.icu.util.RangeValueIterator;
import android.provider.DocumentsContract;
import android.util.Base64;
import android.util.Log;
import android.util.Xml;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.dexafree.materialList.card.Card;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.ByteArrayInputStream;
import java.io.InputStream;
import java.util.HashMap;
import java.util.Map;

import org.json.XML;

/**
 * Created by jiahao on 2016-05-02.
 */

/**
 * 功能：使用Redmine API向后端发送查询，并接受返回结果，work()指示了接受数据后的处理动作，需要自行实现动作
 * 示范：
 * SUbclsssOfAbstractDataQuery dataQuery = new SUbclsssOfAbstractDataQuery();
 * dataQuery.getData("issues.xml?nometa=1",this, user);
 */
public abstract class AbstractDataQuery {
    private static Object retur(Object stuff) {
        return stuff;
    }

    /**
     * @param parameter api参数，例如：issues.xml?nometa=1
     * @param context   一般用this
     * @param user      用户
     */

    public void getData(String parameter, Context context, final User user) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(context);
        String url = "http://teammanager.tk/" + parameter;
        // Request a string response from the provided URL.

        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject userIssuesJSONObject = null;
                        try {
                            if(response.substring(0,1).equals("{")) {
                                userIssuesJSONObject = new JSONObject(response);
                                work(userIssuesJSONObject);
                            }
                            else {
                                parseXMLWithPull(response);
                            }

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
// Add the request to the RequestQueue.
        queue.add(stringRequest);
    }

    /*自行实现要对后端传输过来的数据的处理方式*/
    protected abstract void work(JSONObject userIssuesJSONObject);
    protected abstract  void parseXMLWithPull(String xmlData);
}

