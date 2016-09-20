package com.walkindeep.teammanagerpreview.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.app.Dialog;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.Spinner;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.walkindeep.teammanagerpreview.Controller.Parser;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;
import com.walkindeep.teammanagerpreview.OpenFileDialog;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * 用于创建任务的活动
 */
public class CreateTaskActivity extends AppCompatActivity {

    static private int openfileDialogId = 0;
    int project_id;
    int tracker_id;//跟踪者的ID,在视图中对应是名字,但是在API中要求返回是跟踪的ID,所以这里需要通过名字查找ID
    int status_id;//由于状态只有"新建",所以这个默认参数为0
    int priority_id;//优先级,"低","普通","高","紧急","立刻",分别对于1-5
    String description;//描述,对于创建该任务的描述
    String subject;//主题
    int estimated_hours;//预期时间
    boolean is_private;//是否私有
    private Spinner assignSpinner;
    private List<String> dataAssignSpinner;//动态加载到 assignSpinner的数组,对应视图中指派给谁的数组
    private ArrayAdapter<String> assignAdapter;//assignSpinner适配器,对应视图中指派给谁的适配器
    private Context mContext;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        /**
         * 活动启动所执行的函数
         */
        mContext=this;
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_task);

         project_id=0;
         tracker_id=0;
         status_id=0;
         priority_id=0;
         description="";
         subject="";
         estimated_hours=0;

        assignSpinner = (Spinner) findViewById(R.id.assignSpinner);//新建Spinner对于layout里面的控件
        dataAssignSpinner = new ArrayList<String>();//初始化

        //添加数据
        //这里需要说明一下,Spinner在加载一个动态数组时会出现异步加载问题,导致无法正常显示,这个问题婕妤代码出现过,
        //需要解决这个方法,比较简单的就是先给对应的数组加载一个数据,之后动态添加就没有问题
        dataAssignSpinner.add("");
        dataAssignSpinner.add("<<我>>");


//        DataQuery query = new DataQuery();//创建一个DataQuery的实例
//        query.getData("projects/1/memberships.xml", this, User.getUser());//调用API接口

        String parameter = "projects/1/memberships.xml";
        String url = "http://teammanager.tk/" + parameter;

        final NetworkRequestController networkRequestController = NetworkRequestController.getInstance();//获取网络控制器

     final   User user = User.getUser();

        //创建请求
        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        JSONObject userIssuesJSONObject = null;

                        if(response.substring(0,1).equals("{")) {
                        }
                        else {
                           Parser.parseXMLWithPull(response, dataAssignSpinner);
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
        networkRequestController.getRequestQueue().add(stringRequest);//把request添加到全局网络请求队列




        //适配器
        assignAdapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, dataAssignSpinner);
        assignAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        //加载适配器
        assignSpinner.setAdapter(assignAdapter);
        assignSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
        //跟踪spinner
        Spinner follow=(Spinner) findViewById(R.id.followSpinner);

        follow.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                tracker_id=i+1;//这里加1的原因是,Spinner默认是有0开始,而传参时是默认1开始
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });
         //状态参数
        Spinner stateSpinner=(Spinner)findViewById(R.id.stateSpinner);
        stateSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                status_id=i+1;//这里加1的原因是,Spinner默认是有0开始,而传参时是默认1开始
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //优先级spinner
        final Spinner priority=(Spinner)findViewById(R.id.prioritySpinner);//创建对应的Spinner
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priority_id=i+1;//这里加1的原因是,Spinner默认是有0开始,而传参时是默认1开始
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //百分比spinner
        Spinner finish=(Spinner)findViewById(R.id.followSpinner);//创建对应的Spinner
        //由于这里不需要传入任何参数,仅设置一个监听器,备注:这里应该需要加上这个参数
        finish.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {

            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });



        //选择文件
        Button chooseFile = (Button) findViewById(R.id.choosefile);
        chooseFile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showDialog(openfileDialogId);
            }
        });

        //创建按钮
        Button create =(Button) findViewById(R.id.createTaskButton);
        create.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //主题
                EditText theme = (EditText) findViewById(R.id.themeText);
                subject = theme.getText().toString();//获取对应的字符串

                CheckBox c=(CheckBox)findViewById(R.id.checkbox) ;//创建"是否私有"对应的CheckBox
                c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                    @Override
                    public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                        if(b){
                            is_private=true;
                        }
                        else is_private=false;

                    }
                });
                //预期时间
                EditText hours = (EditText) findViewById(R.id.time);//创建对应的"预期时间的"EditText
                //判断是否为空,不为空的话,把该字符串转化为数字(按理说不是数字应该也可以??)
                if (!hours.getText().toString().equals(""))
                estimated_hours = Integer.valueOf(hours.getText().toString());

                //描述
                EditText des = (EditText) findViewById(R.id.describeText);
                description = des.getText().toString();

                //把获得的数据进行封装
                JSONObject data=new JSONObject();
                JSONObject postdata=new JSONObject();
                try {
                    data.put("project_id", 1);//默认项目ID为1，后期修改为当前项目的ID！！！！！！！！勿忘！！
                    data.put("subject",subject);
                    data.put("priority_id",priority_id);
                    data.put("estimated_hours",estimated_hours);
                    data.put("status_id",status_id);
                    data.put("tracker_id",tracker_id);
                    data.put("is_private",is_private);
                    postdata.put("issue",data);
                    Log.d("Debug",postdata.toString());
                }catch (JSONException e){
                    e.printStackTrace();
                }

                String parameter="issues.json";
                String url = "http://teammanager.tk/" + parameter;

                //创建用于post的请求
                JsonObjectRequest jsonObjectRequest = new JsonObjectRequest(Request.Method.POST, url,
                        postdata,
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
                });
                //添加post请求到全局网络请求队列
                networkRequestController.getRequestQueue().add(jsonObjectRequest);

                Log.d("Debug",postdata.toString());
            Intent intent=new Intent(CreateTaskActivity.this,MyTaskActivity.class);
            startActivity(intent);
            }
        });
    }

    //点击选择文件按钮出现的Dialog
    @Override
    protected Dialog onCreateDialog(int id) {
        if (id == openfileDialogId) {
            Map<String, Integer> images = new HashMap<String, Integer>();
            // 下面几句设置各文件类型的图标， 需要你先把图标添加到资源文件夹
            images.put(OpenFileDialog.sRoot, R.drawable.category);   // 根目录图标
            images.put(OpenFileDialog.sParent, R.drawable.sign_out);    //返回上一层的图标
            images.put(OpenFileDialog.sFolder, R.drawable.folder);   //文件夹图标
            images.put("wav", R.drawable.billing);   //wav文件图标
            images.put(OpenFileDialog.sEmpty, R.drawable.category);
            Dialog dialog = OpenFileDialog.createDialog(id, this, "打开文件", new OpenFileDialog.CallbackBundle() {
                        @Override
                        public void callback(Bundle bundle) {
                            String filepath = bundle.getString("path");
                            setTitle(filepath); // 把文件路径显示在标题上
                        }
                    },
                    ".wav;",
                    images);
            return dialog;
        }
        return null;
    }
}
