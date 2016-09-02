package com.walkindeep.teammanagerpreview;

import android.app.Activity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

import com.walkindeep.teammanagerpreview.DAO.AbstractDataQuery;
import com.walkindeep.teammanagerpreview.DAO.DataPost;
import com.walkindeep.teammanagerpreview.Project.User;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Samsung on 2016/7/26.
 */
public class tryclass extends Activity {

    User user= User.getUser();
    DataPost dp=new DataPost();
    final int MAXTHEMELENGTH=20;
    final int MAXDESCLENGTH=140;
    int themeLength=MAXTHEMELENGTH;
    int descLength=MAXDESCLENGTH;



    private Spinner trackerSpinner;
    private EditText themeEdit;
    private TextView themeLengthShow;
    private TextView descLengthShow;
    private List<String> list = new ArrayList<String>();
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
        trackerSpinner = (Spinner) findViewById(R.id.spinner1);
        setContentView(R.layout.activity_newproject);
        list.add("任务");
        list.add("功能");
        list.add("支持");

    }
}
