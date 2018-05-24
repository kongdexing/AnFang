package com.xptschool.parent.ui.watch;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;

import com.android.widget.view.LoadMoreRecyclerView;
import com.xptschool.parent.R;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;

/**
 * 聊天列表页面
 * 1.循环加载家长绑定的学生卡，手表
 * 2.加载本地聊天数据
 */
public class ChatListActivity extends BaseListActivity {

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    WatchChatAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chatlist);
        setTitle(R.string.home_chat);

        initView();

    }

    private void initView() {
        initRecyclerView(recyclerView, swipeRefreshLayout);
        adapter = new WatchChatAdapter(this);
        swipeRefreshLayout.setEnabled(false);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {

            }
        });
        recyclerView.setAdapter(adapter);
    }

    @Override
    protected void onResume() {
        super.onResume();
        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        adapter.refreshData(students);
    }
}
