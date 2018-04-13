package com.xptschool.parent.ui.honor;

import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanHonor;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

//荣誉详情
public class HonorDetailActivity extends BaseActivity {

    @BindView(R.id.txtClassName)
    TextView txtClassName;
    @BindView(R.id.txtStudent)
    TextView txtStudent;
    @BindView(R.id.txtType)
    TextView txtType;
    @BindView(R.id.txtContent)
    TextView txtContent;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    private BeanHonor currentHonor;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honor_detail);
        setTitle(R.string.title_honor_detail);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                currentHonor = bundle.getParcelable(ExtraKey.HONOR_DETAIL);
                if (currentHonor != null) {
                    initData();
                }
                String id = bundle.getString(ExtraKey.DETAIL_ID);
                if (id != null) {
                    getHonorDetail(id);
                }
            } catch (Exception ex) {
                Log.i(TAG, "onCreate: get bundle data error " + ex.getMessage());
            }
        }

        //华为机型推送使用uri传值
        Uri uri = getIntent().getData();
        if (uri != null) {
            String id = uri.getQueryParameter("id");
            if (id != null && !id.isEmpty()) {
                getHonorDetail(id);
            }
        }

    }

    private void initData() {
        txtClassName.setText(currentHonor.getG_name() + currentHonor.getC_name());
        txtStudent.setText(currentHonor.getStu_name());
        txtType.setText(currentHonor.getReward_type());
        txtContent.setText(currentHonor.getReward_details());

        btnDelete.setVisibility(View.GONE);

//        if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())
//                && currentComment.getUser_id().equals(GreenDaoHelper.getInstance().getCurrentTeacher().getU_id())) {
//            btnDelete.setVisibility(View.VISIBLE);
//        }
    }

    private void getHonorDetail(String id) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Honor_detail,
                new MyVolleyHttpParamsEntity().addParam("id", id), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在获取荣誉信息...");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                                    currentHonor = new BeanHonor();
                                    currentHonor.setG_name(object.getString("g_name"));
                                    currentHonor.setC_name(object.getString("c_name"));
                                    currentHonor.setStu_name(object.getString("stu_name"));
                                    currentHonor.setCreate_time(object.getString("create_time"));
                                    currentHonor.setReward_type(object.getString("reward_type"));
                                    currentHonor.setReward_details(object.getString("reward_details"));
                                    initData();
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                }
                                break;
                            default:
                                ToastUtils.showToast(HonorDetailActivity.this, "获取荣誉失败!");
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

    @OnClick(R.id.btnDelete)
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                VolleyHttpService.getInstance().sendPostRequest(HttpAction.ACTION_LOGIN,
                        new MyVolleyHttpParamsEntity(), new MyVolleyRequestListener() {
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
                break;
        }
    }


}
