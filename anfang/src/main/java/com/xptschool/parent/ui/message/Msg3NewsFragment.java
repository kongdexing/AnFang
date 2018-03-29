package com.xptschool.parent.ui.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.LoadMoreRecyclerView;
import com.xptschool.parent.R;
import com.xptschool.parent.common.NewsType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.fragment.BaseFragment;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuhaixinxi on 2018/3/23.
 */

public class Msg3NewsFragment extends BaseFragment {

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipe_refresh_widget;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    String newsType = "";

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_1_news, container, false);
        ButterKnife.bind(this, mRootView);

        Bundle bundle = getArguments();
        if (bundle != null) {
            newsType = bundle.getString("type");
        }
        initView();
        return mRootView;
    }

    private void initView() {
        swipe_refresh_widget.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
        swipe_refresh_widget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getContacts();
                getNewsList();
            }
        });

    }

    @Override
    protected void initData() {
        Log.i(TAG, "initData: ");
        getNewsList();
    }

    private void getNewsList() {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_MESSAGE,
                new MyVolleyHttpParamsEntity()
                        .addParam("m_type", newsType)
                        .addParam("page", "1"), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();

                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);

                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                    }
                });

    }

}
