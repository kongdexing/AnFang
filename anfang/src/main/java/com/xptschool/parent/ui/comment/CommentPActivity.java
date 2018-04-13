package com.xptschool.parent.ui.comment;

import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.android.widget.view.LoadMoreRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanComment;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseListActivity;
import com.xptschool.parent.util.TeacherUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class CommentPActivity extends BaseListActivity {

    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.spnStudents)
    MaterialSpinner spnStudents;
    @BindView(R.id.spnType)
    MaterialSpinner spnType;

    @BindView(R.id.flTransparent)
    FrameLayout flTransparent;

    CommentAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_p);
        setTitle(R.string.home_comment);
        initView();
        initDate();
    }

    public void initView() {
        initRecyclerView(recyclerView, swipeRefreshLayout);

        adapter = new CommentAdapter(this);
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
                    getCommentList();
                }
            }
        });
        recyclerView.setAdapter(adapter);
    }

    public void initDate() {
        if (GreenDaoHelper.getInstance().getStudents().size() == 0) {
            spnStudents.setText(R.string.title_no_student);
            spnStudents.setEnabled(false);
            swipeRefreshLayout.setEnabled(false);
            return;
        }
        spnStudents.setItems(GreenDaoHelper.getInstance().getStudents());
        spnStudents.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
                getFirstPageData();
            }
        });
        spnStudents.setOnNothingSelectedListener(spinnerNothingSelectedListener);

        spnType.setItems(TeacherUtil.commentType);
        spnType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                flTransparent.setVisibility(View.GONE);
                getFirstPageData();
            }
        });
        spnType.setOnNothingSelectedListener(spinnerNothingSelectedListener);
        getFirstPageData();
    }

    @OnClick({R.id.spnStudents, R.id.spnType})
    void honorClick(View view) {
        switch (view.getId()) {
            case R.id.spnStudents:
                if (spnStudents.getItems().size() != 1) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.spnType:
                if (spnType.getItems().size() != 1) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void getFirstPageData() {
        flTransparent.setVisibility(View.GONE);
        resultPage.setPage(1);
        adapter.refreshData(new ArrayList<BeanComment>());
        getCommentList();
    }

    private void getCommentList() {
        BeanStudent currentStudent = (BeanStudent) spnStudents.getSelectedItem();
        String type = spnType.getSelectedIndex() + "";

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Remark_query,
                new MyVolleyHttpParamsEntity()
                        .addParam("stu_id", currentStudent.getStu_id())
                        .addParam("r_type", type)
                        .addParam("page", resultPage.getPage() + "")
                        .addParam("token", CommonUtil.encryptToken(HttpAction.HOMEWORK_QUERY)),
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

                                    List<BeanComment> comments = new ArrayList<>();
                                    Gson gson = new Gson();
                                    comments = gson.fromJson(jsonObject.getJSONArray("content").toString(), new TypeToken<List<BeanComment>>() {
                                    }.getType());

                                    if (resultPage.getPage() > 1) {
                                        adapter.appendData(comments);
                                    } else {
                                        //第一页数据
                                        if (comments.size() == 0) {
                                            Toast.makeText(CommentPActivity.this, R.string.toast_data_empty, Toast.LENGTH_SHORT).show();
                                        }
                                        recyclerView.removeAllViews();
                                        adapter.refreshData(comments);
                                    }
                                    recyclerView.notifyMoreFinish(resultPage.getTotal_page() > resultPage.getPage());
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                    Toast.makeText(CommentPActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(CommentPActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
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

    MaterialSpinner.OnNothingSelectedListener spinnerNothingSelectedListener = new MaterialSpinner.OnNothingSelectedListener() {
        @Override
        public void onNothingSelected(MaterialSpinner spinner) {
            flTransparent.setVisibility(View.GONE);
        }
    };

}
