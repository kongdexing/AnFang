package com.shuhai.anfang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.model.BeanParent;
import com.shuhai.anfang.model.BeanTeacher;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.login.LoginActivity;
import com.shuhai.anfang.ui.mine.MyInfoActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import butterknife.Unbinder;

public class MineFragment extends BaseFragment {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.txtUserName)
    TextView txtUserName;

    @BindView(R.id.txtChangeAccount)
    TextView txtMineInfo;

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
        if (rlMyChild == null || txtMineInfo == null) {
            return;
        }

        //判断登录状态
        if (XPTApplication.getInstance().isLoggedIn()) {
            txtMineInfo.setText(getString(R.string.mine_change_account));

            if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                rlMyChild.setVisibility(View.GONE);
                rlMyClass.setVisibility(View.VISIBLE);

                BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
                if (teacher != null) {
                    txtUserName.setText(teacher.getName());
                    if (teacher.getSex().equals("1")) {
                        imgHead.setImageResource(R.drawable.teacher_man);
                    } else {
                        imgHead.setImageResource(R.drawable.teacher_woman);
                    }
                }
            } else if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                rlMyChild.setVisibility(View.VISIBLE);
                rlMyClass.setVisibility(View.GONE);

                BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
                if (parent != null) {
                    txtUserName.setText(parent.getParent_name());
                    if (parent.getSex().equals("1")) {
                        imgHead.setImageResource(R.drawable.parent_father);
                    } else {
                        imgHead.setImageResource(R.drawable.parent_mother);
                    }
                }
            }
        } else {
            rlMyChild.setVisibility(View.VISIBLE);
            rlMyClass.setVisibility(View.VISIBLE);
            txtMineInfo.setText(getString(R.string.mine_unlogin));
        }
    }

    @Override
    public void onPause() {
        super.onPause();
    }

    @Override
    protected void initData() {

    }

    @OnClick({R.id.imgHead, R.id.txtChangeAccount, R.id.rlMyChild,
            R.id.rlMyCourse, R.id.rlQRCode})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.imgHead:
            case R.id.txtChangeAccount:
                //登录？进入个人信息：登录页面
                if (XPTApplication.getInstance().isLoggedIn()) {
                    startActivity(new Intent(getContext(), MyInfoActivity.class));
                } else {
                    startActivity(new Intent(getContext(), LoginActivity.class));
                }
                break;
//            case R.id.rlMyChild:
//                startActivity(new Intent(getContext(), MyChildActivity.class));
//                break;
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
//            case R.id.rlSetting:
//                startActivity(new Intent(getContext(), SettingActivity.class));
//                break;
            case R.id.rlQRCode:

                break;
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
