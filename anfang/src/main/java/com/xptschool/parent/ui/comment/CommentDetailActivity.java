package com.xptschool.parent.ui.comment;

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
import com.xptschool.parent.bean.BeanComment;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;
import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.OnClick;

//评语详情
public class CommentDetailActivity extends BaseActivity {

    @BindView(R.id.txtClassName)
    TextView txtClassName;
    @BindView(R.id.txtStudent)
    TextView txtStudent;
    @BindView(R.id.txtTime)
    TextView txtTime;
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

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                currentComment = bundle.getParcelable(ExtraKey.COMMENT_DETAIL);
                if (currentComment != null) {
                    initData();
                }
                String id = bundle.getString(ExtraKey.DETAIL_ID);
                if (id != null) {
                    getCommentDetail(id);
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
                getCommentDetail(id);
            }
        }
    }

    private void initData() {
        txtClassName.setText(currentComment.getG_name() + currentComment.getC_name());
        txtStudent.setText(currentComment.getStu_name());
        txtTime.setText(currentComment.getCreate_time());
        txtType.setText(currentComment.getR_type());
        txtContent.setText(currentComment.getContent());

        btnDelete.setVisibility(View.GONE);

//        if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())
//                && currentComment.getUser_id().equals(GreenDaoHelper.getInstance().getCurrentTeacher().getU_id())) {
//            btnDelete.setVisibility(View.VISIBLE);
//        }
    }

    private void getCommentDetail(String id) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Remark_detail,
                new MyVolleyHttpParamsEntity().addParam("id", id), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在获取评语...");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                                    currentComment = new BeanComment();
                                    currentComment.setG_name(object.getString("g_name"));
                                    currentComment.setC_name(object.getString("c_name"));
                                    currentComment.setStu_name(object.getString("stu_name"));
                                    currentComment.setCreate_time(object.getString("create_time"));
                                    currentComment.setR_type(object.getString("r_type"));
                                    currentComment.setContent(object.getString("content"));
                                    initData();
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                }
                                break;
                            default:
                                ToastUtils.showToast(CommentDetailActivity.this, "获取评语失败!");
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
