package com.walkindeep.teammanagerpreview.UI;

import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import com.walkindeep.teammanagerpreview.DAO.AbstractDataQuery;
import com.walkindeep.teammanagerpreview.FindPassword;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;


/**
 * 用于登录的类
 */
public class LoginActivity extends AppCompatActivity {

    final Context context = this;
    Button loginButton;
    EditText userNameText;
    EditText passwordText;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        loginButton = (Button) findViewById(R.id.login_button);
        userNameText = (EditText) findViewById(R.id.input_id);
        passwordText = (EditText) findViewById(R.id.input_password);

        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String userName = userNameText.getText().toString();
                String password = passwordText.getText().toString();
                try {
                    login(userName, password);
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        });

        TextView register = (TextView) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        TextView forget = (TextView) findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPassword.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 用于登录的函数,利用了HTTP基本认证的方法
     *
     * @param userName 登录用户名
     * @param password 用户密码
     */
    public void login(final String userName, final String password) throws Exception {

        final Context context = this;
        /**
         * Handler用于处理线程传送过来的HTTP状态码并对UI界面进行操作
         */
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                int code = data.getInt("code");
                if (code == 200) {  // 账号密码正确的情况
                    User.init(userName, password);
                    /* 下面代码用于获取user的key值并记录在user类中 */
                    LoginDataQuery keyQuery = new LoginDataQuery();
                    keyQuery.getData("users/current.json", context, User.getUser());
                    // Toast.makeText(getBaseContext(), "登录成功" + User.getKey(), Toast.LENGTH_LONG).show();

                    /* 进入主界面 */
                    Intent intent = new Intent(LoginActivity.this, MyTaskActivity.class);
                    startActivity(intent);
                } else if (code == 401) { // 账号密码错误的情况
                    Toast.makeText(getBaseContext(), "登录失败，请验证用户名或密码", Toast.LENGTH_LONG).show();
                } else { // 未知错误?? 有待测试
                    Toast.makeText(getBaseContext(), "登录失败，未知错误", Toast.LENGTH_LONG).show();
                }
            }
        };

        /**
         * 线程用于异步进行HTTP基本认证，并向Handler传送返回的HTTP状态码
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String address = "http://walkindeep.tk/issues.xml";
                    URL url = new URL(address);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String author = "Basic " + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", author);
                    conn.connect();

                    int code = conn.getResponseCode(); /* 获取返回的状态码 200表示验证通过 401表示验证不通过 */
                    Message msg = new Message();
                    Bundle data = new Bundle();
                    data.putInt("code", code);
                    msg.setData(data);
                    handler.sendMessage(msg);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }).start();
    }

    /**
     * 用于处理登录界面与后台进行数据交互的类，继承了AbstractDataQuery。
     * 这里的类是基于登录成功的情况，从服务器后台得到对应的key值
     */
    private class LoginDataQuery extends AbstractDataQuery {
        protected void work(JSONObject userJSONObject) {
            try {
                JSONObject userInfo = userJSONObject.getJSONObject("user");
                String key = userInfo.getString("api_key");
                User.setKey(key);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    protected void parseXMLWithPull(String xmlData){

    }
    }
}
