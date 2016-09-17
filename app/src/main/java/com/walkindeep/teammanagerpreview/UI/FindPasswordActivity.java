package com.walkindeep.teammanagerpreview.UI;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;

import com.walkindeep.teammanagerpreview.R;

/**
 * Created by OneHouWong on 2016/7/22.
 */
public class FindPasswordActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_findpassword);

        Button submitButton = (Button) findViewById(R.id.submit_button);
        /**
         * 忘记密码的内部逻辑 暂未实现
         */
        submitButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }
}
