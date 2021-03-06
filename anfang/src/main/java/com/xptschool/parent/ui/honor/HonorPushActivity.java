package com.xptschool.parent.ui.honor;

import android.graphics.drawable.BitmapDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanClass;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.mine.StudentAdapter;
import com.xptschool.parent.ui.mine.StudentPopupWindowView;
import com.xptschool.parent.util.TeacherUtil;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 老师发布荣誉
 */
public class HonorPushActivity extends BaseActivity {


    @BindView(R.id.flTransparent)
    FrameLayout flTransparent;

    @BindView(R.id.spnClasses)
    MaterialSpinner spnClasses;

    @BindView(R.id.spnHonorType)
    MaterialSpinner spnHonorType;

    @BindView(R.id.txtStudent)
    TextView txtStudent;
    @BindView(R.id.edtContent)
    EditText edtContent;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    private BeanStudent currentStudent;
    private BeanClass currentClass;
    PopupWindow studentPopup;
    StudentPopupWindowView studentPopupWindowView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_honor_push);

        setTitle(R.string.title_honor_push);

        initView();
        initData();

    }

    private void initView() {


    }

    private void initData() {
        spnClasses.setItems(GreenDaoHelper.getInstance().getAllClassNameAppend());
        spnClasses.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanClass>() {
            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, BeanClass item) {
                currentClass = item;
            }
        });
        spnHonorType.setItems(TeacherUtil.honorType);
    }

    @OnClick({R.id.txtStudent, R.id.btnSubmit})
    void onViewClick(View view) {
        switch (view.getId()) {
            case R.id.txtStudent:
                if (currentClass == null || currentClass.getC_id().isEmpty() || currentClass.getG_id().isEmpty()) {
                    ToastUtils.showToast(this, "请先选择班级");
                    return;
                }
                getStudents();
                break;
            case R.id.btnSubmit:
                if (currentClass == null || currentClass.getC_id().isEmpty() || currentClass.getG_id().isEmpty()) {
                    ToastUtils.showToast(this, "请先选择班级");
                    return;
                }
                if (currentStudent == null) {
                    ToastUtils.showToast(this, R.string.hint_choose_student);
                    return;
                }
                //荣誉类型
                if (spnHonorType.getSelectedIndex() == 0) {
                    ToastUtils.showToast(this, R.string.toast_honor_type_empty);
                    return;
                }
                String content = edtContent.getText().toString().trim();
                if (content.isEmpty()) {
                    ToastUtils.showToast(this, R.string.hint_honor_content);
                    return;
                }

                addHonor(content);
                break;
        }
    }

    private void getStudents() {
        Log.i(TAG, "getStudentByClass: ");
        if (studentPopupWindowView == null) {
            studentPopupWindowView = new StudentPopupWindowView(this);
        }
        studentPopupWindowView.getStudentByClass((BeanClass) spnClasses.getSelectedItem());
        studentPopupWindowView.setMyGridViewClickListener(new StudentAdapter.MyGridViewClickListener() {
            @Override
            public void onGridViewItemClick(BeanStudent student) {
                studentPopup.dismiss();
                if (student == null) {
                    return;
                }
                txtStudent.setText(student.getStu_name());
                //根据学生id获取学生位置
                if (student != currentStudent) {
                    currentStudent = student;
                }
            }
        });

        if (studentPopup == null) {
            studentPopup = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT,
                    XPTApplication.getInstance().getWindowHeight() / 2);
            studentPopup.setFocusable(true);
            studentPopup.setTouchable(true);
            studentPopup.setBackgroundDrawable(new BitmapDrawable());
            studentPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    flTransparent.setVisibility(View.GONE);
//                    txtStudentName.collapse();
                }
            });
        }

        studentPopup.setContentView(studentPopupWindowView);

        flTransparent.setVisibility(View.VISIBLE);
        studentPopup.showAtLocation(txtStudent, Gravity.BOTTOM, 0, 0);
    }

    private void addHonor(String content) {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Honor_edit,
                new MyVolleyHttpParamsEntity()
                        .addParam("stu_id", currentStudent.getStu_id())
                        .addParam("s_id", GreenDaoHelper.getInstance().getCurrentTeacher().getS_id())
                        .addParam("a_id", GreenDaoHelper.getInstance().getCurrentTeacher().getA_id())
                        .addParam("g_id", currentClass.getG_id())
                        .addParam("c_id", currentClass.getC_id())
                        .addParam("reward_type", spnHonorType.getSelectedIndex() + "")
                        .addParam("reward_details", content)
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Honor_edit))
                , new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        showProgress("正在发布荣誉...");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        hideProgress();
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                ToastUtils.showToast(HonorPushActivity.this, "发布成功");
                                finish();
                                break;
                            default:
                                ToastUtils.showToast(HonorPushActivity.this, "发布失败");
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        hideProgress();
                        ToastUtils.showToast(HonorPushActivity.this, "发布失败");
                    }
                });
    }

}
