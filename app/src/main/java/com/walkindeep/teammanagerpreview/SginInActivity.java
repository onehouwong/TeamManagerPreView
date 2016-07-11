package com.walkindeep.teammanagerpreview;

import android.content.Context;
import android.content.Intent;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import shem.com.materiallogin.MaterialLoginView;
import shem.com.materiallogin.MaterialLoginViewListener;

public class SginInActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sgin_in);

        final Context context = this;
        final MaterialLoginView login = (MaterialLoginView) findViewById(R.id.login);
        login.setListener(new MaterialLoginViewListener() {
            @Override
            public void onRegister(TextInputLayout registerUser, TextInputLayout registerPass, TextInputLayout registerPassRep) {
                //Handle register
            }

            @Override
            public void onLogin(TextInputLayout loginUser, TextInputLayout loginPass) {
                //Handle login
                User user = User.init(loginUser.getEditText().getText().toString(), loginPass.getEditText().getText().toString());
                if (user.authenticate()) {
                    SaveSharedPreference.setUserName(context, user.getUsername());
                    Intent intent = new Intent(context, MyTaskActivity.class);
                    startActivity(intent);
                }

            }
        });
    }
}
