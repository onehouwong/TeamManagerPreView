package com.walkindeep.teammanagerpreview.DAO;

import android.content.Context;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.toolbox.Volley;
import com.walkindeep.teammanagerpreview.MyApplication;

/**
 * 网络请求队列类
 * <p/>
 * 所有访问网络的操作 都添加到这个队列里来
 * <p/>
 * 这是一个单例类
 * <p/>
 * 设计思想：这个类把RequestQueue用单例模式封装，使得整个app下只有一个网络请求队列。
 * <p/>
 * 这个网络请求队列有cache和connect。
 * <p/>
 * 通过共享同一个网络请求队列的方式访问网络，可以加快访问速度，避免每次访问网络都新建一个队列。
 *
 * @see <a href="https://developer.android.com/training/volley/requestqueue.html#singleton">
 * volley-requestqueue介绍</a>
 */
public class NetworkRequestController {
    /**
     * 唯一实例
     */
    private static NetworkRequestController mInstance;

    /**
     * 唯一的RequestQueue
     *
     * @see com.android.volley.RequestQueue
     */
    private RequestQueue mRequestQueue;

    /**
     * 上下文
     * <p/>
     * 此处应使用application的context，而不是用Activity的context,这样一来，
     * {@link com.android.volley.RequestQueue}的生命周期僵尸整个app的生命周期，而不会随着某一Activity的消失而消失。
     */
    private static Context mCtx= MyApplication.getContext();


    private NetworkRequestController() {
        mRequestQueue = getRequestQueue();
    }


    public static synchronized NetworkRequestController getInstance() {
        if (mInstance == null) {
            mInstance = new NetworkRequestController();
        }
        return mInstance;
    }

    public RequestQueue getRequestQueue() {
        if (mRequestQueue == null) {
            // getApplicationContext() is key, it keeps you from leaking the
            // Activity or BroadcastReceiver if someone passes one in.
            mRequestQueue = Volley.newRequestQueue(mCtx.getApplicationContext());
        }
        return mRequestQueue;
    }

    /**
     * 所有网络请求应添加到这个队列里，会自动执行
     *
     * @param req request
     *
     */
    public <T> void addToRequestQueue(Request<T> req) {
        getRequestQueue().add(req);
    }


}

