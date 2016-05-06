package com.walkindeep.teammanagerpreview;

import android.content.Context;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.design.widget.Snackbar;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.Toolbar;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Toast;

import com.dexafree.materialList.card.Card;
import com.dexafree.materialList.card.CardProvider;
import com.dexafree.materialList.card.OnActionClickListener;
import com.dexafree.materialList.card.action.TextViewAction;
import com.dexafree.materialList.listeners.OnDismissCallback;
import com.dexafree.materialList.view.MaterialListView;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MyTaskActivity extends NavigationActivity {
    private Context mContext = this;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        /*全局导航栏*/
//        setContentView(R.layout.activity_task);//不再需要
        LayoutInflater inflater = (LayoutInflater) this
                .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View contentView = inflater.inflate(R.layout.activity_task, null, false);
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

//        //generate task
//        ViewGroup task_layout = (ViewGroup) findViewById(R.id.task_layout);
//        CardView cardView = (CardView) findViewById(R.id.task_card_view);
//        EditText task_headline = (EditText) findViewById(R.id.task_headline);
//        task_headline.setText("");
//        EditText task_content = (EditText) findViewById(R.id.task_content);
//        task_content.setText("");
//        task_layout.addView(cardView);

/*cardView*/
//        initCard();
        final User user = User.init("guojiahao", "teammanager");
        updateTask(user);

        /*下滑刷新*/
        final SwipeRefreshLayout swipeRefreshLayout = (SwipeRefreshLayout) findViewById(R.id.SwipeRefreshLayout);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                updateTask(user, swipeRefreshLayout);
            }
        });
    }

    /*更新任务数据*/
    private void updateTask(User user, SwipeRefreshLayout swipeRefreshLayout) {
        DataHandler dataHandler = new DataHandler();
        dataHandler.getData("issues.json?assigned_to_id=me" + "&status_id=1", this, user);
        swipeRefreshLayout.setRefreshing(false);
    }

    /*更新任务数据*/
    private void updateTask(User user) {
        DataHandler dataHandler = new DataHandler();
        dataHandler.getData("issues.json?assigned_to_id=me" + "&status_id=1", this, user);
    }
//    private void initCard() {
//        // Instantiate the RequestQueue.
//        RequestQueue queue = Volley.newRequestQueue(this);
//        String url = "http://teammanager.tk/issues.json";
//        // Request a string response from the provided URL.
//        StringRequest stringRequest = new StringRequest(Request.Method.GET, url,
//                new Response.Listener<String>() {
//                    @Override
//                    public void onResponse(String response) {
//                        Card[] cards = jsonToCards(response);
//                        setCardsToUI(cards);
//                    }
//
//
//                }, new Response.ErrorListener() {
//            @Override
//            public void onErrorResponse(VolleyError error) {
//            }
//        }) {
//
//            @Override
//            public Map<String, String> getHeaders() throws AuthFailureError {
//                HashMap<String, String> params = new HashMap<String, String>();
//                String creds = String.format("%s:%s", "guojiahao", "teammanager");
//                String auth = "Basic " + Base64.encodeToString(creds.getBytes(), Base64.DEFAULT);
//                params.put("Authorization", auth);
//                return params;
//            }
//        };
//// Add the request to the RequestQueue.
//        queue.add(stringRequest);
//    }

    private Card[] jsonToCards(JSONObject userIssuesJSONObjectTemp) {
        JSONArray userIssuesJSONArray = null;
        try {
            userIssuesJSONArray = userIssuesJSONObjectTemp.getJSONArray("issues");

        } catch (JSONException e) {
            e.printStackTrace();
        }
        List<Card> cards = new ArrayList<>();

        /*用于存储所有issue的list*/
        List<Issue> issueList = Issue.getIssueList();
        issueList.clear();

        for (int i = 0; i < userIssuesJSONArray.length(); i++) {
            try {
                /*创建issue类*/
                Issue issue = new Issue(userIssuesJSONArray.getJSONObject(i).getString("subject"), userIssuesJSONArray.getJSONObject(i).getString("description"), i, userIssuesJSONArray.getJSONObject(i).getInt("id"));
                issueList.add(issue);

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
                cards.add(card);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return cards.toArray(new Card[cards.size()]);
    }


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
            @Override
            public void onDismiss(Card card, int position) {
                Issue issue = Issue.getIssue(position);
                issue.setStatusid(3);
                issue.pushStatusName(mContext);

                List<Issue> issueList = Issue.getIssueList();
                                    /*被删除的卡片下面的卡片对应的issue 的position减一*/
                for (int i = position; i < issueList.size(); i++) {
                    issueList.get(i).setPosition(issueList.get(i).getPosition() - 1);
                }
            }
        });
    }

    class DataHandler extends AbstractDataQuery {
        @Override
        protected void work(JSONObject userIssuesJSONObject) {
            Card[] cards = jsonToCards(userIssuesJSONObject);
            setCardsToUI(cards);
        }
    }
}

