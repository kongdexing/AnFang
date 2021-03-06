package com.xptschool.parent.ui.mine;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.LoadMoreRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.bean.BeanInvite;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseListActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

public class MyInviteActivity extends BaseListActivity {

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.llInviteTitle)
    LinearLayout llInviteTitle;

    @BindView(R.id.recyclerView)
    LoadMoreRecyclerView recyclerView;

    MyInviteAdapter adapter;
    private String userId = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_invite);

        initView();

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            userId = bundle.getString("user_id");
            if (userId.equals(XPTApplication.getInstance().getCurrentUserId())) {
                setTitle(R.string.mine_invite);
            } else {
                String userName = bundle.getString("user_name");
                setTitle("【" + userName + "】的会员");
            }
        }

        getFirstPageData();
    }

    private void initView() {
        initRecyclerView(recyclerView, swipeRefreshLayout);

        adapter = new MyInviteAdapter(this);
        recyclerView.setAdapter(adapter);

        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (resultPage.getPage() < resultPage.getTotal_page()) {
                    resultPage.setPage(resultPage.getPage() + 1);
                    getInviteList();
                }
            }
        });
    }

    private void getFirstPageData() {
        resultPage.setPage(1);
        adapter.refreshData(new ArrayList<BeanInvite>());
        getInviteList();
    }

    private void getInviteList() {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_REFER,
                new MyVolleyHttpParamsEntity()
                        .addParam("page", resultPage.getPage() + "")
                        .addParam("user_id", userId),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        if (swipeRefreshLayout != null && resultPage.getPage() == 1) {
                            swipeRefreshLayout.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        if (swipeRefreshLayout != null && resultPage.getPage() == 1) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                        switch (httpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject jsonObject = new JSONObject(httpResult.getData().toString());
                                    resultPage.setPage(jsonObject.getInt("page"));
                                    resultPage.setTotal_page(jsonObject.getInt("total_page"));
                                    resultPage.setTotal_count(jsonObject.getInt("total_count"));

                                    if (resultPage.getTotal_page() > resultPage.getPage()) {
                                        recyclerView.setAutoLoadMoreEnable(true);
                                    } else {
                                        recyclerView.setAutoLoadMoreEnable(false);
                                    }

                                    List<BeanInvite> beanInvites = new ArrayList<>();
//                                    JSONArray jsonArray = jsonObject.getJSONArray("content");
                                    Gson gson = new Gson();
                                    beanInvites = gson.fromJson(jsonObject.getJSONArray("content").toString(), new TypeToken<List<BeanInvite>>() {
                                    }.getType());

                                    if (resultPage.getPage() > 1) {
                                        adapter.appendData(beanInvites);
                                    } else {
                                        //第一页数据
                                        if (beanInvites.size() == 0) {
                                            llInviteTitle.setVisibility(View.GONE);
                                            Toast.makeText(MyInviteActivity.this, R.string.toast_data_empty, Toast.LENGTH_SHORT).show();
                                        } else {
                                            llInviteTitle.setVisibility(View.VISIBLE);
                                        }

                                        recyclerView.removeAllViews();
                                        adapter.refreshData(beanInvites);
                                    }
                                    recyclerView.notifyMoreFinish(resultPage.getTotal_page() > resultPage.getPage());
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                    Toast.makeText(MyInviteActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(MyInviteActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        if (swipeRefreshLayout != null && resultPage.getPage() == 1) {
                            swipeRefreshLayout.setRefreshing(false);
                        }
                    }
                });
    }

}
