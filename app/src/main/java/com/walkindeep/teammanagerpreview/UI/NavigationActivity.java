package com.walkindeep.teammanagerpreview.UI;

import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ImageView;
import android.widget.TextView;

import com.walkindeep.teammanagerpreview.Project.ProjectList;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;
import com.walkindeep.teammanagerpreview.UserAvatarGetter;

import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;


public class NavigationActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    protected DrawerLayout drawer;
    private Bitmap bitmap;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navigation);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*dont need later delete*/
//        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
//        fab.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
//                        .setAction("Action", null).show();
//            }
//        });

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Log.i("NavigationActivity", "onCreate.");
        try{
            setImage(); // 设置导航栏上的用户头像
        }
        catch (Exception e){
            e.printStackTrace();
        }

//        //notification
//        NotificationCompat.Builder mBuilder =
//                new NotificationCompat.Builder(this)
//                        .setSmallIcon(R.drawable.alarm)
//                        .setContentTitle("任务提醒")
//                        .setContentText("BOSS提醒您及时汇报任务进度");
//
//        // Sets an ID for the notification
//        int mNotificationId = 001;
//// Gets an instance of the NotificationManager service
//        NotificationManager mNotifyMgr =
//                (NotificationManager) getSystemService(NOTIFICATION_SERVICE);
//// Builds the notification and issues it.
//        mNotifyMgr.notify(mNotificationId, mBuilder.build());
    }

    @Override
    public void onBackPressed() {
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.navigation, menu);
        // 设置导航栏上的用户名
        TextView nav_name_text = (TextView)findViewById(R.id.nav_name);
        nav_name_text.setText(User.getUsername());
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        //handle the projectsList action
        if(id==R.id.nav_project) {
                Intent intent = new Intent(this, ProjectList.class);
                startActivity(intent);


        }else if (id == R.id.nav_alarm) {
            // Handle the camera action
        } else if (id == R.id.nav_task) {
            if (this instanceof MyTaskActivity)
                ;
            else {
                Intent intent = new Intent(this, MyTaskActivity.class);
                startActivity(intent);
            }

        } else if (id == R.id.nav_calendar) {

        } else if (id == R.id.nav_contact) {
//            Intent intent = new Intent(this, Activityest.class);
//            startActivity(intent);
//

        } else if (id == R.id.nav_file) {

        } else if (id == R.id.nav_performance) {
            Intent intent = new Intent(this, PerferanceActivity.class);
            startActivity(intent);

        } else if (id == R.id.nav_settings) {
            Intent intent = new Intent(this, SettingsActivity.class);
            startActivity(intent);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
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
                ImageView view=(ImageView)findViewById(R.id.nav_imageView);
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
