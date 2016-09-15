package com.walkindeep.teammanagerpreview.UI;

import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;

import com.gigamole.library.ArcProgressStackView;
import com.walkindeep.teammanagerpreview.R;

import java.util.ArrayList;

/**
 * 没用的。
 */
public class PerferanceActivity extends NavigationActivity {
    public final static int MODEL_COUNT = 4;
    // APSV
    private ArcProgressStackView mArcProgressStackView;
    // Parsed colors
    private int[] mStartColors = new int[MODEL_COUNT];
    private int[] mEndColors = new int[MODEL_COUNT];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*全局导航栏*/
//        setContentView(R.layout.activity_perferance);
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_perferance, null, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Snackbar.make(view, "Replace with your own action", Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initProcessBar();
    }

    private void initProcessBar() {

        // Get APSV
        mArcProgressStackView = (ArcProgressStackView) findViewById(R.id.apsv);

        // Get colors
        final String[] startColors = getResources().getStringArray(R.array.polluted_waves);
        final String[] endColors = getResources().getStringArray(R.array.default_preview);
        final String[] bgColors = getResources().getStringArray(R.array.medical_express);

        // Parse colors
        for (int i = 0; i < MODEL_COUNT; i++) {
            mStartColors[i] = Color.parseColor(startColors[i]);
            mEndColors[i] = Color.parseColor(endColors[i]);
        }

        // Set models
        final ArrayList<ArcProgressStackView.Model> models = new ArrayList<>();
        models.add(new ArcProgressStackView.Model("时效", 30, Color.parseColor(bgColors[0]), mStartColors[0]));
        models.add(new ArcProgressStackView.Model("质量", 50, Color.parseColor(bgColors[1]), mStartColors[1]));
        models.add(new ArcProgressStackView.Model("数量", 70, Color.parseColor(bgColors[2]), mStartColors[2]));
        models.add(new ArcProgressStackView.Model("态度", 85, Color.parseColor(bgColors[3]), mStartColors[3]));
        mArcProgressStackView.setModels(models);


    }

}
