package com.walkindeep.teammanagerpreview.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;

/**
 * Created by OneHouWong on 2016/9/19.
 */
public class SettingsActivity extends AppCompatActivity {
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);
        init();
    }

    /**
     * 初始化所有组件的函数
     */
    private void init()
    {
        Button logout_Button = (Button)findViewById(R.id.logout_button);

        logout_Button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 创建询问是否退出的对话框
                AlertDialog alert = new AlertDialog.Builder(SettingsActivity.this).create();
                alert.setTitle("注销账号");
                alert.setMessage("确定退出登录？");
                alert.setButton(DialogInterface.BUTTON_NEGATIVE, "取消", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                    }
                });

                alert.setButton(DialogInterface.BUTTON_POSITIVE, "确定", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        User.logout(); // 注销账号
                        // 清除SharedPreferences中的记录
                        SharedPreferences loginPreferences = getSharedPreferences("login", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = loginPreferences.edit();
                        editor.clear();
                        editor.apply();
                        Intent intent = new Intent(SettingsActivity.this, LoginActivity.class); //转入登录界面
                        // 清除Activity栈中的全部内容
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish(); // 退出SettingsActivity
                    }
                });
                alert.show();
            }
        });
    }
}
