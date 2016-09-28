package com.walkindeep.teammanagerpreview.Controller;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.walkindeep.teammanagerpreview.R;

import java.util.ArrayList;


/**
 * Created by Administrator on 2016/9/24.
 */

public class RecyclerViewAdapter extends RecyclerView.Adapter<RecyclerViewAdapter.ViewHolder>
{

    private ArrayList<String> projectsName;
    private ArrayList<String> projectsDesc;


    /**viewHolder*/
     public class ViewHolder extends RecyclerView.ViewHolder {
        public View myView;
        public ViewHolder(View v) {
        super(v);
        myView = v;
    }
    }

    /**recyclerViewAdapter构造函数*/
    public RecyclerViewAdapter(ArrayList<String> myProjectName, ArrayList<String>myProjectDesc) {
        projectsName = myProjectName;
        projectsDesc=myProjectDesc;
    }

    /**实例化view*/
    @Override
    public RecyclerViewAdapter.ViewHolder onCreateViewHolder(ViewGroup parent,final int viewType) {
        View v = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.projects_item, parent, false);
        return new ViewHolder(v);
    }

    /**绑定view中的内容*/
    @Override
    public void onBindViewHolder(final RecyclerViewAdapter.ViewHolder holder,  int position) {
       TextView projectName=(TextView) holder.myView.findViewById(R.id.projects_name);
        TextView projectDescri=(TextView)holder.myView.findViewById(R.id.projects_descri);
        projectName.setText(projectsName.get(position));
        projectDescri.setText(projectsDesc.get(position));
    }

    @Override
    public int getItemCount() {
        return projectsName.size();
    }



}
