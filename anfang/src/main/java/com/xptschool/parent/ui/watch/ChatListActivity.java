package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.view.View;

import com.android.widget.view.LoadMoreRecyclerView;
import com.xptschool.parent.R;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.homework.HomeWorkParentAdapter;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.main.BaseListActivity;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

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

        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        List<BeanStudent> watchStu = new ArrayList<>();
        for (int i = 0; i < students.size(); i++) {
            BeanStudent student = students.get(i);
            if (student.getDevice_type().equals("2")) {
                watchStu.add(student);
            }
        }
        adapter.refreshData(watchStu);

    }


}
