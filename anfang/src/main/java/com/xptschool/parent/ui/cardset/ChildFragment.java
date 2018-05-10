package com.xptschool.parent.ui.cardset;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.CircularImageView;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.fragment.BaseFragment;
import com.xptschool.parent.ui.mine.MyChildActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import java.text.SimpleDateFormat;
import java.util.Date;

public class ChildFragment extends BaseFragment implements View.OnClickListener {

    private static String TAG = ChildFragment.class.getSimpleName();

    CircularImageView imgHead;
    ImageView imgSex;
    ImageView imgEdit;
    TextView txtName;
    LinearLayout llInfoBg;
    TextView txtAge;
    TextView txtCardPhone;
    RelativeLayout rlUnbind;

    private BeanStudent currentStudent;

    public ChildFragment() {
        // Required empty public constructor
    }

    public void setStudent(BeanStudent student) {
        Log.i(TAG, "setStudent: ");
        currentStudent = student;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_child, container, false);

        imgHead = (CircularImageView) view.findViewById(R.id.imgHead);
        imgSex = (ImageView) view.findViewById(R.id.imgSex);
        imgEdit = (ImageView) view.findViewById(R.id.imgEdit);
        txtName = (TextView) view.findViewById(R.id.txtName);
        llInfoBg = (LinearLayout) view.findViewById(R.id.llInfoBg);
        txtAge = (TextView) view.findViewById(R.id.txtAge);

        txtCardPhone = (TextView) view.findViewById(R.id.txtCardPhone);
        txtCardPhone.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String cardPhone = txtCardPhone.getText().toString();
                if (cardPhone.isEmpty()) {
                    return;
                }
                try {
                    Intent intent = new Intent();
                    intent.setAction(Intent.ACTION_DIAL);
                    intent.setData(Uri.parse("tel:" + cardPhone));
                    intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                    startActivity(intent);
                } catch (Exception ex) {
                    ToastUtils.showToast(ChildFragment.this.getContext(), R.string.toast_startcall_error);
                }
            }
        });

        imgEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                
            }
        });

        rlUnbind = (RelativeLayout) view.findViewById(R.id.rlUnbind);
        rlUnbind.setOnClickListener(this);
        return view;
    }

    @Override
    protected void initData() {
        if (currentStudent == null || txtName == null) {
            Log.i(TAG, "bindingData: " + txtName.hashCode());
            return;
        }
        if ("1".equals(currentStudent.getSex())) {
            imgHead.setImageResource(R.drawable.student_boy);
            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_boy);
            imgSex.setBackgroundResource(R.drawable.male_w);
        } else {
            imgHead.setImageResource(R.drawable.student_girl);
            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_girl);
            imgSex.setBackgroundResource(R.drawable.female_w);
        }
        //设置信息
        txtName.setText(currentStudent.getStu_name());

        try {
            SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
            Date dbDate = (Date) dateFormat.parse(currentStudent.getBirth_date());
            txtAge.setText(CommonUtil.getAge(dbDate) + "岁");
        } catch (Exception ex) {
            txtAge.setText("未知");
        }
        txtCardPhone.setText(currentStudent.getCard_phone());
    }

    @Override
    public void onClick(View view) {
        if (currentStudent != null && currentStudent.getImei_id().isEmpty()) {
            Toast.makeText(mContext, R.string.toast_imei_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        Intent intent = new Intent();
        switch (view.getId()) {
            case R.id.rlUnbind:
                //解绑设备
                CustomDialog dialog = new CustomDialog(mContext);
                dialog.setTitle(R.string.label_tip);
                dialog.setMessage(mContext.getResources().getString(R.string.msg_unbind_watch, currentStudent.getImei_id()));
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        unBindDevice();
                    }
                });
                break;
        }
    }

    private void unBindDevice() {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.WATCH_UnBind,
                new VolleyHttpParamsEntity()
                        .addParam("stu_id", currentStudent.getStu_id())
                        .addParam("user_id", XPTApplication.getInstance().getCurrentUserId()), new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        super.onStart();
                        ((MyChildActivity) mContext).showProgress("正在解除设备");
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        ((MyChildActivity) mContext).hideProgress();
                        ToastUtils.showToast(mContext, volleyHttpResult.getInfo());
                        if (volleyHttpResult.getStatus() == HttpAction.SUCCESS) {
                            //删除学生，重新刷新界面
                            GreenDaoHelper.getInstance().deleteStuById(currentStudent.getStu_id());
                            ((MyChildActivity) mContext).initData();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        ((MyChildActivity) mContext).hideProgress();
                    }
                });

    }

}
