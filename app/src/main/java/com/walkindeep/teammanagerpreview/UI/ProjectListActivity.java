package com.walkindeep.teammanagerpreview.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static com.walkindeep.teammanagerpreview.DAO.Constant.WEBURL;

/**
 * Created by Administrator on 2016/9/24.
 */

public class ProjectListActivity extends AppCompatActivity {
    final User user=User.getUser();
    ArrayList<String> ProjectNameList=new ArrayList<String>();
    JSONObject js =null;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        ProjectNameList.add("cjy");
        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
       String url=WEBURL+"projects.json";

        /**show project list*/
        StringRequest request=new StringRequest(Request.Method.GET, url,
        new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {

                           js = new JSONObject(response);
                    JSONArray ja = null;
                    ja = js.getJSONArray("projects");
                    for (int i = 0; i < ja.length(); i++)
                        ProjectNameList.add((((JSONObject) ja.get(i)).get("name")).toString());
                    Log.d("projectList show~~~",ProjectNameList.toString());
                    System.out.print("!!!!!!project"+ProjectNameList);
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
        //把网络请求添加到全局网络请求队列

        networkRequestController.getRequestQueue().add(request);
        Log.d("projectList show~~~",ProjectNameList.toString());
        System.out.print(ProjectNameList);

        setContentView(R.layout.project_list);
        ArrayAdapter<String>adapter=new ArrayAdapter<String>(ProjectListActivity.this,android.R.layout.simple_list_item_1,ProjectNameList);
        ListView listView=(ListView)findViewById(R.id.project_list);
        listView.setAdapter(adapter);
        }

    };


