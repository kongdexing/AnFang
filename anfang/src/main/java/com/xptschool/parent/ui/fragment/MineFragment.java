package com.xptschool.parent.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.BeanUser;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.login.LoginActivity;
import com.xptschool.parent.ui.mine.MyChildActivity;
import com.xptschool.parent.ui.mine.MyClassesActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.ui.mine.MyInviteActivity;
import com.xptschool.parent.ui.setting.DownloadQRCodeActivity;
import com.xptschool.parent.ui.setting.QRCodeActivity;
import com.xptschool.parent.ui.setting.SettingActivity;
import com.xptschool.parent.ui.wallet.WalletActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import java.util.List;

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
    @BindView(R.id.txtRole)
    TextView txtRole;

    @BindView(R.id.rlMyClass)
    RelativeLayout rlMyClass;
    @BindView(R.id.rlMyChild)
    RelativeLayout rlMyChild;

    //    @BindView(R.id.rlMyBill)
//    RelativeLayout rlMyBill;
    @BindView(R.id.rlMyProperty)
    RelativeLayout rlMyProperty;

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
        reloadMineUI();
    }

    private void reloadMineUI() {
        if (rlMyChild == null || ll_unlogin == null) {
            return;
        }
        //判断登录状态
        if (XPTApplication.getInstance().isLoggedIn()) {

            ll_unlogin.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);

            UserType type = XPTApplication.getInstance().getCurrent_user_type();
            if (type == null) {
                return;
            }
            String headImg = "";

            if (UserType.TEACHER.equals(type)) {
                rlMyChild.setVisibility(View.GONE);
                rlMyClass.setVisibility(View.VISIBLE);
//                rlMyBill.setVisibility(View.GONE);
                rlMyProperty.setVisibility(View.GONE);

                BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
                if (teacher != null) {
                    txtUserName.setText(teacher.getName());
                    txtPhone.setText("手机号：" + teacher.getPhone());
                    headImg = teacher.getHead_portrait();
                }
            } else if (UserType.PARENT.equals(type)) {
                rlMyChild.setVisibility(View.VISIBLE);

                LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlMyChild.getLayoutParams();
                params.setMargins(0, (int) mContext.getResources().getDimension(R.dimen.dp_20), 0, 0);
                rlMyChild.setLayoutParams(params);

                rlMyClass.setVisibility(View.GONE);
//                rlMyBill.setVisibility(View.VISIBLE);
                rlMyProperty.setVisibility(View.VISIBLE);

                BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
                if (parent != null) {
                    txtUserName.setText(parent.getParent_name());
                    txtPhone.setText("手机号：" + parent.getParent_phone());
                    headImg = parent.getHead_portrait();
                }
            } else if (UserType.VISITOR.equals(type) || UserType.COMPANY.equals(type) || UserType.PROXY.equals(type) || UserType.CITYPROXY.equals(type)) {
                rlMyClass.setVisibility(View.GONE);
                rlMyChild.setVisibility(View.GONE);
                rlMyProperty.setVisibility(View.GONE);
                BeanUser user = GreenDaoHelper.getInstance().getCurrentUser();
                if (user != null) {
                    headImg = user.getHead_portrait();
                    txtUserName.setText(user.getUsername());
                }
                txtPhone.setVisibility(View.GONE);
            }
            ImageLoader.getInstance().displayImage(headImg,
                    new ImageViewAware(imgLoginHead), CommonUtil.getDefaultImageLoaderOption());

            txtRole.setText(type.getRoleName());
        } else {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) rlMyChild.getLayoutParams();
            params.setMargins(0, (int) mContext.getResources().getDimension(R.dimen.dp_1), 0, 0);
            rlMyChild.setLayoutParams(params);

            rlMyChild.setVisibility(View.VISIBLE);
            rlMyClass.setVisibility(View.VISIBLE);
//            rlMyBill.setVisibility(View.VISIBLE);
            rlMyProperty.setVisibility(View.VISIBLE);
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

    @OnClick({R.id.imgHead, R.id.txtToLogin, R.id.ll_login, R.id.rlMyChild, R.id.rlMyInvite,
            R.id.rlMyBill, R.id.rlMyClass, R.id.rlMyProperty, R.id.rlSetting, R.id.rlQRCode, R.id.rlDownloadQRCode})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rlMyClass:
            case R.id.rlMyChild:
            case R.id.rlMyBill:
            case R.id.rlMyInvite:
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
            case R.id.rlMyInvite:
                startActivity(new Intent(getContext(), MyInviteActivity.class));
                break;
            case R.id.rlMyProperty:
                List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
                if (students.size() > 0) {
                    startActivity(new Intent(getContext(), WalletActivity.class));
                } else {
                    Toast.makeText(mContext, "暂无绑定的学生", Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlSetting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
            case R.id.rlQRCode:
                startActivity(new Intent(getContext(), QRCodeActivity.class));
                break;
            case R.id.rlDownloadQRCode:
                startActivity(new Intent(getContext(), DownloadQRCodeActivity.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
