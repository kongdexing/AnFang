package com.xptschool.parent.ui.leave;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanLeave;
import com.xptschool.parent.common.ActivityResultCode;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import org.json.JSONObject;

import butterknife.BindView;
import butterknife.OnClick;

//老师端审批请假
public class LeaveTDetailActivity extends BaseActivity {

    @BindView(R.id.txtClassName)
    TextView txtClassName;

    @BindView(R.id.txtStudentName)
    TextView txtStudentName;

    @BindView(R.id.txtTeacher)
    TextView txtTeacher;

    @BindView(R.id.txtType)
    TextView txtType;

    @BindView(R.id.txtStatus)
    TextView txtStatus;

    @BindView(R.id.txtSTime)
    TextView txtSTime;

    @BindView(R.id.txtETime)
    TextView txtETime;

    @BindView(R.id.txtLeave)
    TextView txtLeave;

    @BindView(R.id.edtReply)
    EditText edtReply;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;
    @BindView(R.id.btnRebut)
    Button btnRebut;
    private BeanLeave currentLeave;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_t_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            try {
                currentLeave = bundle.getParcelable(ExtraKey.LEAVE_DETAIL);
                if (currentLeave != null) {
                    initData();
                }
                String id = bundle.getString(ExtraKey.DETAIL_ID);
                Log.i(TAG, "onCreate: " + id);
                if (id != null && !id.isEmpty()) {
                    getLeaveDetail(id);
                }
            } catch (Exception ex) {
                Log.e(TAG, "onCreate: " + ex.getMessage());
            }
        }
    }

    private void initData() {
        txtClassName.setText(GreenDaoHelper.getInstance().getClassNameById(currentLeave.getC_id()));
        txtStudentName.setText(currentLeave.getStu_name());
        txtType.setText(currentLeave.getLeave_name());
        txtStatus.setText(currentLeave.getStatus_name());
        if (currentLeave.getStatus().equals("0")) {
            btnSubmit.setVisibility(View.VISIBLE);
            btnRebut.setVisibility(View.VISIBLE);
        } else {
            btnSubmit.setVisibility(View.GONE);
            btnRebut.setVisibility(View.GONE);
            edtReply.setEnabled(false);
            edtReply.clearFocus();
        }
        txtSTime.setText(currentLeave.getStart_time());
        txtETime.setText(currentLeave.getEnd_time());
        txtLeave.setText(currentLeave.getLeave_memo());
        edtReply.setText(currentLeave.getReply());
    }

    @OnClick({R.id.btnSubmit, R.id.btnRebut})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                currentLeave.setStatus("1");
                currentLeave.setReply(edtReply.getText().toString().trim());
                putLeaveStatus();
                break;
            case R.id.btnRebut:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle("请假条");
                dialog.setMessage("确定要驳回【" + currentLeave.getStu_name() + "】的请假条吗？");
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        currentLeave.setStatus("2");
                        currentLeave.setReply(edtReply.getText().toString().trim());
                        putLeaveStatus();
                    }
                });
                break;
        }
    }

    private void getLeaveDetail(String id) {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Leave_Detail,
                new VolleyHttpParamsEntity().addParam("id", id), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在获取请假信息...");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                                    currentLeave = new BeanLeave();
                                    currentLeave.setId(object.getString("id"));
                                    currentLeave.setG_id(object.getString("g_id"));
                                    currentLeave.setG_name(object.getString("g_name"));
                                    currentLeave.setC_id(object.getString("c_id"));
                                    currentLeave.setC_name(object.getString("c_name"));
                                    currentLeave.setStu_name(object.getString("stu_name"));
                                    currentLeave.setLeave_name(object.getString("leave_name"));
                                    currentLeave.setStatus(object.getString("status"));
                                    currentLeave.setStatus_name(object.getString("status_name"));
                                    currentLeave.setStart_time(object.getString("start_time"));
                                    currentLeave.setEnd_time(object.getString("end_time"));
                                    currentLeave.setLeave_memo(object.getString("leave_memo"));
                                    initData();
                                } catch (Exception ex) {
                                    ToastUtils.showToast(LeaveTDetailActivity.this, "获取请假信息失败!");
                                }
                                break;
                            default:
                                ToastUtils.showToast(LeaveTDetailActivity.this, "获取请假信息失败!");
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

    private void putLeaveStatus() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Leave_Edit, new VolleyHttpParamsEntity()
                        .addParam("status", currentLeave.getStatus())
                        .addParam("reply", currentLeave.getReply())
                        .addParam("id", currentLeave.getId())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Leave_Edit)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        showProgress(R.string.progress_loading_cn);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        Toast.makeText(LeaveTDetailActivity.this, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            Intent intent = new Intent();
                            intent.putExtra(ExtraKey.LEAVE_DETAIL, currentLeave);
                            setResult(ActivityResultCode.Leave_Edit, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                    }
                });
    }

}
