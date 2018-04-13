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

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.util.ToastUtils;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by dexing on 2017-11-29 0029.
 */

public class TInfoView extends BaseUserView {

    @BindView(R.id.txtTeacherName)
    TextView txtTeacherName;

    @BindView(R.id.txtPhone)
    TextView txtPhone;

    @BindView(R.id.txtEducation)
    TextView txtEducation;

    @BindView(R.id.txtSchoolArea)
    TextView txtSchoolArea;

    @BindView(R.id.txtSchoolName)
    TextView txtSchoolName;

    @BindView(R.id.txtDepartmentName)
    TextView txtDepartmentName;

    @BindView(R.id.txtAdviser)
    TextView txtAdviser;

    public TInfoView(Context context) {
        this(context, null);
    }

    public TInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        try {
            View view = LayoutInflater.from(context).inflate(R.layout.activity_my_info_teacher, this, true);
            ButterKnife.bind(view);
            initData();
        } catch (Exception ex) {
            ToastUtils.showToast(mContext, ex.getMessage());
        }
    }

    protected void initData() {
        BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
        if (teacher == null) {
            return;
        }

        ImageLoader.getInstance().displayImage(teacher.getHead_portrait(),
                new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());

        txtTeacherName.setText(teacher.getName());
        txtPhone.setText(teacher.getPhone());
        txtEducation.setText(teacher.getEducation());
        txtSchoolArea.setText(teacher.getA_name());
        txtSchoolName.setText(teacher.getS_name());
        txtDepartmentName.setText(teacher.getD_name());
        txtAdviser.setText(teacher.getCharge() == "1" ? "是" : "否");
    }

    @OnClick({R.id.rlMinePhoto, R.id.rlMinePhone})
    void viewClick(View view) {
        BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
        if (teacher == null) {
            return;
        }
        switch (view.getId()) {
            case R.id.rlMinePhoto:
                choosePic(imgHead);
                break;
            case R.id.rlMinePhone:
                if (teacher.getPhone().isEmpty()) {
                    Toast.makeText(mContext, R.string.toast_phone_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + teacher.getPhone()));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    mContext.startActivity(intent);
                } catch (Exception ex) {
                    Toast.makeText(mContext, R.string.toast_startcall_error, Toast.LENGTH_SHORT).show();
                }
                break;
        }
    }

}
