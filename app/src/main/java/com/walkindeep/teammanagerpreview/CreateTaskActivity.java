package com.walkindeep.teammanagerpreview;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
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

import com.walkindeep.teammanagerpreview.DAO.AbstrractDataQueryWithXML;
import com.walkindeep.teammanagerpreview.DAO.DataPost;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.UI.MyTaskActivity;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserFactory;

/**
 * Created by lenovo on 2016/7/31.
 */
public class CreateTaskActivity extends AppCompatActivity {
    static private int openfileDialogId = 0;
    private Spinner assignSpinner;
    private List<String> dataAssignSpinner;//动态加载到 assignSpinner的数组
    private ArrayAdapter<String> assignAdapter;//assignSpinner适配器
    private Context mContext;

    int project_id;
    int tracker_id;
    int status_id;
    int priority_id;
    String description;
    String subject;
    int estimated_hours;
    boolean is_private;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
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

        assignSpinner = (Spinner) findViewById(R.id.assignSpinner);
        dataAssignSpinner = new ArrayList<String>();

        //添加数据
        dataAssignSpinner.add("");
        dataAssignSpinner.add("<<我>>");

        DataQuery query = new DataQuery();
        query.getData("projects/1/memberships.xml", this, User.getUser());



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
                tracker_id=i+1;
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
                status_id=i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //优先级spinner
        final Spinner priority=(Spinner)findViewById(R.id.prioritySpinner);
        priority.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int i, long l) {
                priority_id=i+1;
            }

            @Override
            public void onNothingSelected(AdapterView<?> adapterView) {

            }
        });

        //百分比spinner
        Spinner finish=(Spinner)findViewById(R.id.followSpinner);
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
                subject = theme.getText().toString();

                CheckBox c=(CheckBox)findViewById(R.id.checkbox) ;
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
                EditText hours = (EditText) findViewById(R.id.time);
                if (!hours.getText().toString().equals(""))
                estimated_hours = Integer.valueOf(hours.getText().toString());

                //描述
                EditText des = (EditText) findViewById(R.id.describeText);
                description = des.getText().toString();


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
                DataPost post=new DataPost();
                post.post("issues.json",mContext, User.getUser(),postdata);
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


    private class DataQuery extends AbstrractDataQueryWithXML {
        protected void work(JSONObject userIssuesJSONObject) {
        }
        protected   void parseXMLWithPull(String xmlData){
            try {
                XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
                XmlPullParser xmlPullParser = factory.newPullParser();
                xmlPullParser.setInput(new StringReader(xmlData));
                int eventType =xmlPullParser.getEventType();
                String id="";
                String project ="";
                String user ="";
                List<String> role =new ArrayList<String>();
                while(eventType!=XmlPullParser.END_DOCUMENT){
                    String nodeName =xmlPullParser.getName();

                    switch (eventType){
                        //开始解析某个结点
                        case XmlPullParser.START_TAG:{
                            if("id".equals(nodeName)){
                                id=xmlPullParser.nextText();
                            }
                            else if("user".equals(nodeName)){
                               String temp= xmlPullParser.getAttributeValue(null,"name");
                               dataAssignSpinner.add(temp);
                                Log.d("Debug",temp);
                            }
                            break;
                        }
                        case XmlPullParser.END_TAG:{

                            break;
                        }
                    }
                    eventType=xmlPullParser.next();
                }
            }
            catch (Exception e){
                e.printStackTrace();
            }
        }
    }
}
