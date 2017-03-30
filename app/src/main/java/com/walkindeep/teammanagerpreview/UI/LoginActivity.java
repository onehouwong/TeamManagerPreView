package com.walkindeep.teammanagerpreview.UI;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import android.util.Base64;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.walkindeep.teammanagerpreview.DAO.Constant;
import com.walkindeep.teammanagerpreview.DAO.NetworkRequestController;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;

import org.json.JSONException;
import org.json.JSONObject;

import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.Map;


/**
 * 用于登录的类
 */
public class LoginActivity extends AppCompatActivity {

    final Context context = this;
    Button loginButton; //登录按钮
    EditText userNameText; //用户名的EditText
    EditText passwordText; //密码的EditText

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        try {
            if(autoLogin()) // 先尝试进行自动登录
                return;
        }
        catch (Exception e){
            e.printStackTrace();
        }
        setContentView(R.layout.activity_login);

        //获取登陆界面的组件
        loginButton = (Button) findViewById(R.id.login_button);
        userNameText = (EditText) findViewById(R.id.input_id);
        passwordText = (EditText) findViewById(R.id.input_password);

        //登陆按钮的监听器
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

        //转入注册界面的textview的监听器
        TextView register = (TextView) findViewById(R.id.register_button);
        register.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        //转入忘记密码界面的textview的监听器
        TextView forget = (TextView) findViewById(R.id.forget);
        forget.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, FindPasswordActivity.class);
                startActivity(intent);
            }
        });
    }

    /**
     * 用于自动登录的函数，前提是已经设置好了sharedPreferences中的数据
     * @return 是否自动登录的boolean值
     * @throws Exception
     */
    private boolean autoLogin() throws Exception
    {
        SharedPreferences loginPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
        if(!loginPreferences.contains("name")) // sharedPreferences还没设置的情况，直接return false
            return false;
        String userName = loginPreferences.getString("name", ""); // 获取用户名
        String password = loginPreferences.getString("password", ""); // 获取密码
        login(userName, password); // 登录
        return true;
    }

    /**
     * 用于登录的函数,利用了HTTP基本认证的方法
     * @param userName 登录用户名
     * @param password 用户密码
     */
    public void login(final String userName, final String password) throws Exception {
        /**
         * Handler用于处理下面线程传送过来的HTTP状态码并对UI界面进行操作，在线程执行完毕后响应
         */
        final Handler handler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                Bundle data = msg.getData();
                int code = data.getInt("code");
                if (code == 200) {  // 账号密码正确的情况
                    User.init(userName, password);
                    // 创建一个login的sharedPreferences来保持登陆状态
                    SharedPreferences loginPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                    if(!loginPreferences.contains("name")) {
                        //Preferences中还没有写入用户名以及密码的时候，即第一次成功登录，此时需要先写入
                        SharedPreferences.Editor editor = loginPreferences.edit();
                        editor.putString("name", userName); // 写入用户名
                        editor.putString("password", password); // 保存密码
                        editor.apply();
                    }
                    //下面代码用于获取user的key值并记录在user类中

                    String url = Constant.WEBURL + "users/current.json";

                    final User user = User.getUser();

                    StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
                            new Response.Listener<String>() {
                                @Override
                                public void onResponse(String response) {
                                    JSONObject userIssuesJSONObject = null;
                                    try {
                                        userIssuesJSONObject = new JSONObject(response);

                                        try {
                                            //以下代码将登陆成功后获得的key和email值记录在User类中，在getData后自动执行
                                            JSONObject userInfo = userIssuesJSONObject.getJSONObject("user");
                                            String key = userInfo.getString("api_key");
                                            User.setKey(key);
                                            String email = "www";
                                            // 有些用户可能没有注册邮箱或者隐藏了邮箱，这时候为了防止返回邮箱时出错，设置一个默认值
                                            if(userInfo.has("mail"))
                                                email = userInfo.getString("mail");
                                            User.setMail(email);
                                            SharedPreferences loginPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                                            SharedPreferences.Editor editor = loginPreferences.edit();
                                            editor.putString("api_key", key); // 写入key
                                            editor.putString("email", email); // 写入email
                                            editor.apply();
                                            // 要等到request结束后方可结束activity，为了线程对user的同步
                                            // 原因是如果不等request结束，navigationActivity会找不到email
                                            Intent intent = new Intent(LoginActivity.this, MyTaskActivity.class);
                                            startActivity(intent);
                                            finish(); // 结束LoginActivity

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
                    // Add the request to the RequestQueue.
                    NetworkRequestController networkRequestController = NetworkRequestController.getInstance();
                    networkRequestController.getRequestQueue().add(stringRequest);

                    // Toast.makeText(getBaseContext(), "登录成功" + User.getKey(), Toast.LENGTH_LONG).show();

                    // 进入主界面
                    /*
                    Intent intent = new Intent(LoginActivity.this, MyTaskActivity.class);
                    startActivity(intent);
                    finish(); // 结束LoginActivity
                    */
                } else if (code == 401) { // 账号密码错误的情况
                    Toast.makeText(getBaseContext(), "登录失败，请验证用户名或密码", Toast.LENGTH_LONG).show();
                } else { // 未知错误?? 有待测试
                    Toast.makeText(getBaseContext(), "登录失败，未知错误", Toast.LENGTH_LONG).show();
                }
            }
        };

        /**
         * 线程用于异步进行HTTP基本认证，并向Handler传送返回的HTTP状态码
         * 科普一下HTTP基本验证，它就是把“用户名：密码”的形式加密放在请求头的authorization中发送，然后获得回复的状态码
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    //以下为HTTP基本验证的流程
                    String address = "http://walkindeep.tk/issues.xml"; //用于验证的url前缀（这里以issues,xml为例，可以更改）
                    URL url = new URL(address);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    String author = "Basic " + Base64.encodeToString((userName + ":" + password).getBytes(), Base64.DEFAULT);
                    conn.setRequestProperty("Authorization", author);
                    conn.connect();

                    int code = conn.getResponseCode();  //获取返回的状态码 200表示验证通过 401表示验证不通过
                    //以下代码将code包装到message中传递给handler
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

}
