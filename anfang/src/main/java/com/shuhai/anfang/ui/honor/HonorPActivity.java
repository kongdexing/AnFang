package com.shuhai.anfang.ui.honor;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.Spinner;

import com.android.widget.spinner.MaterialSpinner;
import com.android.widget.view.LoadMoreRecyclerView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.ui.homework.HomeWorkParentAdapter;
import com.shuhai.anfang.ui.main.BaseListActivity;

import butterknife.BindView;

public class HonorPActivity extends BaseListActivity {

    @BindView(R.id.spnStudents)
    MaterialSpinner spnStudents;

    @BindView(R.id.spnHonorType)
    MaterialSpinner spnHonorType;

    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    HonorPAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honor_p);
        setTitle(R.string.home_honour);
        initView();
        initDate();
    }

    private void initView() {
        initRecyclerView(recyclerView, swipeRefreshLayout);

        adapter = new HonorPAdapter(this);
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
                    getHomeWorkList();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    private void initDate() {

    }


    private void getFirstPageData() {

    }

    private void getHomeWorkList() {

    }

}
