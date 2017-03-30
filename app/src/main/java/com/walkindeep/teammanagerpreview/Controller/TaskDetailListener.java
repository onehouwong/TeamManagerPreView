package com.walkindeep.teammanagerpreview.Controller;

import android.app.Activity;
import android.content.Intent;
import android.util.Log;
import android.view.View;

import com.walkindeep.teammanagerpreview.UI.TaskDetailActivity;

/**
 * Created by OneHouWong on 2017/3/18.
 */

/**
 * 在MyTaskActivity中点击任务进入TaskDetailActivity的listener
 */
public class TaskDetailListener implements View.OnClickListener {

    private int position;
    private Activity activity;

    public TaskDetailListener(int pos, Activity act)
    {
        position = pos;
        activity = act;
    }
    @Override
    public void onClick(View view)
    {
        Intent intent = new Intent(activity, TaskDetailActivity.class);
        intent.putExtra("position", position); // 将任务的position传输给下一个activity
        activity.startActivity(intent);
    }
}
