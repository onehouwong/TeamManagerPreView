package com.walkindeep.teammanagerpreview.UI;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.cocosw.bottomsheet.BottomSheet;
import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.view.MaterialListView;
import com.walkindeep.teammanagerpreview.Project.Issue;
import com.walkindeep.teammanagerpreview.Project.User;
import com.walkindeep.teammanagerpreview.R;

//import com.igexin.sdk.PushManager;

import java.util.ArrayList;
import java.util.List;

/**
 * 我的任务界面，同时也是主界面
 */
public class MyTaskActivity extends NavigationActivity {
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

//push
        //PushManager.getInstance().initialize(this.getApplicationContext());


        /*全局导航栏*/
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_task, null, false);
        drawer.addView(contentView, 0);

        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        /*
        右下角+号 浮动按钮
         */
        FloatingActionButton fab = (FloatingActionButton) findViewById(R.id.floatingActionButtionTask);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                showBottomSheet();
            }
        });
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        final User user = User.getUser();  //= User.init("test2", "teammanager");

        user.updateToDoIssueList(this);

        /*下滑刷新*/
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                user.updateToDoIssueList(MyTaskActivity.this);
                setCardsToUI(buildCardsList());

                swipeRefreshLayout.setRefreshing(false);
            }
        });
    }

    public Card[] buildCardsList() {
        final User user = User.getUser();
        List<Issue> toDoIssueList = user.getToDoIssueList();

        List<Card> cards = new ArrayList<>();

        for (Issue issue : toDoIssueList) {
            Card card = new Card.Builder(this)
                    .withProvider(new CardProvider())
                    .setLayout(R.layout.material_basic_buttons_card)

                    .setTitle(issue.getSubject())
                    .setDescription(issue.getDescription())
                    .addAction(R.id.left_text_button, new TextViewAction(this)
                            .setText("按钮1")
                            .setTextResourceColor(R.color.black_button)
                            .setListener(new OnActionClickListener() {
                                @Override
                                public void onActionClicked(View view, Card card) {
                                    Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
                                }
                            }))
                    .addAction(R.id.right_text_button, new TextViewAction(this)
                            .setText("按钮2")
                            .setTextResourceColor(R.color.accent_material_dark)
                            .setListener(new OnActionClickListener() {
                                @Override
                                public void onActionClicked(View view, Card card) {
                                    Toast.makeText(mContext, "You have pressed the right button", Toast.LENGTH_SHORT).show();
                                }
                            }))
                    .endConfig()
                    .build();
            card.setTag(issue);//设置卡片和issue关联
            cards.add(card);
        }
        return cards.toArray(new Card[cards.size()]);
    }

    private void showBottomSheet() {
        new BottomSheet.Builder(this).title("新建").sheet(R.menu.task_bottom_sheet).listener(new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                Intent intent;

                switch (which) {
                    case R.id.buildProject://创建项目按钮

                        intent = new Intent(MyTaskActivity.this, NewProject.class);
                        startActivity(intent);
                        break;

                    case R.id.buildTask://创建任务按钮
                        intent = new Intent(MyTaskActivity.this, CreateTaskActivity.class);
                        startActivity(intent);
                        break;
                }
            }
        }).show();
    }

//    /*更新任务数据*/
//    private void updateTask(User user, SwipeRefreshLayout swipeRefreshLayout) {
//        DataHandler dataHandler = new DataHandler();
//        dataHandler.getData("issues.json?assigned_to_id=me" + "&status_id=1", this, user);
//        swipeRefreshLayout.setRefreshing(false);
//    }


//    private Card[] jsonToCards(JSONObject userIssuesJSONObjectTemp) {
//        JSONArray userIssuesJSONArray = null;
//        try {
//            userIssuesJSONArray = userIssuesJSONObjectTemp.getJSONArray("issues");
//
//        } catch (JSONException e) {
//            e.printStackTrace();
//        }
//        List<Card> cards = new ArrayList<>();
//
//        /*用于存储所有issue的list*/
//        List<Issue> issueList = Issue.getToDoIssueList();
//        issueList.clear();
//
//        for (int i = 0; i < userIssuesJSONArray.length(); i++) {
//            try {
//                /*创建issue类*/
//                Issue issue = new Issue(userIssuesJSONArray.getJSONObject(i).getString("subject"), userIssuesJSONArray.getJSONObject(i).getString("description"), userIssuesJSONArray.getJSONObject(i).getInt("id"));
//                issueList.add(issue);
//
//                Card card = new Card.Builder(this)
//                        .withProvider(new CardProvider())
//                        .setLayout(R.layout.material_basic_buttons_card)
//
//                        .setTitle(issue.getSubject())
//                        .setDescription(issue.getDescription())
//                        .addAction(R.id.left_text_button, new TextViewAction(this)
//                                .setText("按钮1")
//                                .setTextResourceColor(R.color.black_button)
//                                .setListener(new OnActionClickListener() {
//                                    @Override
//                                    public void onActionClicked(View view, Card card) {
//                                        Toast.makeText(mContext, "You have pressed the left button", Toast.LENGTH_SHORT).show();
//                                    }
//                                }))
//                        .addAction(R.id.right_text_button, new TextViewAction(this)
//                                .setText("按钮2")
//                                .setTextResourceColor(R.color.accent_material_dark)
//                                .setListener(new OnActionClickListener() {
//                                    @Override
//                                    public void onActionClicked(View view, Card card) {
//                                        Toast.makeText(mContext, "You have pressed the right button", Toast.LENGTH_SHORT).show();
//                                    }
//                                }))
//                        .endConfig()
//                        .build();
//                card.setTag(issue);//设置卡片和issue关联
//                cards.add(card);
//            } catch (JSONException e) {
//                e.printStackTrace();
//            }
//
//        }
////        return cards.toArray(new Card[cards.size()]);
//    }


    public void setCardsToUI(Card[] cards) {
        final MaterialListView mListView = (MaterialListView) findViewById(R.id.material_listview);
        mListView.getAdapter().clearAll();
        for (int i = 0; i < cards.length; i++) {
            mListView.getAdapter().add(cards[i]);

            /*设置卡片可以滑动删除*/
            cards[i].setDismissible(true);
        }

        /*设置滑动删除操作*/
        mListView.setOnDismissCallback(new OnDismissCallback() {
            public void onDismiss(final Card card, final int position) {
                Snackbar.make(mListView, "已标记完成", Snackbar.LENGTH_LONG)
                        .setCallback(new Snackbar.Callback() {
                            @Override
                            public void onDismissed(Snackbar snackbar, int event) {
                                if (event == DISMISS_EVENT_ACTION) {//do nothing but recover card
                                    mListView.getAdapter().add(position, card);
                                } else {
                                    Issue issue = (Issue) card.getTag();
                                    issue.setStatusid(3);
                                    issue.pushStatusName(mContext);



                                }
                            }
                        })
                        .setAction("撤销", new View.OnClickListener() {
                            @Override
                            public void onClick(View v) {

                            }
                        })

                        .show();
            }
        });
    }

    /**
     * 获取用户的issue数据
     */
//    class DataHandler extends AbstractDataQuery {
//        /**
//         * @param userIssuesJSONObject
//         */
//        @Override
//        protected void work(JSONObject userIssuesJSONObject) {
//            Card[] cards = jsonToCards(userIssuesJSONObject);
//            setCardsToUI(cards);
//        }
//
//        protected void parseXMLWithPull(String xmlData) {
//
//        }
//    }
}
