package com.walkindeep.teammanagerpreview;

import android.app.AlertDialog;
import android.app.DatePickerDialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.walkindeep.teammanagerpreview.DAO.AbstractDataQuery;
import com.walkindeep.teammanagerpreview.DAO.DataPost;
import com.walkindeep.teammanagerpreview.Project.Issue;
import com.walkindeep.teammanagerpreview.Project.Project;
import com.walkindeep.teammanagerpreview.Project.User;

import org.json.*;






import java.util.Calendar;

public class Createmission extends AppCompatActivity {
    private TextView managerbutton;
    private TextView starttimebutton;
    private TextView prioritybutton;
    private TextView endtimebutton;
    private TextView save;
    private TextView missionedittext;
    private EditText missiontheme;
    private EditText missioncontent;
    private TextView missionnumber;




    User user1 = User.getUser();
    AbstractDataQuery ab;
    DataPost dp;
    Issue issue1;
    Calendar c;
    JSONObject json;
    //get JSONObject
    class getObject extends AbstractDataQuery{
        private JSONObject son=null;
        protected void work(JSONObject sonObject){
            son=sonObject;
        }
        public JSONObject getSon(){return son;}
    }



    //设置监听按钮，用于传入后台数据
    class ButtonOnClick implements DialogInterface.OnClickListener

    {
        private int index; // 表示选项的索引
        private String parmeter;

        public ButtonOnClick(int index,String s)

        {
            this.index = index;
            this.parmeter=s;
        }

        @Override

        public void onClick(DialogInterface dialog, int which)

        {
            // which表示单击的按钮索引，所有的选项索引都是大于0，按钮索引都是小于0的。

            if (which >= 0)

            {
                //如果单击的是列表项，将当前列表项的索引保存在index中。
                //如果想单击列表项后关闭对话框，可在此处调用dialog.cancel()
                //或是用dialog.dismiss()方法。
                index = which;
            } else
            {
                //用户单击的是【确定】按钮
                if (which == DialogInterface.BUTTON_POSITIVE)
                {
                    //显示用户选择的是第几个列表项。
                    JSONObject jsonObjectp=new JSONObject();
                    try {
                        jsonObjectp.put(parmeter,index);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }
                    dp.post(parmeter,Createmission.this,user1,jsonObjectp);
                }

                //用户单击的是【取消】按钮

                else if (which == DialogInterface.BUTTON_NEGATIVE)
                {
                    dialog.dismiss();
                }
            }
        }
    }

    /**
     * ATTENTION: This was auto-generated to implement the App Indexing API.
     * See https://g.co/AppIndexing/AndroidStudio for more information.
     */

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_createmission);
        c = Calendar.getInstance();


        //mission content
        missionedittext = (TextView) findViewById(R.id.missionEditText);
        missionedittext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                View missionDialogView = (LinearLayout) getLayoutInflater().inflate(R.layout.missioncontent, null);
                AlertDialog.Builder misContent = new AlertDialog.Builder(Createmission.this);
                missiontheme = (EditText) missionDialogView.findViewById(R.id.edit_missionTheme);
                missioncontent = (EditText) missionDialogView.findViewById(R.id.edit_missionContent);
                missionnumber = (TextView) missionDialogView.findViewById(R.id.missionNumber);
                missionnumber.setText("任务编号：" );


                //更新任务描述中的内容
                /*
                if(user1中任务描述存在）
                导入到任务描述中
                else
                返回为无内容
                */
                misContent.setTitle("任务描述");
                misContent.setView(missionDialogView);
                misContent.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
// TODO Auto-generated method stub
                        //将新建任务的描述传输到后端
                        JSONObject jasonObjectm = new JSONObject();
                        try {
                            jasonObjectm.put("subject",missiontheme.getText().toString());
                            jasonObjectm.put("description",missioncontent.getText().toString());
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dp.post("subject"/*???????*/, Createmission.this, user1, jasonObjectm);

                    }
                }).setNegativeButton("取消", null);
                misContent.create().show();
            }


        });

//managers choice

        managerbutton = (TextView) findViewById(R.id.managerButton);

        //~~~





        managerbutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                AlertDialog.Builder dlg = new AlertDialog.Builder(Createmission.this);
                dlg.setTitle("ManagerList");
                JSONObject jsonObjectmng=new JSONObject();

                //提取manager中的name列表

                getObject gO=new getObject();
                gO.getData("Assigned",Createmission.this,user1);
                jsonObjectmng=gO.getSon();
                final String[] managers ={"name1","name2","name3"} ;
                JSONArray managerJSONArray=null;
                try {
                    managerJSONArray = jsonObjectmng.getJSONArray("Assigned");
                } catch (JSONException e) {
                    e.printStackTrace();
                }
                //change managers!!!
                for(int i=0;i<managerJSONArray.length();i++)
                    try {
                        managers[i]= managerJSONArray.getJSONObject(i).getString("Assigned");
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }


                final boolean []checkedItems = new boolean [managers.length ] ;
                for(int i=0;i<managers.length;i++)

                    //原本应该调用json传过来的数据

                    checkedItems[i]=false;

                dlg.setMultiChoiceItems(managers, checkedItems, new DialogInterface.OnMultiChoiceClickListener() {
                    StringBuffer managername = new StringBuffer(100);

                    @Override
                    public void onClick(DialogInterface dialog, int which, boolean isChecked) {
                        if (isChecked) {
                            //将所选的manager记录下来
                            checkedItems[which]=true;
                        }

                    }
                });
                dlg.setPositiveButton("确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //添加java代码
                        //内容：将managername中的内容写到后台数据库中，记录下来。
                        JSONObject jsonObject1=new JSONObject();
                        try {
                            jsonObject1.put("assignedcheck",checkedItems);//s为选中的字符
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                        dp.post("assignedcheck",Createmission.this,user1,jsonObject1);
                    }
                });
                dlg.setNegativeButton("取消", null);
                dlg.show();
            }
        });

        starttimebutton = (TextView) findViewById(R.id.startTimeButton);
        starttimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                new DatePickerDialog(Createmission.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                //设置日历
                                JSONObject jsonobjectstatime=new JSONObject();

                                try {
                                    jsonobjectstatime.put("year",year);
                                    jsonobjectstatime.put("mouth",monthOfYear);
                                    jsonobjectstatime.put("day",dayOfMonth);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }
                                dp.post("start_data",Createmission.this,user1,jsonobjectstatime);
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        endtimebutton = (TextView) findViewById(R.id.endTimeButton);
        endtimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                new DatePickerDialog(Createmission.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                //设置日历
                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });




        //优先级选择
        prioritybutton = (TextView) findViewById(R.id.PriorityButton);
        prioritybutton.setOnClickListener(new View.OnClickListener() {
            final ButtonOnClick buttonOnClick=new ButtonOnClick(1,"priority");

            @Override
            public void onClick(View v) {
                //add action

                AlertDialog.Builder builderpriority = new AlertDialog.Builder(Createmission.this);

                builderpriority.setTitle("请选择")
                        .setPositiveButton("确定", buttonOnClick)
                        .setNegativeButton("取消",buttonOnClick)
                        .setIcon(android.R.drawable.ic_dialog_info)
                        .setSingleChoiceItems(new String[]{"1级", "2级", "3级", "4级（紧急）"}, 0,
                                buttonOnClick)

                        .show();
            }
        });

        // ATTENTION: This was auto-generated to implement the App Indexing API.
        // See https://g.co/AppIndexing/AndroidStudio for more information.

    }

}

