package com.shuhai.anfang.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.common.UserHelper;
import com.shuhai.anfang.model.BeanParent;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.view.CustomDialog;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dexing on 2017-11-29 0029.
 */

public class PInfoView extends LinearLayout {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.txtMineName)
    TextView txtMineName;

    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.txtEmail)
    TextView txtEmail;

    @BindView(R.id.txtHomeAdd)
    TextView txtHomeAdd;

    @BindView(R.id.txtHomeTel)
    TextView txtHomeTel;

    Context mContext;

    public PInfoView(Context context) {
        this(context, null);
    }

    public PInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        View view = LayoutInflater.from(context).inflate(R.layout.activity_my_info_parent, this, true);
        ButterKnife.bind(view);
        initData();
    }

    private void initData() {

        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent != null) {
            if (parent.getSex().equals("1")) {
                imgHead.setImageResource(R.drawable.parent_father);
            } else {
                imgHead.setImageResource(R.drawable.parent_mother);
            }

            txtMineName.setText(parent.getParent_name());
            txtPhone.setText(parent.getParent_phone());
            txtEmail.setText(parent.getEmail());
            txtHomeAdd.setText(parent.getAddress());
            txtHomeTel.setText(parent.getFamily_tel());
        }
    }

    @OnClick({R.id.rlMinePhoto, R.id.rlMinePhone, R.id.rlAddressPhone, R.id.rlExit})
    void viewClick(View view) {
        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlMinePhone:
                if (parent.getParent_phone().isEmpty()) {
                    Toast.makeText(mContext, R.string.toast_phone_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + parent.getParent_phone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(mContext, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlAddressPhone:
                if (parent.getFamily_tel().isEmpty()) {
                    Toast.makeText(mContext, R.string.toast_phone_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + parent.getFamily_tel()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(mContext, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
                }
                break;
            case R.id.rlExit:
                CustomDialog dialog = new CustomDialog(mContext);
                dialog.setTitle(R.string.label_tip);
                dialog.setMessage(R.string.msg_exit);
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        //清除数据
                        SharedPreferencesUtil.clearUserInfo(mContext);
                        GreenDaoHelper.getInstance().clearData();
                        UserHelper.getInstance().userExit();
                        ((MyInfoActivity)mContext).finish();
                    }
                });
                break;
        }
    }
}
