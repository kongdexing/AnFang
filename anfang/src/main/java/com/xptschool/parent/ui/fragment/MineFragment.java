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

import com.android.widget.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.login.LoginActivity;
import com.xptschool.parent.ui.mine.MyClassesActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.ui.mine.MyInviteActivity;
import com.xptschool.parent.ui.setting.SettingActivity;
import com.xptschool.parent.ui.watch.DevicesManageActivity;
import com.xptschool.parent.view.CustomDialog;

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
    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.txtRole)
    TextView txtRole;

    @BindView(R.id.rlMyClass)
    RelativeLayout rlMyClass;
    @BindView(R.id.rlMyChild)
    RelativeLayout rlMyChild;

//    @BindView(R.id.rlMyInvite)
//    RelativeLayout rlMyInvite;

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
        rlMyChild.setVisibility(View.GONE);
        rlMyClass.setVisibility(View.GONE);
//        rlMyInvite.setVisibility(View.GONE);

        //判断登录状态
        if (XPTApplication.getInstance().isLoggedIn()) {

            ll_unlogin.setVisibility(View.GONE);
            ll_login.setVisibility(View.VISIBLE);

            rlMyChild.setVisibility(View.VISIBLE);
            String headImg = "";
            BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
            if (parent != null) {
                txtPhone.setText(parent.getPhone());
                headImg = parent.getHead_portrait();
            }

            ImageLoader.getInstance().displayImage(headImg,
                    new ImageViewAware(imgLoginHead), CommonUtil.getDefaultUserImageLoaderOption());
        } else {
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

    @OnClick({R.id.imgHead, R.id.txtToLogin, R.id.ll_login, R.id.rlMyChild, R.id.rlMyInvite, R.id.rlMyClass,
            R.id.rlSetting})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rlMyClass:
            case R.id.rlMyChild:
            case R.id.rlMyInvite:
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
                //进入设备管理界面
                startActivity(new Intent(getContext(), DevicesManageActivity.class));
                break;
            case R.id.rlMyClass:
                startActivity(new Intent(getContext(), MyClassesActivity.class));
                break;
            case R.id.rlMyInvite:
                Intent intent = new Intent(getContext(), MyInviteActivity.class);
                intent.putExtra("user_id", XPTApplication.getInstance().getCurrentUserId());
                startActivity(intent);
                break;
            case R.id.rlSetting:
                startActivity(new Intent(getContext(), SettingActivity.class));
                break;
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
