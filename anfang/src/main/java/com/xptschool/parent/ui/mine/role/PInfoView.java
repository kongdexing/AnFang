package com.xptschool.parent.ui.mine.role;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.mine.BaseInfoView;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dexing on 2017-11-29 0029.
 */

public class PInfoView extends BaseUserView {

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

    protected void initData() {

        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent != null) {
            Log.i(TAG, "initData: "+parent.getHead_portrait());
            ImageLoader.getInstance().displayImage(parent.getHead_portrait(),
                    new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());

            txtMineName.setText(parent.getParent_name());
            txtPhone.setText(parent.getParent_phone());
            txtEmail.setText(parent.getEmail());
            txtHomeAdd.setText(parent.getAddress());
            txtHomeTel.setText(parent.getFamily_tel());
        }
    }

    @OnClick({R.id.rlName, R.id.rlMail, R.id.rlSex, R.id.rlMinePhoto, R.id.rlMinePhone, R.id.rlAddressPhone})
    void viewClick(View view) {
        BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
        if (parent == null) {
            return;
        }
        switch (view.getId()) {
//            case R.id.rlName:
//                changeName(parent.getParent_name());
//                break;
            case R.id.rlMinePhoto:
                choosePic(imgHead);
                break;
//            case R.id.rlMail:
//                changeEmail(parent.getEmail());
//                break;
//            case R.id.rlSex:
//                changeSex(parent.getSex());
//                break;
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
        }
    }
}
