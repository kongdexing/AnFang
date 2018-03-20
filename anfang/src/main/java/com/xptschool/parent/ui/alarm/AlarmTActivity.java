package com.xptschool.parent.ui.alarm;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.support.v4.widget.SwipeRefreshLayout;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.android.widget.view.LoadMoreRecyclerView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanAlarm;
import com.xptschool.parent.common.BroadcastAction;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanClass;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseListActivity;
import com.xptschool.parent.view.CalendarView;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

//教师警报查询
public class AlarmTActivity extends BaseListActivity {

    @BindView(R.id.llDate)
    LinearLayout llDate;

    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipeRefresh;

    @BindView(R.id.recycleView)
    LoadMoreRecyclerView recycleView;

    @BindView(R.id.spnClass)
    MaterialSpinner spnClass;

    @BindView(R.id.txtDate)
    TextView txtDate;

    @BindView(R.id.flTransparent)
    FrameLayout flTransparent;

    @BindView(R.id.llCheckTitle)
    LinearLayout llCheckTitle;

    private PopupWindow datePopup;
    private AlarmAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_alarm_t);
        setTitle(R.string.home_alarm);
        initView();
        initDate();

        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(BroadcastAction.ALARM_AMEND);
        registerReceiver(AlarmAmendReceiver, intentFilter);
    }

    private void initView() {
        initRecyclerView(recycleView, swipeRefresh);

        adapter = new AlarmAdapter(this);
        recycleView.setAdapter(adapter);

        swipeRefresh.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getFirstPageData();
            }
        });

        recycleView.setLoadMoreListener(new LoadMoreRecyclerView.LoadMoreListener() {
            @Override
            public void onLoadMore() {
                Log.i(TAG, "onLoadMore: ");
                if (resultPage.getPage() < resultPage.getTotal_page()) {
                    resultPage.setPage(resultPage.getPage() + 1);
                    getAlarmList(txtDate.getText().toString());
                }
            }
        });

    }

    private void initDate() {
        txtDate.setText(CommonUtil.getCurrentDate());

        List<BeanClass> beanClasses = GreenDaoHelper.getInstance().getAllClass();
        if (beanClasses.size() == 0) {
            spnClass.setText(R.string.toast_class_empty);
            spnClass.setEnabled(false);
            Toast.makeText(this, R.string.toast_class_empty, Toast.LENGTH_SHORT).show();
            return;
        } else {
            spnClass.setItems(beanClasses);
        }

        spnClass.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanClass>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, BeanClass item) {
                getFirstPageData();
            }
        });
        spnClass.setOnNothingSelectedListener(spinnerNothingSelectedListener);
        getFirstPageData();
    }

    private void getFirstPageData() {
        flTransparent.setVisibility(View.GONE);
        resultPage.setPage(1);
        adapter.refreshData(new ArrayList<BeanAlarm>());
        getAlarmList(txtDate.getText().toString());
    }

    @OnClick({R.id.llDate, R.id.spnClass})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.llDate:
                showDatePop();
                break;
            case R.id.spnClass:
                if (spnClass.getItems().size() > 0) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
        }
    }

    private void getAlarmList(String date) {
        BeanClass beanClass = (BeanClass) spnClass.getSelectedItem();

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Track_alarm,
                new MyVolleyHttpParamsEntity()
                        .addParam("dates", date)
                        .addParam("c_id", beanClass.getC_id())
                        .addParam("page", resultPage.getPage() + "")
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Track_alarm)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        if (swipeRefresh != null && resultPage.getPage() == 1) {
                            swipeRefresh.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        if (swipeRefresh != null && resultPage.getPage() == 1) {
                            swipeRefresh.setRefreshing(false);
                        }
                        switch (httpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject jsonObject = new JSONObject(httpResult.getData().toString());
                                    resultPage.setPage(jsonObject.getInt("page"));
                                    resultPage.setTotal_page(jsonObject.getInt("total_page"));
                                    resultPage.setTotal_count(jsonObject.getInt("total_count"));

                                    if (resultPage.getTotal_page() > resultPage.getPage()) {
                                        recycleView.setAutoLoadMoreEnable(true);
                                    } else {
                                        recycleView.setAutoLoadMoreEnable(false);
                                    }

                                    Gson gson = new Gson();
                                    List<BeanAlarm> beanAlarms = gson.fromJson(jsonObject.getJSONArray("content").toString(),
                                            new TypeToken<List<BeanAlarm>>() {
                                            }.getType());

                                    if (beanAlarms.size() > 0) {
                                        llCheckTitle.setVisibility(View.VISIBLE);
                                    } else {
                                        llCheckTitle.setVisibility(View.GONE);
                                    }

                                    if (resultPage.getPage() > 1) {
                                        adapter.appendData(beanAlarms);
                                    } else {
                                        //第一页数据
                                        if (beanAlarms.size() == 0) {
                                            Toast.makeText(AlarmTActivity.this, R.string.toast_data_empty, Toast.LENGTH_SHORT).show();
                                        }
                                        recycleView.removeAllViews();
                                        adapter.refreshData(beanAlarms);
                                    }
                                    recycleView.notifyMoreFinish(resultPage.getTotal_page() > resultPage.getPage());

                                } catch (Exception ex) {
                                    if (llCheckTitle != null)
                                        llCheckTitle.setVisibility(View.GONE);
                                    Toast.makeText(AlarmTActivity.this, ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(AlarmTActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        if (swipeRefresh != null && resultPage.getPage() == 1) {
                            swipeRefresh.setRefreshing(false);
                        }
                    }
                });
    }

    private void showDatePop() {
        if (datePopup == null) {
            CalendarView calendarView = new CalendarView(this, CalendarView.SELECTION_MODE_SINGLE);
            calendarView.setContainerGravity(Gravity.CENTER);

            calendarView.setSelectedListener(new CalendarView.CalendarViewSelectedListener() {
                @Override
                public void onCalendarSelected(int mode, String... date) {
                    datePopup.dismiss();
                    String dateStr = txtDate.getText().toString();
                    if (mode == CalendarView.SELECTION_MODE_SINGLE) {
                        dateStr = date[0];
                    }
                    txtDate.setText(dateStr);
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
                }
            });
        }
        flTransparent.setVisibility(View.VISIBLE);
        datePopup.showAsDropDown(llDate, 0, 2);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        try {
            this.unregisterReceiver(AlarmAmendReceiver);
        } catch (Exception ex) {

        }
    }

    MaterialSpinner.OnNothingSelectedListener spinnerNothingSelectedListener = new MaterialSpinner.OnNothingSelectedListener() {
        @Override
        public void onNothingSelected(MaterialSpinner spinner) {
            flTransparent.setVisibility(View.GONE);
        }
    };

    BroadcastReceiver AlarmAmendReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            Log.i(TAG, "onReceive: " + intent.getAction());

            if (intent.getAction() == BroadcastAction.ALARM_AMEND) {
                Log.i(TAG, "onReceive: equal");
                if (intent == null || intent.getExtras() == null) {
                    Log.i(TAG, "onActivityResult: data.getExtras() is null");
                    return;
                }
                BeanAlarm alarm = intent.getExtras().getParcelable(ExtraKey.ALARM_DETAIL);
                if (alarm == null) {
                    Log.i(TAG, " is null");
                    return;
                }

                Log.i(TAG, "onReceive: " + alarm.toString());
                recycleView.updateItem(adapter.updateData(alarm));
            }
        }
    };

}
