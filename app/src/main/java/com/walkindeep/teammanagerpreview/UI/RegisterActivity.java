package com.walkindeep.teammanagerpreview.UI;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.walkindeep.teammanagerpreview.DAO.DataPost;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;

import org.json.JSONException;
import org.json.JSONObject;

/**
 * Created by OneHouWong on 2016/7/22.
 */
public class RegisterActivity extends AppCompatActivity {

    private static final String TAG = "RegisterActivity";
    final Context context = this;
    Button registerButton; //注册按钮
    EditText idText, passwordText, repasswordText, lastNameText, firstNameText, emailText; //用户输入信息的TextView
    TextView login; //转入登陆界面的TextView

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        //初始化UI组件
        idText = (EditText) findViewById(R.id.input_id);
        passwordText = (EditText) findViewById(R.id.input_password);
        repasswordText = (EditText) findViewById(R.id.input_repassword);
        lastNameText = (EditText) findViewById(R.id.input_lastName);
        firstNameText = (EditText) findViewById(R.id.input_firstName);
        emailText = (EditText) findViewById(R.id.input_email);
        registerButton = (Button) findViewById(R.id.register_button);
        login = (TextView) findViewById(R.id.login_button);

        /**
         * 定义注册按钮的监听器
         */
        registerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                signUp();
            }
        });

        /**
         * 定义跳转到注册页面的TextView的监听器
         */
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    /**
     * 注册的主函数
     */
    public boolean signUp() {
        Log.d(TAG, "Sign up");

        // 先对用户输入的用户名、密码、重复密码、姓名、email等信息进行验证，如果不符合要求修改
        if (!validate()) {
            onSignUpFailed("注册失败，请检查以上信息");
            return false;
        }
        registerButton.setEnabled(false); //点击注册按钮到注册完成的时间里，不允许用户再次点击按钮

        /* 创建一个发送给后台的JSONObject，内包含注册用户的信息 */
        JSONObject userJSONObject = new JSONObject();
        String id, email, password, firstName, lastName;
        try {
            // 从UI组件中获取用户注册信息
            id = idText.getText().toString();
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            firstName = firstNameText.getText().toString();
            lastName = lastNameText.getText().toString();

            // 将用户信息包装到Json文件中，即userJSONObject
            JSONObject userInfo = new JSONObject();
            userInfo.put("login", id);
            userInfo.put("firstname", firstName);
            userInfo.put("lastname", lastName);
            userInfo.put("mail", email);
            userInfo.put("password", password);
            userJSONObject.put("user", userInfo);
        } catch (JSONException e) {
            e.printStackTrace();
        }

        //向后台发送JSON并响应处理
        User.init("guojiahao", "teammanager"); // 必须借助管理员账号才能注册，这样是否会存在安全性问题，待解决
        // 初始化一个RegisterDataPost对象，并使用其向后台发送json文件，发送时使用管理员身份
        RegisterDataPost registerDataPost = new RegisterDataPost();
        registerDataPost.post("users.json", context, User.getUser(), userJSONObject);
        return true;
    }


    /**
     * 表示注册成功的场景，用于处理界面信息以及活动转换
     */
    public void onSignUpSuccess() {
        registerButton.setEnabled(true); // 重新允许用户点击按钮
        setResult(RESULT_OK, null);
        finish(); //退出Register活动，返回Login活动
    }

    /**
     * 表示注册失败的场景
     */
    public void onSignUpFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show(); //显示注册失败的消息
        registerButton.setEnabled(true); // 重新允许用户点击按钮
    }

    /**
     * 用于检验用户输入信息是否都合法的函数
     * @return 注册是否成功的boolean值
     */
    public boolean validate() {
        boolean valid = true; //函数最终返回值，先初始化为true，中途有一项不符合则置为false

        String id = idText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String repassword = repasswordText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();

        //登录名检查，不能为空
        if (id.isEmpty()) {
            idText.setError("登录名不能为空");
            valid = false;
        } else {
            idText.setError(null);
        }

        //email检查，不能为空，且格式需要符合电子邮件格式
        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("请输入合法的电子邮件地址");
            valid = false;
        } else {
            emailText.setError(null);
        }

        //密码检查，不能为空，且长度要小于8
        if (password.isEmpty()) {
            passwordText.setError("密码不能为空");
            valid = false;
        } else if (password.length() < 8) {
            passwordText.setError("密码长度至少为8位");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        //重复密码检查，不能为空，且需要与前面输入的密码一致
        if (repassword.isEmpty()) {
            repasswordText.setError("重复密码不能为空");
            valid = false;
        } else if (!repassword.equals(password)) {
            repasswordText.setError("两次输入密码不一致");
            valid = false;
        } else {
            repasswordText.setError(null);
        }

        //名字检查，不能为空
        if (firstName.isEmpty()) {
            firstNameText.setError("名字不能为空");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        //姓氏检查，不能为空
        if (lastName.isEmpty()) {
            lastNameText.setError("姓氏不能为空");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        return valid;
    }

    /**
     * 继承DataPost类，针对register活动，用于向后台发送json文件
     */
    private class RegisterDataPost extends DataPost {

        /**
         * 处理post函数向后台发送json文件后返回的response，表示注册成功
         * @param response 后台接受json文件后返回的数据结构
         */
        @Override
        protected void responseHandle(JSONObject response) {

            /* 定义过度信息 将在成功注册后展示 */
            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("正在创建账号...");
            progressDialog.show();

            // Handler类，3000ms后会执行run里面的函数内容
            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            onSignUpSuccess(); //调用注册成功的函数，处理一些界面信息即转场
                            progressDialog.dismiss(); //过度信息将在3000ms后关闭
                        }
                    }, 3000);

        }

        /**
         * 用于处理注册失败的情况
         * @param error 后台接收json文件后返回的一个错误信息
         */
        @Override
        protected void errorResponseHandle(VolleyError error) {
            error.printStackTrace();
            /* 这里有个问题 貌似无法获取错误信息，而在输入的是否我们已经提前对每个字段进行了检查
             * 所以这里应该只有唯一一个问题了，那就是账号重复注册，但还需要通过测试debug */
            onSignUpFailed("注册失败，" + "账号已被注册");
        }
    }
}
