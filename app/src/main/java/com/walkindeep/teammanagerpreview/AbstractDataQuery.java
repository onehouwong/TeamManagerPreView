package com.walkindeep.teammanagerpreview;

import android.content.Context;
import android.util.Base64;

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

import java.util.HashMap;
import java.util.Map;

/**
 * Created by jiahao on 2016-05-02.
 */
public abstract class AbstractDataQuery {
    private static Object retur(Object stuff) {
        return stuff;
    }

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
                            userIssuesJSONObject = new JSONObject(response);
                            work(userIssuesJSONObject);
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }


                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
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
        queue.add(stringRequest);
    }

    /*自行实现要对后端传输过来的数据的处理方式*/
    protected abstract void work(JSONObject userIssuesJSONObject);

}

