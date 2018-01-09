package com.xptschool.parent.ui.honor;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanHonor;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;

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

        try {
            Bundle bundle = getIntent().getExtras();
            currentHonor = bundle.getParcelable(ExtraKey.HONOR_DETAIL);
        } catch (Exception ex) {
            Log.i(TAG, "onCreate: get bundle data error " + ex.getMessage());
        }

        if (currentHonor == null) {
            finish();
            return;
        }
        initData();
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

    @OnClick(R.id.btnDelete)
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.btnDelete:
                VolleyHttpService.getInstance().sendPostRequest(HttpAction.ACTION_LOGIN,
                        new VolleyHttpParamsEntity(), new MyVolleyRequestListener() {
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
