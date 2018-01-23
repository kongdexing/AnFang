package com.xptschool.parent.ui.leave;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanLeave;
import com.xptschool.parent.common.ActivityResultCode;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.SpinnerTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;
import com.xptschool.parent.view.TimePickerPopupWindow;

import org.json.JSONObject;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 家长发布请假
 */
public class LeavePDetailActivity extends BaseActivity {

    @BindView(R.id.spnTeacher)
    MaterialSpinner spnTeacher;

    @BindView(R.id.spnStudents)
    MaterialSpinner spnStudents;

    @BindView(R.id.spnLeaveType)
    MaterialSpinner spnLeaveType;

    @BindView(R.id.txtStatus)
    TextView txtStatus;

    @BindView(R.id.txtSTime)
    TextView txtSTime;

    @BindView(R.id.txtETime)
    TextView txtETime;

    @BindView(R.id.txtLeave)
    EditText edtLeave;

    @BindView(R.id.edtReply)
    EditText edtReply;

    @BindView(R.id.llLeaveStatus)
    LinearLayout llLeaveStatus;
    @BindView(R.id.rlLeaveReply)
    RelativeLayout rlLeaveReply;

    @BindView(R.id.btnDelete)
    Button btnDelete;
    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    private BeanLeave currentLeave;
    private String[] leaveTypes = new String[]{"事假", "病假", "其他"};
    private TimePickerPopupWindow pushDate, completeDate;
    private boolean canModify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_leave_detail);
        setTitle(R.string.label_leave);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentLeave = bundle.getParcelable(ExtraKey.LEAVE_DETAIL);
            initData();
            String id = bundle.getString(ExtraKey.DETAIL_ID);
            Log.i(TAG, "onCreate: " + id);
            if (id != null && !id.isEmpty()) {
                getLeaveDetail(id);
            }
        } else {
            initData();
        }

        //华为机型推送使用uri传值
        Uri uri = getIntent().getData();
        if (uri != null) {
            String id = uri.getQueryParameter("id");
            if (id != null && !id.isEmpty()) {
                getLeaveDetail(id);
            }
        }

        initView();
    }

    private void initView() {
        //学生
        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        spnStudents.setItems(students);
        if (currentLeave != null) {
            spnStudents.setEnabled(false);

            for (int i = 0; i < students.size(); i++) {
                if (students.get(i).getStu_id().equals(currentLeave.getStu_id())) {
                    spnStudents.setSelectedIndex(i);
                    break;
                }
            }

        }
        spnStudents.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener() {
            @Override
            public void onItemSelected(MaterialSpinner materialSpinner, int i, long l, Object o) {
                getTeacherByStudent();
            }
        });

        //发布请假时获取老师信息
        if (currentLeave == null) {
            //老师及请假类型
            spnTeacher.setItems("正在获取老师信息");
            getTeacherByStudent();
            spnTeacher.setEnabled(false);
        }
        spnLeaveType.setItems(leaveTypes);
    }

    private void initData() {

        if (currentLeave != null) {
            //如果已批准，则不能编辑
            if (currentLeave.getStatus().equals("0")) {
                setTxtRight("编辑");
                setTextRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setViewEnable(true);
                        btnSubmit.setVisibility(View.VISIBLE);
                        getTeacherByStudent();
                        setTxtRight("");
                    }
                });
            }

            if (currentLeave.getLeave_type().equals("1")) {
                spnLeaveType.setSelectedIndex(0);
            } else if (currentLeave.getLeave_type().equals("2")) {
                spnLeaveType.setSelectedIndex(1);
            } else {
                spnLeaveType.setSelectedIndex(2);
            }
            txtStatus.setText(currentLeave.getStatus_name());
            spnTeacher.setText(currentLeave.getT_name());

            if (currentLeave.getStatus_name().equals("已提交")) {
                btnDelete.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                btnSubmit.setText(R.string.label_leave_modify);
                rlLeaveReply.setVisibility(View.GONE);
            } else {
                btnDelete.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
            }

            txtSTime.setText(currentLeave.getStart_time());
            txtETime.setText(currentLeave.getEnd_time());
            edtLeave.setText(currentLeave.getLeave_memo());
            edtReply.setText(currentLeave.getReply());
            edtLeave.setSelection(edtLeave.getText().toString().length());
        }

        if (currentLeave == null) {
            llLeaveStatus.setVisibility(View.GONE);
            rlLeaveReply.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            txtSTime.setText(CommonUtil.dateToStr(CommonUtil.getDateBefore(0)));
            txtETime.setText(CommonUtil.dateToStr(CommonUtil.getDateBefore(-1)));
            setViewEnable(true);
        } else {
            setViewEnable(false);
        }
    }

    private void setViewEnable(boolean enable) {
        canModify = enable;
        spnStudents.setEnabled(enable);
        spnTeacher.setEnabled(enable);
        spnLeaveType.setEnabled(enable);
        txtSTime.setEnabled(enable);
        txtETime.setEnabled(enable);
        edtLeave.setEnabled(enable);
        edtReply.setEnabled(enable);
    }

    @OnClick({R.id.btnSubmit, R.id.btnDelete, R.id.txtSTime, R.id.txtETime})
    void viewOnClick(View view) {
        switch (view.getId()) {
            case R.id.txtSTime:
                if (pushDate == null) {
                    pushDate = new TimePickerPopupWindow(LeavePDetailActivity.this, txtSTime.getText().toString(),
                            new TimePickerPopupWindow.OnTimePickerClickListener() {

                                @Override
                                public void onTimePickerResult(String result) {
                                    if (!result.isEmpty())
                                        txtSTime.setText(result);
                                }
                            });
                    pushDate.setTouchable(true);
                    pushDate.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    pushDate.setBackgroundDrawable(new ColorDrawable());
                    pushDate.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1.0f);
                        }
                    });
                }
                backgroundAlpha(0.5f);
                pushDate.showAtLocation(txtSTime, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.txtETime:
                if (completeDate == null) {
                    completeDate = new TimePickerPopupWindow(LeavePDetailActivity.this, txtETime.getText().toString(),
                            new TimePickerPopupWindow.OnTimePickerClickListener() {

                                @Override
                                public void onTimePickerResult(String result) {
                                    if (!result.isEmpty())
                                        txtETime.setText(result);
                                }
                            });
                    completeDate.setTouchable(true);
                    completeDate.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    completeDate.setBackgroundDrawable(new ColorDrawable());
                    completeDate.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1.0f);
                        }
                    });
                }
                backgroundAlpha(0.5f);
                completeDate.showAtLocation(txtETime, Gravity.BOTTOM, 0, 0);
                break;
            case R.id.btnSubmit:
                if (edtLeave.getText().toString().trim().isEmpty()) {
                    Toast.makeText(this, R.string.toast_leave_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                putLeaveStatus();
                break;
            case R.id.btnDelete:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle(R.string.home_leave);
                dialog.setMessage(R.string.msg_delete_leave);
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        deleteLeave();
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
                                    currentLeave.setReply(object.getString("reply"));

                                    spnStudents.setText(currentLeave.getStu_name());
                                    spnTeacher.setText(currentLeave.getT_name());
                                    spnLeaveType.setText(currentLeave.getLeave_name());
                                    txtStatus.setText(currentLeave.getStatus_name());
                                    txtSTime.setText(currentLeave.getStart_time());
                                    txtETime.setText(currentLeave.getEnd_time());
                                    edtLeave.setText(currentLeave.getLeave_memo());
                                    edtReply.setText(currentLeave.getReply());
                                    setViewEnable(false);
                                    btnSubmit.setVisibility(View.GONE);
                                    btnDelete.setVisibility(View.GONE);

                                } catch (Exception ex) {
                                    ToastUtils.showToast(LeavePDetailActivity.this, "获取请假信息失败!");
                                }
                                break;
                            default:
                                ToastUtils.showToast(LeavePDetailActivity.this, "获取请假信息失败!");
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
        BeanStudent student = (BeanStudent) spnStudents.getSelectedItem();
        SpinnerTeacher teacher = (SpinnerTeacher) spnTeacher.getSelectedItem();
        String type = (String) spnLeaveType.getSelectedItem();
        int leave_type = 1;
        if (type.equals("事假")) {
            leave_type = 1;
        } else if (type.equals("病假")) {
            leave_type = 2;
        } else {
            leave_type = 0;
        }

        if (currentLeave != null) {
            currentLeave.setStu_id(student.getStu_id());
            currentLeave.setStu_name(student.getStu_name());
            currentLeave.setT_name(teacher.getTeacherName());
            currentLeave.setT_id(teacher.getT_id());
            currentLeave.setLeave_memo(edtLeave.getText().toString().trim());
            currentLeave.setLeave_type(leave_type + "");
            currentLeave.setStart_time(txtSTime.getText().toString());
            currentLeave.setEnd_time(txtETime.getText().toString());
        }

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Leave_Add, new VolleyHttpParamsEntity()
                        .addParam("id", currentLeave == null ? "0" : currentLeave.getId())
                        .addParam("stu_id", student.getStu_id())
                        .addParam("t_id", teacher.getT_id())
                        .addParam("leave_memo", edtLeave.getText().toString().trim())
                        .addParam("leave_type", leave_type + "")
                        .addParam("start_time", txtSTime.getText().toString())
                        .addParam("end_time", txtETime.getText().toString())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Leave_Add)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        showProgress(R.string.progress_loading_cn);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        Toast.makeText(LeavePDetailActivity.this, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            if (currentLeave != null) {
                                Intent intent = new Intent();
                                intent.putExtra(ExtraKey.LEAVE_DETAIL, currentLeave);
                                setResult(ActivityResultCode.Leave_Edit, intent);
                            }
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

    private void getTeacherByStudent() {
        BeanStudent student = (BeanStudent) spnStudents.getSelectedItem();

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GET_TEACHER_BYCID,
                new VolleyHttpParamsEntity()
                        .addParam("c_id", student.getC_id())
                        .addParam("g_id", student.getG_id()),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    Gson gson = new Gson();
                                    List<SpinnerTeacher> teachers = gson.fromJson(volleyHttpResult.getData().toString(), new TypeToken<List<SpinnerTeacher>>() {
                                    }.getType());
                                    spnTeacher.setItems(teachers);
                                    if (teachers.size() == 1) {
                                        spnTeacher.setEnabled(false);
                                    } else {
                                        spnTeacher.setEnabled(true);
                                    }
                                    if (!canModify && currentLeave != null) {
                                        spnTeacher.setEnabled(false);
                                        for (int i = 0; i < teachers.size(); i++) {
                                            if (teachers.get(i).getT_id().equals(currentLeave.getT_id())) {
                                                spnTeacher.setSelectedIndex(i);
                                                break;
                                            }
                                        }
                                    }
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: " + ex.getMessage());
                                }
                                break;
                            default:

                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {

                    }
                });
    }

    private void deleteLeave() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Leave_Del,
                new VolleyHttpParamsEntity()
                        .addParam("id", currentLeave.getId())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Leave_Del)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        showProgress(R.string.progress_loading_cn);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        Toast.makeText(LeavePDetailActivity.this, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            Intent intent = new Intent();
                            intent.putExtra(ExtraKey.LEAVE_DETAIL, currentLeave);
                            setResult(ActivityResultCode.Leave_Del, intent);
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
