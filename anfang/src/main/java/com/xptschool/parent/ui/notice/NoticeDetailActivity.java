package com.xptschool.parent.ui.notice;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanClassInfo;
import com.xptschool.parent.bean.BeanNotice;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;

public class NoticeDetailActivity extends BaseActivity {

    @BindView(R.id.txtClassName)
    TextView txtClassName;

    @BindView(R.id.txtTime)
    TextView txtTime;

    @BindView(R.id.txtNoticeTitle)
    TextView txtNoticeTitle;

    @BindView(R.id.txtContent)
    TextView txtContent;

    private BeanNotice currentNotice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail);

        setTitle(R.string.home_notice);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                currentNotice = bundle.getParcelable(ExtraKey.NOTICE_DETAIL);
                if (currentNotice != null) {
                    initData();
                }
                String id = bundle.getString(ExtraKey.DETAIL_ID);
                Log.i(TAG, "onCreate: " + id);
                if (id != null && !id.isEmpty()) {
                    getNoticeDetail(id);
                }
            } catch (Exception ex) {
                Log.e(TAG, "onCreate: bundle error " + ex.getMessage());
            }
        }
    }

    private void initData() {
        if (currentNotice != null) {
            List<BeanClassInfo> classInfos = currentNotice.getClassInfo();
            if (classInfos.size() > 0) {
                BeanClassInfo classInfo = classInfos.get(0);
                String className = classInfo.getG_name() + classInfo.getC_name();
                if (classInfos.size() > 1) {
                    className += "...";
                }
                txtClassName.setText(className);
            } else {
                txtClassName.setVisibility(View.GONE);
            }

            txtNoticeTitle.setText(currentNotice.getTitle());
            txtTime.setText(currentNotice.getCreate_time());
            txtContent.setText(currentNotice.getContent());
        }
    }

    private void getNoticeDetail(String id) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.NOTICE_DETAIL, new VolleyHttpParamsEntity()
                .addParam("id", id), new MyVolleyRequestListener() {
            @Override
            public void onStart() {
                super.onStart();
                showProgress("正在获取公告信息...");
            }

            @Override
            public void onResponse(VolleyHttpResult volleyHttpResult) {
                super.onResponse(volleyHttpResult);
                hideProgress();
                switch (volleyHttpResult.getStatus()) {
                    case HttpAction.SUCCESS:
                        try {
                            currentNotice = new BeanNotice();
                            JSONObject object = new JSONObject(volleyHttpResult.getData().toString());

                            currentNotice.setC_id(object.getString("c_id"));
                            currentNotice.setTitle(object.getString("title"));
                            currentNotice.setContent(object.getString("content"));
                            currentNotice.setCreate_time(object.getString("create_time"));

                            List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
                            for (int i = 0; i < students.size(); i++) {
                                BeanStudent student = students.get(i);
                                if (student.getC_id().equals(currentNotice.getC_id())) {
                                    txtClassName.setText(student.getG_name() + student.getC_name());
                                    break;
                                }
                            }

                            txtNoticeTitle.setText(currentNotice.getTitle());
                            txtTime.setText(currentNotice.getCreate_time());
                            txtContent.setText(currentNotice.getContent());

                        } catch (Exception ex) {
                            Log.i(TAG, "onResponse: " + ex.getMessage());
                        }
                        break;
                    default:
                        ToastUtils.showToast(NoticeDetailActivity.this, "公告获取失败");
                        break;
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
            }
        });
    }

}
