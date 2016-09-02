package com.walkindeep.teammanagerpreview;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.walkindeep.teammanagerpreview.DAO.AbstractDataQuery;
import com.walkindeep.teammanagerpreview.DAO.DataPost;
import com.walkindeep.teammanagerpreview.Project.User;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

/**
 * Created by Samsung on 2016/7/27.
 */
public class newProblem extends Activity{
    User user=User.getUser();
    DataPost dp=new DataPost();
    final int MAXTHEMELENGTH=20;
    final int MAXDESCLENGTH=140;
    int themeLength=MAXTHEMELENGTH;
    int descLength=MAXDESCLENGTH;
    Calendar c;

    private Button commitButton;
    private Spinner doneSpinner;
    private Spinner assignSpinner;
    private Spinner prioritySpinner;
    private Button starttimebutton;
    private Button endtimebutton;
    private Spinner statusSpinner;
    private Spinner trackerSpinner;
    private EditText themeEdit;
    private EditText descEdit;
    private TextView themeLengthShow;
    private TextView descLengthShow;
    private List<String> list = new ArrayList<String>();
    private  List<String> list2 = new ArrayList<String>();
    private  List<String> list3 = new ArrayList<String>();
    private  List<String> list4 = new ArrayList<String>();
    private  List<String> list5 = new ArrayList<String>();
    private ArrayAdapter<String> adapter;


    JSONObject jsonget=new JSONObject();
    JSONObject jsonpost=new JSONObject();
    AbstractDataQuery a=new AbstractDataQuery() {
        @Override
        protected void work(JSONObject userIssuesJSONObject) {
            jsonget=userIssuesJSONObject;
        }
    };

   @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_newproject);
        trackerSpinner = (Spinner) findViewById(R.id.spinner1);
        list.add("任务");
        list.add("功能");
        list.add("支持");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        trackerSpinner.setAdapter(adapter);
        trackerSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                /*将需要传递的数据put进jsonobject*/
                try {
                    jsonpost.put("trackerposition", position);
                    jsonpost.put("trackername", str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });




        /*themeEdit 实现*/
        themeEdit = (EditText) findViewById(R.id.themeedit);
        themeLengthShow = (TextView) findViewById(R.id.textView2);
        themeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                themeLengthShow.setText("还能输入" + themeLength + "个字");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((20 - s.length()) >= 0) {
                    themeLength = 20 - s.length();
                    themeLengthShow.setText("还能输入" + themeLength + "个字");
                } else {
                    Toast.makeText(getApplicationContext(), "最多输入" + MAXTHEMELENGTH + "个字符", Toast.LENGTH_SHORT).show();
                    themeLengthShow.setText("还能输入" + themeLength + "个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                themeLengthShow.setText("还能输入" + themeLength + "个字");
                Log.d(s.toString(), "theme content is" + s.toString());
                //打包jsonobject
                try {
                    jsonpost.put("theme", s.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });



    /*descedit实现*/
        descEdit = (EditText) findViewById(R.id.descedit);
        descLengthShow = (TextView) findViewById(R.id.textView3);
        themeEdit.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                themeLengthShow.setText("还能输入" + themeLength + "个字");
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if ((MAXDESCLENGTH - s.length()) >= 0) {
                    themeLength = MAXDESCLENGTH - s.length();
                    themeLengthShow.setText("还能输入" + descLength + "个字");
                } else {
                    Toast.makeText(getApplicationContext(), "最多输入" + MAXDESCLENGTH + "个字符", Toast.LENGTH_SHORT).show();
                    themeLengthShow.setText("还能输入" + descLength + "个字");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                descLengthShow.setText("还能输入" + descLength + "个字");
                Log.d(s.toString(), "desc content is" + s.toString());
                //打包jsonobject
                try {
                    jsonpost.put("desc", s.toString());


                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        });


        /*status实现*/
        statusSpinner = (Spinner) findViewById(R.id.spinner2);
        list2.add("新建");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list2);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                /*将需要传递的数据put进jsonobject*/
                try {
                    jsonpost.put("statusposition", position);
                    jsonpost.put("statusname", str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });



        /*startDate实现*/
        starttimebutton = (Button) findViewById(R.id.sbutton);
        starttimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                new DatePickerDialog(newProblem.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                //设置日历


                                try {
                                    jsonpost.put("startyear", year);
                                    jsonpost.put("startmouth", monthOfYear);
                                    jsonpost.put("startday", dayOfMonth);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        endtimebutton = (Button) findViewById(R.id.ebutton);
        endtimebutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                // TODO Auto-generated method stub
                new DatePickerDialog(newProblem.this,
                        new DatePickerDialog.OnDateSetListener() {

                            @Override
                            public void onDateSet(DatePicker view, int year, int monthOfYear,
                                                  int dayOfMonth) {
                                //设置日历


                                try {
                                    jsonpost.put("endyear", year);
                                    jsonpost.put("endmouth", monthOfYear);
                                    jsonpost.put("endday", dayOfMonth);
                                } catch (JSONException e) {
                                    e.printStackTrace();
                                }

                            }
                        }, c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH)).show();
            }
        });


        /*priority 实现*/
        prioritySpinner = (Spinner) findViewById(R.id.spinner3);
        list3.add("低");
        list3.add("普通");
        list3.add("高");
        list3.add("紧急");
        list3.add("立刻");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list3);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                /*将需要传递的数据put进jsonobject*/
                try {
                    jsonpost.put("priorityposition", position);
                    jsonpost.put("priorityname", str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*assign实现*/

        assignSpinner = (Spinner) findViewById(R.id.assign_spinner);
        list3.add("低");
        list3.add("普通");
        list3.add("高");
        list3.add("紧急");
        list3.add("立刻");
        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list4);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                /*将需要传递的数据put进jsonobject*/
                try {
                    jsonpost.put("assignposition", position);
                    jsonpost.put("assignname", str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*done实现*/

        doneSpinner = (Spinner) findViewById(R.id.done_spinner);
        list5.add("0%");
        list5.add("10%");
        list5.add("20%");
        list5.add("30%");
        list5.add("40%");
        list5.add("50%");
        list5.add("60%");
        list5.add("70%");
        list5.add("80%");
        list5.add("90%");
        list5.add("100%");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_spinner_item, list4);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        statusSpinner.setAdapter(adapter);
        statusSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                String str = parent.getItemAtPosition(position).toString();

                /*将需要传递的数据put进jsonobject*/
                try {
                    jsonpost.put("doneposition", position);
                    jsonpost.put("donename", str);
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });


        /*createButton实现*/
        commitButton = (Button) findViewById(R.id.createProject);
        commitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*!!!*/
            }
        });
    }

}

