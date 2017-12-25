package com.shuhai.anfang.ui.honor;

import android.content.Intent;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.android.widget.view.LoadMoreRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuhai.anfang.R;
import com.shuhai.anfang.bean.BeanHonor;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.model.BeanClass;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.main.BaseListActivity;
import com.shuhai.anfang.util.TeacherUtil;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 老师 荣誉
 */
public class HonorTActivity extends BaseListActivity {

    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.spnClasses)
    MaterialSpinner spnClass;
    @BindView(R.id.spnHonorType)
    MaterialSpinner spnHonorType;

    @BindView(R.id.flTransparent)
    FrameLayout flTransparent;

    HonorAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honor_t);
        setTitle(R.string.home_honour);
        initView();
        initDate();
    }

    public void initView() {
        initRecyclerView(recyclerView, swipeRefreshLayout);

        adapter = new HonorAdapter(this);
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
                    getHonorList();
                }
            }
        });
        recyclerView.setAdapter(adapter);

        setTxtRight(R.string.push);
        setTextRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(HonorTActivity.this,HonorPushActivity.class));
            }
        });

    }

    public void initDate() {
        spnClass.setItems(GreenDaoHelper.getInstance().getAllClassNameAppend());
        spnClass.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanClass>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, BeanClass item) {
                flTransparent.setVisibility(View.GONE);
                getFirstPageData();
            }
        });
        spnClass.setOnNothingSelectedListener(spinnerNothingSelectedListener);

        spnHonorType.setItems(TeacherUtil.honorType);
        spnHonorType.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<String>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, String item) {
                flTransparent.setVisibility(View.GONE);
                getFirstPageData();
            }
        });
        spnHonorType.setOnNothingSelectedListener(spinnerNothingSelectedListener);
        getFirstPageData();
    }

    @OnClick({R.id.spnClasses, R.id.spnHonorType})
    void honorClick(View view) {
        switch (view.getId()) {
            case R.id.spnClasses:
                if (spnClass.getItems().size() != 1) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.spnHonorType:
                if (spnHonorType.getItems().size() != 1) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void getFirstPageData() {
        flTransparent.setVisibility(View.GONE);
        resultPage.setPage(1);
        adapter.refreshData(new ArrayList<BeanHonor>());
        getHonorList();
    }

    private void getHonorList() {
        BeanClass currentClass = (BeanClass) spnClass.getSelectedItem();
        String type = spnHonorType.getSelectedIndex() + "";

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Honor_query,
                new VolleyHttpParamsEntity()
                        .addParam("c_id", currentClass.getC_id())
                        .addParam("reward_type", type)
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

                                    List<BeanHonor> honors = new ArrayList<>();
                                    Gson gson = new Gson();
                                    honors = gson.fromJson(jsonObject.getJSONArray("content").toString(), new TypeToken<List<BeanHonor>>() {
                                    }.getType());

                                    if (resultPage.getPage() > 1) {
                                        adapter.appendData(honors);
                                    } else {
                                        //第一页数据
                                        if (honors.size() == 0) {
                                            Toast.makeText(HonorTActivity.this, R.string.toast_data_empty, Toast.LENGTH_SHORT).show();
                                        }
                                        recyclerView.removeAllViews();
                                        adapter.refreshData(honors);
                                    }
                                    recyclerView.notifyMoreFinish(resultPage.getTotal_page() > resultPage.getPage());
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                    Toast.makeText(HonorTActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(HonorTActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
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
