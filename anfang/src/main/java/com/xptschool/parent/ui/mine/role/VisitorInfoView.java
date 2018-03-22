package com.xptschool.parent.ui.mine.role;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.mine.BaseInfoView;
import com.xptschool.parent.util.ToastUtils;

import org.w3c.dom.Text;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dexing on 2017-11-29 0029.
 */

public class VisitorInfoView extends BaseInfoView {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    @BindView(R.id.txtName)
    TextView txtName;

    @BindView(R.id.txtPhone)
    TextView txtPhone;
    @BindView(R.id.txtRole)
    TextView txtRole;

    public VisitorInfoView(Context context) {
        this(context, null);
    }

    public VisitorInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_my_info_visitor, this, true);
            ButterKnife.bind(view);
            initData();
        } catch (Exception ex) {
            ToastUtils.showToast(mContext, ex.getMessage());
        }
    }

    private void initData() {
        txtName.setText(SharedPreferencesUtil.getData(mContext, SharedPreferencesUtil.KEY_VISITOR_NAME, "").toString());
        txtPhone.setText(SharedPreferencesUtil.getData(mContext, SharedPreferencesUtil.KEY_USER_NAME, "").toString());
        txtRole.setText(XPTApplication.getInstance().getCurrent_user_type().getRoleName());
    }

    @OnClick({R.id.rlMinePhoto, R.id.rlMinePhone})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.rlMinePhone:
                String phone = SharedPreferencesUtil.getData(mContext, SharedPreferencesUtil.KEY_USER_NAME, "").toString();
                if (phone.isEmpty()) {
                    Toast.makeText(mContext, R.string.toast_phone_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + phone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(mContext, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
