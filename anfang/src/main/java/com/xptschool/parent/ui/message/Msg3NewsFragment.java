package com.xptschool.parent.ui.message;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.LoadMoreRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanHomeWork;
import com.xptschool.parent.bean.BeanNews;
import com.xptschool.parent.bean.ResultPage;
import com.xptschool.parent.common.NewsType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.fragment.BaseFragment;
import com.xptschool.parent.ui.fragment.BaseListFragment;
import com.xptschool.parent.ui.homework.HomeWorkTeacherActivity;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuhaixinxi on 2018/3/23.
 */

public class Msg3NewsFragment extends BaseListFragment {

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipe_refresh_widget;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;
    NewsAdapter adapter;
    @BindView(R.id.txtEmpty)
    TextView txtEmpty;
    String newsType = "";

    public ResultPage resultPage = new ResultPage();

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_1_news, container, false);
        ButterKnife.bind(this, mRootView);

//        Bundle bundle = getArguments();
//        if (bundle != null) {
//            newsType = bundle.getString("type");
//            Log.i(TAG, "onCreateView: type " + newsType);
//        }
        initView();
        return mRootView;
    }

    private void initView() {
        initRecyclerView(recyclerView, swipe_refresh_widget);

        swipe_refresh_widget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
//                getContacts();
                getNewsList();
            }
        });

        adapter = new NewsAdapter(mContext);
        recyclerView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                if (resultPage.getPage() < resultPage.getTotal_page()) {
                    resultPage.setPage(resultPage.getPage() + 1);
                    getNewsList();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void setNewsType(String type) {
        Log.i(TAG, "setNewsType: " + type);
        newsType = type;
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
                        .addParam("page", resultPage.getPage() + ""), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        if (swipe_refresh_widget != null && resultPage.getPage() == 1) {
                            swipe_refresh_widget.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (swipe_refresh_widget != null && resultPage.getPage() == 1) {
                            swipe_refresh_widget.setRefreshing(false);
                        }
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject jsonObject = new JSONObject(volleyHttpResult.getData().toString());
                                    resultPage.setPage(jsonObject.getInt("page"));
                                    resultPage.setTotal_page(jsonObject.getInt("total_page"));
                                    resultPage.setTotal_count(jsonObject.getInt("total_count"));

                                    if (resultPage.getTotal_page() > resultPage.getPage()) {
                                        recyclerView.setAutoLoadMoreEnable(true);
                                    } else {
                                        recyclerView.setAutoLoadMoreEnable(false);
                                    }

                                    List<BeanNews> beanNews = new ArrayList<>();
//                                    JSONArray jsonArray = jsonObject.getJSONArray("content");
                                    Gson gson = new Gson();
                                    beanNews = gson.fromJson(jsonObject.getJSONArray("content").toString(), new TypeToken<List<BeanNews>>() {
                                    }.getType());

                                    if (resultPage.getPage() > 1) {
                                        adapter.appendData(beanNews);
                                    } else {
                                        //第一页数据
                                        if (beanNews.size() == 0) {
                                            //数据为空
                                            txtEmpty.setVisibility(View.VISIBLE);
                                        } else {
                                            txtEmpty.setVisibility(View.GONE);
                                        }
                                        recyclerView.removeAllViews();
                                        adapter.refreshData(beanNews);
                                    }
                                    recyclerView.notifyMoreFinish(resultPage.getTotal_page() > resultPage.getPage());
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                    Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(mContext, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                                break;
                        }

                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        if (swipe_refresh_widget != null && resultPage.getPage() == 1) {
                            swipe_refresh_widget.setRefreshing(false);
                        }
                    }
                });

    }

}
