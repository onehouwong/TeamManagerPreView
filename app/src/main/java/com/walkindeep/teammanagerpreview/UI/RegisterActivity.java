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
    Button registerButton;
    EditText idText, passwordText, repasswordText, lastNameText, firstNameText, emailText;
    TextView login;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

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
     * 用于注册的函数
     */
    public boolean signUp() {
        Log.d(TAG, "Sign up");

        if (!validate()) {
            onSignUpFailed("注册失败，请检查以上信息");
            return false;
        }
        registerButton.setEnabled(false);

        /* 创建一个发送给后台的JSONObject，内包含注册用户的信息 */
        JSONObject userJSONObject = new JSONObject();
        String id, email, password, firstName, lastName;
        try {
            id = idText.getText().toString();
            email = emailText.getText().toString();
            password = passwordText.getText().toString();
            firstName = firstNameText.getText().toString();
            lastName = lastNameText.getText().toString();
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

        /* 像后台发送JSON并响应处理 */
        User.init("guojiahao", "teammanager"); // 必须借助管理员账号才能注册，这样是否会存在安全性问题，待解决
        RegisterDataPost registerDataPost = new RegisterDataPost();
        registerDataPost.post("users.json", context, User.getUser(), userJSONObject);

        return true;
    }


    /**
     * 表示注册成功的场景
     */
    public void onSignUpSuccess() {
        registerButton.setEnabled(true);
        setResult(RESULT_OK, null);
        finish();
    }

    /**
     * 表示注册失败的场景
     */
    public void onSignUpFailed(String message) {
        Toast.makeText(getBaseContext(), message, Toast.LENGTH_LONG).show();
        registerButton.setEnabled(true);
    }

    /**
     * 判断注册是否成功的函数
     *
     * @return 注册是否成功的boolean值
     */
    public boolean validate() {
        boolean valid = true;

        String id = idText.getText().toString();
        String email = emailText.getText().toString();
        String password = passwordText.getText().toString();
        String repassword = repasswordText.getText().toString();
        String firstName = firstNameText.getText().toString();
        String lastName = lastNameText.getText().toString();

        if (id.isEmpty()) {
            idText.setError("登录名不能为空");
            valid = false;
        } else {
            idText.setError(null);
        }

        if (email.isEmpty() || !android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            emailText.setError("请输入合法的电子邮件地址");
            valid = false;
        } else {
            emailText.setError(null);
        }

        if (password.isEmpty()) {
            passwordText.setError("密码不能为空");
            valid = false;
        } else if (password.length() < 8) {
            passwordText.setError("密码长度至少为8位");
            valid = false;
        } else {
            passwordText.setError(null);
        }

        if (repassword.isEmpty()) {
            repasswordText.setError("重复密码不能为空");
            valid = false;
        } else if (!repassword.equals(password)) {
            repasswordText.setError("两次输入密码不一致");
            valid = false;
        } else {
            repasswordText.setError(null);
        }

        if (firstName.isEmpty()) {
            firstNameText.setError("名字不能为空");
            valid = false;
        } else {
            firstNameText.setError(null);
        }

        if (lastName.isEmpty()) {
            lastNameText.setError("姓氏不能为空");
            valid = false;
        } else {
            lastNameText.setError(null);
        }

        return valid;
    }


    private class RegisterDataPost extends DataPost {

        @Override
        protected void responseHandle(JSONObject response) {

            /* 定义过度信息 将在成功注册后展示 */
            final ProgressDialog progressDialog = new ProgressDialog(RegisterActivity.this,
                    R.style.AppTheme);
            progressDialog.setIndeterminate(true);
            progressDialog.setMessage("正在创建账号...");
            progressDialog.show();


            // TODO: Implement your own signup logic here.

            new android.os.Handler().postDelayed(
                    new Runnable() {
                        public void run() {
                            // On complete call either onSignUpSuccess or onSignUpFailed
                            // depending on success
                            onSignUpSuccess();
                            progressDialog.dismiss();
                        }
                    }, 3000);

        }

        @Override
        protected void errorResponseHandle(VolleyError error) {
            error.printStackTrace();
            /* 这里有个问题 貌似无法获取错误信息，而在输入的是否我们已经提前对每个字段进行了检查
             * 所以这里应该只有唯一一个问题了，那就是账号重复注册，但还需要通过测试debug */
            onSignUpFailed("注册失败，" + "账号已被注册");
        }
    }
}
