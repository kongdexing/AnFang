package com.shuhai.anfang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.UserHelper;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.model.BeanParent;
import com.shuhai.anfang.model.BeanTeacher;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.login.LoginActivity;
import com.shuhai.anfang.ui.mine.MyChildActivity;
import com.shuhai.anfang.ui.mine.MyClassesActivity;
import com.shuhai.anfang.ui.mine.MyInfoActivity;
import com.shuhai.anfang.ui.setting.QRCodeActivity;
import com.shuhai.anfang.ui.setting.SettingActivity;
import com.shuhai.anfang.view.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment {

    @BindView(R.id.ll_unlogin)
    LinearLayout ll_unlogin;
    @BindView(R.id.ll_login)
    RelativeLayout ll_login;

    @BindView(R.id.imgLoginHead)
    CircularImageView imgLoginHead;
    @BindView(R.id.txtUserName)
    TextView txtUserName;
    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.rlMyClass)
    RelativeLayout rlMyClass;
    @BindView(R.id.rlMyChild)
    RelativeLayout rlMyChild;

    private Unbinder unbinder;

    public MineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (rlMyChild == null || ll_unlogin == null) {
            return;
        }
        reloadMineUI();
    }

    private void reloadMineUI() {
        //判断登录状态
        if (XPTApplication.getInstance().isLoggedIn()) {
            ll_unlogin.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);

            if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                rlMyChild.setVisibility(View.GONE);
                rlMyClass.setVisibility(View.VISIBLE);

                BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
                if (teacher != null) {
                    txtUserName.setText(teacher.getName());
                    txtPhone.setText("手机号：" + teacher.getPhone());
                    if (teacher.getSex().equals("1")) {
                        imgLoginHead.setImageResource(R.drawable.teacher_man);
                    } else {
                        imgLoginHead.setImageResource(R.drawable.teacher_woman);
                    }
                }
            } else if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                rlMyChild.setVisibility(View.VISIBLE);
                rlMyClass.setVisibility(View.GONE);

                BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
                if (parent != null) {
                    txtUserName.setText(parent.getParent_name());
                    txtPhone.setText("手机号：" + parent.getParent_phone());
                    if (parent.getSex().equals("1")) {
                        imgLoginHead.setImageResource(R.drawable.parent_father);
                    } else {
                        imgLoginHead.setImageResource(R.drawable.parent_mother);
                    }
                }
            }
        } else {
            rlMyChild.setVisibility(View.VISIBLE);
            rlMyClass.setVisibility(View.VISIBLE);

            ll_unlogin.setVisibility(View.VISIBLE);
            ll_login.setVisibility(View.GONE);
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initData() {
        UserHelper.getInstance().addUserChangeListener(new UserHelper.UserChangeListener() {
            @Override
            public void onUserLoginSuccess() {
                reloadMineUI();
            }

            @Override
            public void onUserExit() {
                reloadMineUI();
            }
        });
    }

    @OnClick({R.id.imgHead, R.id.txtToLogin, R.id.ll_login, R.id.rlMyChild,
            R.id.rlMyBill, R.id.rlMyClass, R.id.rlMyProperty, R.id.rlSetting, R.id.rlQRCode})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rlMyClass:
            case R.id.rlMyChild:
            case R.id.rlMyBill:
            case R.id.rlMyProperty:
                if (!XPTApplication.getInstance().isLoggedIn()) {
                    //弹出登录对话框
                    CustomDialog dialog = new CustomDialog(mContext);
                    dialog.setTitle(R.string.label_tip);
                    dialog.setMessage(R.string.message_tologin);
                    dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                        @Override
                        public void onPositiveClick() {
                            mContext.startActivity(new Intent(mContext, LoginActivity.class));
                        }
                    });
                    return;
                }
        }

        switch (view.getId()) {
            case R.id.imgHead:
            case R.id.txtToLogin:
                startActivity(new Intent(getContext(), LoginActivity.class));
                break;
            case R.id.ll_login:
                startActivity(new Intent(getContext(), MyInfoActivity.class));
                break;
            case R.id.rlMyChild:
                startActivity(new Intent(getContext(), MyChildActivity.class));
                break;
            case R.id.rlMyClass:
                startActivity(new Intent(getContext(), MyClassesActivity.class));
                break;
//            case R.id.rlMyCourse:
//                startActivity(new Intent(getContext(), CourseActivity.class));
//                break;
//            case R.id.rlMyWellet:
//                List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
//                if (students.size() > 0) {
//                    startActivity(new Intent(getContext(), WalletActivity.class));
//                } else {
//                    Toast.makeText(mContext, "暂无绑定的学生", Toast.LENGTH_SHORT).show();
//                }
//                break;
//            case R.id.txtChangeAccount:
//                startActivity(new Intent(getContext(), LoginActivity.class));
//                break;
//            case R.id.rlMyContacts:
//                startActivity(new Intent(getContext(), ContactsActivity.class));
//                break;
            case R.id.rlSetting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.rlQRCode:
                startActivity(new Intent(getContext(), QRCodeActivity.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
