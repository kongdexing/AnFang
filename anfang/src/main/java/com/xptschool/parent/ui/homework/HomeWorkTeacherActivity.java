package com.xptschool.parent.ui.homework;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.android.widget.view.ArrowTextView;
import com.android.widget.view.LoadMoreRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanHomeWork;
import com.xptschool.parent.common.ActivityResultCode;
import com.xptschool.parent.common.BroadcastAction;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanClass;
import com.xptschool.parent.model.BeanCourse;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseListActivity;
import com.xptschool.parent.view.CalendarOptionView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeWorkTeacherActivity extends BaseListActivity {

    @BindView(R.id.recyclerview)
    LoadMoreRecyclerView recyclerView;

    @BindView(R.id.refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;

    @BindView(R.id.spnClass)
    MaterialSpinner spnClass;

    @BindView(R.id.spnCourse)
    MaterialSpinner spnCourse;

    @BindView(R.id.txtDate)
    ArrowTextView txtDate;

    @BindView(R.id.flTransparent)
    FrameLayout flTransparent;

    private PopupWindow datePopup;
    private List<BeanHomeWork> homeWorks_filter = new ArrayList<>();
    private String startTime, endTime;
    private HomeWorkTeacherAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_teacher);
        setTitle(R.string.home_homework);
        setTxtRight(R.string.push);
        initView();
        initDate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.HOMEWORK_AMEND);
        this.registerReceiver(HomeworkAmendReceiver, intentFilter);
    }

    private void initView() {

        initRecyclerView(recyclerView, swipeRefreshLayout);

        adapter = new HomeWorkTeacherAdapter(this);
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

        setTextRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(HomeWorkTeacherActivity.this, HomeWorkDetailTeacherActivity.class));
            }
        });
    }

    private void initDate() {
        spnClass.setItems(GreenDaoHelper.getInstance().getAllClassNameAppend());
        spnClass.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanClass>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, BeanClass item) {
                flTransparent.setVisibility(View.GONE);
                List<BeanCourse> courses = new ArrayList<BeanCourse>();
                if (item != null && item.getG_id() != null) {
                    courses = GreenDaoHelper.getInstance().getCourseByGId(item.getG_id());
                }
                if (courses.size() == 0) {
                    spnCourse.setItems("无课程");
                } else {
                    spnCourse.setItems(courses);
                }
                getFirstPageData();
            }
        });
        spnClass.setOnNothingSelectedListener(spinnerNothingSelectedListener);

        spnCourse.setItems(GreenDaoHelper.getInstance().getAllCourseNameAppend());
        spnCourse.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanCourse>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, BeanCourse item) {
                getFirstPageData();
            }
        });
        spnCourse.setOnNothingSelectedListener(spinnerNothingSelectedListener);

        startTime = CommonUtil.getDate2StrBefore(6);
        endTime = CommonUtil.getCurrentDate();

        setTxtDate();
        getFirstPageData();
    }

    private void getFirstPageData(){
        flTransparent.setVisibility(View.GONE);
        resultPage.setPage(1);
        adapter.refreshData(new ArrayList<BeanHomeWork>());
        getHomeWorkList();
    }

    private void setTxtDate() {
        txtDate.setText(startTime + "\n" + endTime);
    }

    @OnClick({R.id.txtDate, R.id.spnClass, R.id.spnCourse})
    void homeWorkClick(View view) {
        switch (view.getId()) {
            case R.id.txtDate:
                showDatePop();
                break;
            case R.id.spnClass:
                if (spnClass.getItems().size() != 1) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.spnCourse:
                if (spnCourse.getItems().size() != 1) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    //新增，删除，修改
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        Log.i(TAG, "onActivityResult: " + resultCode);
        if (data == null || data.getExtras() == null) {
            Log.i(TAG, "onActivityResult: data.getExtras() is null");
            return;
        }
        BeanHomeWork homeWork = data.getExtras().getParcelable(ExtraKey.HOMEWORK_DETAIL);
        if (homeWork == null) {
            Log.i(TAG, "onActivityResult: homeWork is null");
            return;
        }

        switch (resultCode) {
            case ActivityResultCode.HomeWork_delete:
                Log.i(TAG, "onActivityResult: deleteData");
                int position = adapter.deleteData(homeWork);
                if (position != -1) {
                    recyclerView.deleteByPosition(position);
                }
                break;
        }
        super.onActivityResult(requestCode, resultCode, data);
    }

    private void getHomeWorkList() {
        BeanClass currentClass = (BeanClass) spnClass.getSelectedItem();
        BeanCourse currentCourse = new BeanCourse();
        try {
            currentCourse = (BeanCourse) spnCourse.getSelectedItem();
        } catch (Exception ex) {
            currentCourse = new BeanCourse();
        }

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.HOMEWORK_QUERY,
                new MyVolleyHttpParamsEntity()
                        .addParam("c_id", currentClass.getC_id())
                        .addParam("sdate", startTime)
                        .addParam("edate", endTime)
                        .addParam("page", resultPage.getPage() + "")
                        .addParam("crs_id", currentCourse.getId())
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

                                    List<BeanHomeWork> homeWorks = new ArrayList<>();
//                                    JSONArray jsonArray = jsonObject.getJSONArray("content");
                                    Gson gson = new Gson();
                                    homeWorks = gson.fromJson(jsonObject.getJSONArray("content").toString(), new TypeToken<List<BeanHomeWork>>() {
                                    }.getType());

                                    if (resultPage.getPage() > 1) {
                                        adapter.appendData(homeWorks);
                                    } else {
                                        //第一页数据
                                        if (homeWorks.size() == 0) {
                                            Toast.makeText(HomeWorkTeacherActivity.this, R.string.toast_data_empty, Toast.LENGTH_SHORT).show();
                                        }
                                        recyclerView.removeAllViews();
                                        adapter.refreshData(homeWorks);
                                    }
                                    recyclerView.notifyMoreFinish(resultPage.getTotal_page() > resultPage.getPage());
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                    Toast.makeText(HomeWorkTeacherActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(HomeWorkTeacherActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
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

    private void showDatePop() {
        if (datePopup == null) {
            CalendarOptionView calendarView = new CalendarOptionView(this);

            calendarView.setSelectedListener(new CalendarOptionView.CalendarViewSelectedListener() {
                @Override
                public void onCalendarSelected(String sDate, String eDate) {
                    datePopup.dismiss();
                    startTime = sDate;
                    endTime = eDate;
                    setTxtDate();
                    getFirstPageData();
                }
            });
            datePopup = new PopupWindow(calendarView,
                    LinearLayout.LayoutParams.WRAP_CONTENT, CommonUtil.getPopDateHeight(), true);
            datePopup.setTouchable(true);
            datePopup.setBackgroundDrawable(new BitmapDrawable());
            datePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    flTransparent.setVisibility(View.GONE);
                    txtDate.collapse();
                }
            });
        }
        flTransparent.setVisibility(View.VISIBLE);
        datePopup.showAsDropDown(txtDate, 0, 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            this.unregisterReceiver(HomeworkAmendReceiver);
        } catch (Exception ex) {

        }
    }

    MaterialSpinner.OnNothingSelectedListener spinnerNothingSelectedListener = new MaterialSpinner.OnNothingSelectedListener() {
        @Override
        public void onNothingSelected(MaterialSpinner spinner) {
            flTransparent.setVisibility(View.GONE);
        }
    };

    BroadcastReceiver HomeworkAmendReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + intent.getAction());

            if (intent.getAction() == BroadcastAction.HOMEWORK_AMEND) {
                Log.i(TAG, "onReceive: equal");
                if (intent == null || intent.getExtras() == null) {
                    Log.i(TAG, "onActivityResult: data.getExtras() is null");
                    return;
                }
                BeanHomeWork homeWork = intent.getExtras().getParcelable(ExtraKey.HOMEWORK_DETAIL);
                if (homeWork == null) {
                    Log.i(TAG, " is null");
                    return;
                }

                Log.i(TAG, "onReceive: " + homeWork.toString());
                recyclerView.updateItem(adapter.updateData(homeWork));
            }
        }
    };
}
