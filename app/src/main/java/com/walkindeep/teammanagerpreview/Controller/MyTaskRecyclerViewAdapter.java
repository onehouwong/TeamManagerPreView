package com.walkindeep.teammanagerpreview.Controller;

import android.app.Activity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walkindeep.teammanagerpreview.R;

import java.util.ArrayList;

/**
 * Created by OneHouWong on 2016/10/22.
 */

/**
 * 用于处理MyTask主界面中所有任务界面布局的adapter，继承了RecyclerView的Adapter
 */
public class MyTaskRecyclerViewAdapter extends RecyclerView.Adapter<MyTaskRecyclerViewAdapter.ViewHolder>{

    private ArrayList<String> issueName; // 任务名
    private ArrayList<String> issueDesc; // 任务描述
    private Activity activity; // 接受来自调用者的activity


    /**
     * MyTaskrecyclerViewAdapter构造函数
     * @param name 任务名
     * @param desc 任务描述
     */
    public MyTaskRecyclerViewAdapter(ArrayList<String> name, ArrayList<String> desc, Activity act) {
        issueName = name;
        issueDesc = desc;
        activity = act;
    }

    /**
     * 实例化view
     */
    @Override
    public MyTaskRecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.tasks_item, parent, false);
        return new ViewHolder(v);
    }

    /**
     * 绑定view中的内容
     */
    @Override
    public void onBindViewHolder(final MyTaskRecyclerViewAdapter.ViewHolder holder, int position) {
        TextView issueNameText = (TextView) holder.myView.findViewById(R.id.issue_name);
        TextView issueDescText = (TextView) holder.myView.findViewById(R.id.issue_desc);
        holder.set(position, issueName.get(position), issueDesc.get(position));
        issueNameText.setText(issueName.get(position));
        issueDescText.setText(issueDesc.get(position));
    }

    @Override
    public int getItemCount() {
        return issueName.size();
    }

    /**
     * viewHolder
     */
    public class ViewHolder extends RecyclerView.ViewHolder {
        public View myView;
        private String issueName;
        private String issueDesc;
        private int position;

        /**
         * 设置viewHolder参数，为后续设置listener，进入详细任务界面做准备
         * @param pos 位置
         * @param name 任务名
         * @param desc 任务描述
         */
        public void set(int pos, String name, String desc)
        {   position = pos;
            issueName = name;
            issueDesc = desc;
            myView.setOnClickListener(new TaskDetailListener(position, activity));
        }

        public ViewHolder(View v) {
            super(v);
            myView = v;
        }
    }
}

