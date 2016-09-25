package com.walkindeep.teammanagerpreview.UI;

import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.walkindeep.teammanagerpreview.DAO.Constant;
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

/**
 * Created by Samsung on 2016/7/24.
 */
public class NewProject extends AppCompatActivity {
    List<String> nametemp = new ArrayList<String>();//后台返回项目名List
    JSONObject jsonGet = new JSONObject();
    JSONObject jsonpost = new JSONObject();
    JSONObject jsonoutside = new JSONObject();
    JSONObject jsonproject = new JSONObject();
    JSONObject jsonprojects = new JSONObject();
    JSONArray projectarray = new JSONArray();
    JSONObject enabled_module_names = new JSONObject();
    /**
     * 新创建项目的名字
     */
    private EditText name;
    /**
     * 新创建项目的相关描述
     */
    private EditText description;
    /**
     * 项目的识别
     */
    private EditText identifier;
    /**
     * 项目的主页
     */
    private EditText home;
    private CheckBox publicBox;
    private TextView showname;
    private TextView showdescription;
    private TextView showidentifier;
    private TextView showhome;
    private TextView showparent;
    private TextView showparent_id;
    private CheckBox extendsBox;
    private CheckBox issue_track;
    private CheckBox time_tracking;
    private CheckBox news;
    private CheckBox documents;
    private CheckBox files;
    private CheckBox wiki;
    private CheckBox repository;
    private CheckBox boards;
    private CheckBox calendar;
    private CheckBox gantt;
    private CheckBox mission;
    private CheckBox function;
    private CheckBox supporting;
    private Button commitProject;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createproject);


       /*测试块，用于测试show project相关函数*/
        Button buttonlist = (Button) findViewById(R.id.button2);
        buttonlist.setEnabled(false);//设置成按钮不可点击，对应函数修改过后，可删除测试
        Button buttondelete = (Button) findViewById(R.id.button3);
        buttondelete.setEnabled(false);
        Button buttonupdate = (Button) findViewById(R.id.button4);
        //listproject函数待修改
       /* buttonlist.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                ListingProjects(User.getUser());
            }
        });*/

        //更新函数测试按钮-buttonupdateProject
        buttonupdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProject("16", User.getUser());
            }
        });
        //delete函数待修改
       /* buttondelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               deleteProject("18",User.getUser());
            }
        });*/


        //-----------------------------------------------------------------------------------------------------//
        try {
            //定义默认值!!track_model不知道应该传送到那里去
            jsonpost.put("name", "");
            jsonpost.put("identifier", "");
            jsonpost.put("description", "");
            jsonpost.put("homepage", "");
            jsonpost.put("is_public", false);
            jsonpost.put("issue_tracking", true);
            jsonpost.put("time_tracking", true);
            jsonpost.put("new", true);
            jsonpost.put("files", true);
            jsonpost.put("Wiki", true);
            jsonpost.put("repository", true);
            jsonpost.put("boards", true);
            jsonpost.put("calender", true);
            jsonpost.put("gantt", true);
            jsonpost.put("status", 1);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        /*name实现*/
        name = (EditText) findViewById(R.id.editText);
        name.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    jsonpost.put("name", s.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /* project_description实现*/
        description = (EditText) findViewById(R.id.editText2);
        description.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    jsonpost.put("description", s.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /* identifier实现*/
        identifier = (EditText) findViewById(R.id.editText3);
        identifier.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                //此时识别
                //长度必须在 1 到 100 个字符之间。 仅小写字母（a-z）、数字、破折号（-）和下划线（_）可以使用。
                // 一旦保存，标识无法修改。

                //此处正则检测表达式待修改
             /*  String Regex="([a-z]|[0-9]|-|_)*";
                Pattern pattern;
                Matcher matcher;
                pattern=Pattern.compile(Regex);
                matcher=pattern.matcher(s.toString());
                if(!matcher.matches())
                    identifier.setError("包含非法字符");
                    */

            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    jsonpost.put("identifier", s.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /*主页描述实现*/
        home = (EditText) findViewById(R.id.editText4);
        home.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
            }

            @Override
            public void afterTextChanged(Editable s) {
                try {
                    jsonpost.put("homepage", s.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /*spinner实现*/

        String url = Constant.WEBURL + ("projects.json");

        final User user = User.getUser();

        /*新建一个get请求*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject userIssuesJSONObject = null;
                        try {
                            userIssuesJSONObject = new JSONObject(response);
                            try {
                                projectarray = userIssuesJSONObject.getJSONArray("projects");
                                Log.d("projectarray", projectarray.toString());
                                for (int i = 0; i < projectarray.length(); i++) {
                                    nametemp.add(((JSONObject) projectarray.get(i)).getString("name"));
                                    Log.d("spinner_name", ((JSONObject) projectarray.get(i)).getString("name"));
                                }
                                ;
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                Log.e("error", error.networkResponse.toString());
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
        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
        networkRequestController.getRequestQueue().add(stringRequest);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, nametemp);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        final Spinner spinnerparent = (Spinner) findViewById(R.id.spinner1);
        spinnerparent.setAdapter(adapter);
        //数据加载在work里面动态添加
        // nametemp 的声明加在开头
        spinnerparent.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                try {
                    spinnerparent.setSelection(position, true);

                    JSONObject parenttemp = new JSONObject();
                    parenttemp.put("id", ((JSONObject) projectarray.get(position)).getString("id"));
                    parenttemp.put("name", ((JSONObject) projectarray.get(position)).getString("name"));
                    jsonpost.put("parent", parenttemp);
                } catch (JSONException e) {

                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        /*checkBox实现*/
        publicBox = (CheckBox) findViewById(R.id.checkBox);
        extendsBox = (CheckBox) findViewById(R.id.checkBox11);
        //model
        issue_track = (CheckBox) findViewById(R.id.checkBox2);
        issue_track.setChecked(true);
        time_tracking = (CheckBox) findViewById(R.id.checkBox3);
        time_tracking.setChecked(true);
        news = (CheckBox) findViewById(R.id.checkBox4);
        news.setChecked(true);
        documents = (CheckBox) findViewById(R.id.checkBox5);
        documents.setChecked(true);
        files = (CheckBox) findViewById(R.id.checkBox6);
        files.setChecked(true);
        wiki = (CheckBox) findViewById(R.id.checkBox12);
        wiki.setChecked(true);
        repository = (CheckBox) findViewById(R.id.checkBox7);
        repository.setChecked(true);
        boards = (CheckBox) findViewById(R.id.checkBox8);
        boards.setChecked(true);
        calendar = (CheckBox) findViewById(R.id.checkBox9);
        calendar.setChecked(true);
        gantt = (CheckBox) findViewById(R.id.checkBox10);
        gantt.setChecked(true);
        //trackers label
        mission = (CheckBox) findViewById(R.id.checkBox13);
        mission.setChecked(true);
        function = (CheckBox) findViewById(R.id.checkBox14);
        supporting = (CheckBox) findViewById(R.id.checkBox15);


        /*实现publicBox监听*/
        publicBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    jsonpost.put("is_public", isChecked);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /*实现extendsBox监听*/
        extendsBox.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    jsonpost.put("project_inherit_members", isChecked);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        /*对于模块跟踪监听*/
        issue_track.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("issue_tracking", "issue_tracking");
                    else
                        enabled_module_names.remove("issue_tracking");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于时间跟踪监听*/
        time_tracking.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("time_tracking", "time_tracking");
                    else
                        enabled_module_names.remove("time_tracking");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于新闻checkbox选项监听*/
        news.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("news", "issue_tracking");
                    else
                        enabled_module_names.remove("news");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于文档checkbox选项监听*/
        documents.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("documents", "documents");
                    else
                        enabled_module_names.remove("documents");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于文件checkbox选项监听*/
        files.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("files", "files");
                    else
                        enabled_module_names.remove("files");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于维基checkbox选项监听*/
        wiki.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("wiki", "wiki");
                    else
                        enabled_module_names.remove("wiki");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于repository checkbox选项监听*/
        repository.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("repository", "repository");
                    else
                        enabled_module_names.remove("repository");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


         /*对于board checkbox选项监听*/
        boards.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("boards", "boards");
                    else
                        enabled_module_names.remove("boards");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于日历 checkbox选项监听*/
        calendar.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("calendar", "calendar");
                    else
                        enabled_module_names.remove("calendar");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

         /*对于甘特图 checkbox选项监听*/
        gantt.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked)
                        enabled_module_names.put("gantt", "gantt");
                    else
                        enabled_module_names.remove("gantt");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        //这里有个问题，在网页版上如果三个跟踪同时都没有被选择的话，在传递参数的时候，projects.json就没有对应的项目，
        // 不知道应该向哪里传数据，因此将此项目默认为必须要选择一个
        /*模块监听-任务，功能，支持*/
        mission.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked == true)
                        jsonpost.put("status", 1);
                    else
                        jsonpost.put("mission", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        function.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked == true)
                        jsonpost.put("status", 1);
                    else
                        jsonpost.put("function", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });

        supporting.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                try {
                    if (isChecked == true)
                        jsonpost.put("supporting", 1);
                    else
                        jsonpost.put("status", 0);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        /*创建项目按钮的实现 */
        commitProject = (Button) findViewById(R.id.button);
        commitProject.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    String s = jsonpost.getString("name");
                    if (jsonpost.getString("name") == "")
                        name.setError("项目名字不能为空");
                    else if (jsonpost.getString("identifier") == "")
                        identifier.setError("项目ID不能为空");
                    else

                    {
                        createProject(User.getUser(), jsonpost);
                    }
                    ;
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    /**
     * 创建一个新项目
     *
     * @param user 当前用户
     * @param json 包含用户新创建的项目填写所有信息
     */
    public void createProject(final User user, JSONObject json) {
        User tempuser = User.init("guojiahao", "teammanager");
        tempuser.setidcount(user.getidcount() + 1);

        // final JSONObject temp = json;
        try {
            jsonoutside.put("id", user.getidcount());
            jsonoutside.put("name", json.getString("name"));
            jsonoutside.put("identifier", json.getString("identifier"));
            jsonoutside.put("description", json.getString("description"));
            jsonoutside.put("status", json.getInt("status"));
            jsonoutside.put("is_public", json.getBoolean("is_public"));
            if (json.has("parent"))
                jsonoutside.put("parent", json.getJSONObject("parent"));
            //设置当前时间
            jsonoutside.put("created_on", "2016-07-26T15:32:18Z");
            jsonoutside.put("updated_on", "2016-07-26T15:32:18Z");
            jsonproject.put("project", jsonoutside);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        ;


        //    abstractdata.getData("projects.json",NewProject.this,User.getUser());

        String url = Constant.WEBURL + "projects.json";

        //创建一个用于post的网络请求
        JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                jsonproject,
                new Response.Listener<JSONObject>() {
                    @Override
                    public void onResponse(JSONObject response) {
                        Log.d("!", "response -> " + response.toString());//在android studio中打印response，正式版应移除这行
                        Toast.makeText(NewProject.this, "创建成功", Toast.LENGTH_SHORT).show();
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("ERROR", "onErrorResponse");//在android studio中打印错误信息,正式版应移除
                //此处无法get到代码。如果id冲突，会退出。。。待解决
                if (error.networkResponse.statusCode == 422)
                    Toast.makeText(NewProject.this, "创建失败，该id已经被使用，错误代码422", Toast.LENGTH_SHORT).show();
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

        //把post请求添加到全局队列中
        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
        networkRequestController.getRequestQueue().add(jsonObjectRequest);
    }

    ;


    /*显示项目列表*///--------------------------------------------------------------------------

    /**
     * 显示当前用户所有项目的列表
     *
     * @param user 当前用户
     */
    public void ListingProjects(final User user) {
            /*获取后台数据*/

        String url = Constant.WEBURL + "projects.json";

        /*新建一个get请求*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject userIssuesJSONObject = null;
                        try {
                            userIssuesJSONObject = new JSONObject(response);
                            try {
                                projectarray = userIssuesJSONObject.getJSONArray("projects");
                                Log.d("projectarray", projectarray.toString());
                                for (int i = 0; i < projectarray.length(); i++) {
                                    nametemp.add(((JSONObject) projectarray.get(i)).getString("name"));
                                    Log.d("spinner_name", ((JSONObject) projectarray.get(i)).getString("name"));
                                }
                                ;
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                Log.e("error", error.networkResponse.toString());
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
        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
        networkRequestController.getRequestQueue().add(stringRequest);

        Log.d("jsonarray", projectarray.toString());
        projectarray.put(jsonpost);
        showListView(projectarray);
    }

    ;

    /*展示对应user下所有的项目--下次迭代可以添加关键词筛选优化*/
    public void showListView(JSONArray array) {
        setContentView(R.layout.showprojectlist);
        final List<JSONObject> projectlist = new ArrayList<JSONObject>();
        for (int i = 0; i < array.length(); i++)
            try {
                projectlist.add((JSONObject) array.get(i));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        projectAdapter adapter = new projectAdapter(NewProject.this, R.layout.projectlistlayout, projectlist);
        ListView listview = (ListView) findViewById(R.id.showprojectlistlayout);
        listview.setAdapter(adapter);
        listview.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                JSONObject projecttemp = projectlist.get(position);
                try {
                    showingProject(projecttemp.getString("id"));
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            ;
        });
    }

    ;

    //展示model
    public void setCheckStatus(CheckBox c, String paramter) {
        try {
            c.setChecked(jsonGet.getBoolean(paramter));
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    /**
     * 展示其中一个项目
     *
     * @param id 对应项目的id
     */
    public void showingProject(String id) {
        for (int i = 0; i < projectarray.length(); i++) {
            try {
                if (((JSONObject) projectarray.get(i)).getString("id").equals(id)) {
                    JSONObject jsontemp = (JSONObject) projectarray.get(i);
                    setContentView(R.layout.project);

                    showname = (TextView) findViewById(R.id.textView20);
                    showname.setText(jsontemp.getString("name").toString());

                    if (jsontemp.has("description")) {
                        showdescription = (TextView) findViewById(R.id.textView22);
                        showdescription.setText(jsontemp.getString("description"));
                    }

                    showidentifier = (TextView) findViewById(R.id.textView24);
                    showidentifier.setText(jsontemp.getString("identifier"));

                    if (jsontemp.has("home")) {
                        showhome = (TextView) findViewById(R.id.textView26);
                        showhome.setText(jsontemp.getString("homepage"));
                    }
                    ;

                    if (jsontemp.has("parent")) {
                        showparent = (TextView) findViewById(R.id.textView28);
                        showparent_id = (TextView) findViewById(R.id.textView29);
                        showparent.setText((jsontemp.getJSONObject("parent")).getString("name"));
                        showparent_id.setText((jsontemp.getJSONObject("parent")).getString("identifier"));
                    }
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    }

    ;

    /**
     * 更新项目
     *
     * @param id   想要更新的项目id
     * @param user 想要更新的对应的user账户
     *///----------------------------------------------------------------------------------------
    public void updateProject(String id, final User user) {
        String url = Constant.WEBURL + ("projects.json");


        /*新建一个get请求*/
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject userIssuesJSONObject = null;
                        try {
                            userIssuesJSONObject = new JSONObject(response);
                            try {
                                projectarray = userIssuesJSONObject.getJSONArray("projects");
                                Log.d("projectarray", projectarray.toString());
                                for (int i = 0; i < projectarray.length(); i++) {
                                    nametemp.add(((JSONObject) projectarray.get(i)).getString("name"));
                                    Log.d("spinner_name", ((JSONObject) projectarray.get(i)).getString("name"));
                                }
                                ;
                            } catch (JSONException e) {
                                e.printStackTrace();
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
                Log.e("error", error.networkResponse.toString());
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
        NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
        networkRequestController.getRequestQueue().add(stringRequest);

        if (id == null)
            showListView(projectarray);
        else
            showingProject(id);
    }

    ;

    /**
     * 删除项目
     *
     * @param id   想要删除项目对应的id
     * @param user 被删除项目所在的用户账户
     */
    public void deleteProject(String id, final User user) {
        // Instantiate the RequestQueue.
        RequestQueue queue = Volley.newRequestQueue(this);
        String url = "http://teammanager.tk/projects/" + id + ".json";
        // Request a string response from the provided URL.


        StringRequest stringRequest = new StringRequest(Request.Method.DELETE, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        Log.e("LOGIN-ERROR", "Abstractget_error" + response);
                    }
                }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Log.e("LOGIN-ERROR", "Abstractget_error" + error.getMessage(), error);
                byte[] htmlBodyBytes = error.networkResponse.data;
                Log.e("LOGIN-ERROR", "Abstractget_error" + new String(htmlBodyBytes), error);
                error.printStackTrace();
                System.out.println((error.getMessage()));
                System.out.println(error.networkResponse.statusCode);
                Log.e("response error", error.toString());
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

    ;

    /*重写适配器*/
    public class projectAdapter extends ArrayAdapter<JSONObject> {
        private int a;

        public projectAdapter(Context context, int textViewResourseId, List<JSONObject> object) {
            super(context, textViewResourseId, object);
            a = textViewResourseId;
        }

        ;

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            JSONObject project = getItem(position);
            View view = LayoutInflater.from(getContext()).inflate(a, null);
            TextView name = (TextView) findViewById(R.id.textView16);
            TextView desc = (TextView) findViewById(R.id.textView17);
            TextView id = (TextView) findViewById(R.id.textView18);
            try {
                name.setText(project.getString("name"));
                desc.setText(project.getString("description"));
                id.setText(project.getString("identifier"));
            } catch (JSONException e) {
                e.printStackTrace();
            }
            return view;
        }

        ;
    }
};







