package com.walkindeep.teammanagerpreview.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.media.Image;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.TextView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;
import com.walkindeep.teammanagerpreview.Project.Issue;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;
import com.walkindeep.teammanagerpreview.UserAvatarGetter;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.List;

/**
 * Created by OneHouWong on 2017/3/18.
 */

public class TaskDetailActivity extends AppCompatActivity {

    private int position;
    private String name, desc, status, priority, start_date, due_date, done_ratio, author;
    private Bitmap bitmap;
    @Override
    protected void onCreate(Bundle savedInstance)
    {
        super.onCreate(savedInstance);
        setContentView(R.layout.activity_taskdetail);
        Intent intent = getIntent();
        position = intent.getIntExtra("position", 0);
        getIssue();
        Log.i("TaskDetailActivity", name+desc+status+priority+start_date+due_date+done_ratio+author);

    }

    /**
     * 获取并更新当前issue的信息
     */
    private void getIssue()
    {
        User user = User.getUser();
        Issue issue = user.getToDoIssueList().get(position);
        name = issue.getSubject();
        desc = issue.getDescription();
        status = issue.getStatus_name();
        priority = issue.getPriority_name();
        start_date = issue.getStart_date();
        due_date = issue.getDue_date();
        done_ratio = issue.getDone_ratio();
        author = issue.getAuthor_name();

        try {
            setImage();
        } catch (Exception e){
            e.printStackTrace();
        }

        TextView task_title = (TextView)findViewById(R.id.task_title);
        task_title.setText(name);
        TextView add_info = (TextView)findViewById(R.id.add_info);
        add_info.setText("由" + author + "于" + start_date + "添加");
        TextView status_text = (TextView)findViewById(R.id.status);
        status_text.setText(status);
        TextView start_date_text = (TextView)findViewById(R.id.start_date);
        start_date_text.setText(start_date);
        TextView priority_text = (TextView)findViewById(R.id.priority);
        priority_text.setText(priority);
        TextView due_date_text = (TextView)findViewById(R.id.due_date);
        due_date_text.setText(due_date);
        TextView appoint = (TextView)findViewById(R.id.appoint_to);
        appoint.setText(User.getUsername());
        TextView done_ratio_text = (TextView)findViewById(R.id.done_ratio);
        done_ratio_text.setText(done_ratio + "%");
        ProgressBar progressBar = (ProgressBar)findViewById(R.id.done_ratio_bar);
        progressBar.setProgress(Integer.parseInt(done_ratio));
        TextView descrpition = (TextView)findViewById(R.id.description);
        descrpition.setText(desc);

    }
    /**
     * 用于从gavatar上下载用户图像并设置到导航栏上，采用异步下载的方法
     */

    private void setImage() throws Exception
    {
        final Handler handler = new Handler(){
            @Override
            public void handleMessage(Message msg){
                super.handleMessage(msg);
                ImageView view=(ImageView)findViewById(R.id.tracker_portrait);
                view.setImageBitmap(bitmap);
            }
        };

        new Thread(new Runnable() {
            @Override
            public void run() {
                //这里下载数据
                try{
                    URL url = new URL(UserAvatarGetter.getAvatarURL(User.getUser()));
                    HttpURLConnection conn  = (HttpURLConnection)url.openConnection();
                    conn.setDoInput(true);
                    conn.connect();
                    InputStream inputStream=conn.getInputStream();
                    bitmap = BitmapFactory.decodeStream(inputStream);
                    Message msg=new Message();
                    msg.what=1;
                    handler.sendMessage(msg);
                } catch (MalformedURLException e1) {
                    e1.printStackTrace();
                } catch (IOException e) {
                    // TODO Auto-generated catch block
                    e.printStackTrace();
                }
            }
        }).start();
    }
}
