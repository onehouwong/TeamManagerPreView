package com.walkindeep.teammanagerpreview.Project;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.walkindeep.teammanagerpreview.Controller.RListener;
import com.walkindeep.teammanagerpreview.Controller.RecyclerViewAdapter;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;
import com.walkindeep.teammanagerpreview.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

import static com.walkindeep.teammanagerpreview.DAO.Constant.WEBURL;

/**
 * Created by Administrator on 2016/9/24.
 */



public class ProjectList extends AppCompatActivity {
    final User user=User.getUser();
    ArrayList<String> ProjectNameList=new ArrayList<String>();
    ArrayList<String> ProjectDescList=new ArrayList<String>();
    JSONObject js =null;
    private RecyclerView mRecyclerView;
    private RecyclerView.Adapter mAdapter;
    private RecyclerView.LayoutManager mLayoutManager;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        show_project_list();

    }



    /*
    *show project list*/
    public void show_project_list(){
        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
        setContentView(R.layout.project_list);
        String url=WEBURL+"projects.json";
        mRecyclerView = (RecyclerView) findViewById(R.id.myproject_list);
        mRecyclerView.setHasFixedSize(true);
        mLayoutManager = new LinearLayoutManager(this);
        mRecyclerView.setLayoutManager(mLayoutManager);

        /*show project list*/
        StringRequest request=new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {

                            js = new JSONObject(response);
                            JSONArray ja = null;
                            ja = js.getJSONArray("projects");
                            for (int i = 0; i < ja.length(); i++) {
                                ProjectNameList.add((((JSONObject) ja.get(i)).get("name")).toString());
                                ProjectDescList.add((((JSONObject) ja.get(i)).get("description")).toString());
                            }
                            mAdapter = new RecyclerViewAdapter(ProjectNameList,ProjectDescList);
                            mRecyclerView.setAdapter(mAdapter);
                            mRecyclerView.addOnItemTouchListener(new RListener(ProjectList.this, mRecyclerView,
                                    new RListener.OnItemClickListener() {
                                        @Override
                                        public void onItemClick(View view, int position) {
                                            //切换到具体项目展示
                                            Toast.makeText(ProjectList.this,"Click "+ProjectNameList.get(position).toString(),Toast.LENGTH_SHORT).show();
                                        };}));
                            //ArrayAdapter<String>adapter=new ArrayAdapter<String>(ProjectList.this,android.R.layout.simple_list_item_1,ProjectNameList);
                            //ListView listView=(ListView)findViewById(R.id.project_list);
                            //listView.setAdapter(adapter);
//                            mAdapter.setitemClickListener(new RecyclerViewAdapter.MyItemClickListener(){
//                                @Override
//                                public void onItemClick(View v, int position) {
//                                    Toast.makeText(ProjectList.this, position,Toast.LENGTH_SHORT).show();
//                                }
//
//                            });


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
        //Log.d("projectList show~~~",ProjectNameList.toString());
        System.out.print(ProjectNameList);

    };
    };


