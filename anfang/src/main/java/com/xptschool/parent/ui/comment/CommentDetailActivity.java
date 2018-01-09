package com.xptschool.parent.ui.comment;

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
import com.xptschool.parent.bean.BeanComment;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;

import butterknife.BindView;
import butterknife.OnClick;

//评语详情
public class CommentDetailActivity extends BaseActivity {

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

    private BeanComment currentComment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_comment_detail);
        setTitle(R.string.title_comment_detail);

        try {
            Bundle bundle = getIntent().getExtras();
            currentComment = bundle.getParcelable(ExtraKey.COMMENT_DETAIL);
        } catch (Exception ex) {
            Log.i(TAG, "onCreate: get bundle data error " + ex.getMessage());
        }

        if (currentComment == null) {
            finish();
            return;
        }
        initData();
    }

    private void initData() {
        txtClassName.setText(currentComment.getG_name() + currentComment.getC_name());
        txtStudent.setText(currentComment.getStu_name());
        txtType.setText(currentComment.getR_type());
        txtContent.setText(currentComment.getContent());

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
