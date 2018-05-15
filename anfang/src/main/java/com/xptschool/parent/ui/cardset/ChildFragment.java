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
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
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
import com.xptschool.parent.ui.watch.StuWatchEditActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.util.WatchUtil;
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
    TextView txtCardPhone;
    TextView txtRelation;

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
        txtRelation = (TextView) view.findViewById(R.id.txtRelation);

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

        //跳转至编辑设备信息页
        imgEdit.setOnClickListener(this);
        return view;
    }

    @Override
    public void onResume() {
        super.onResume();
        initData();
    }

    @Override
    protected void initData() {
        if (currentStudent == null || txtName == null) {
            Log.i(TAG, "bindingData: " + txtName.hashCode());
            return;
        }
        currentStudent = GreenDaoHelper.getInstance().getStudentByStuId(currentStudent.getStu_id());

        ImageLoader.getInstance().displayImage(currentStudent.getPhoto(),
                new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());

        if ("1".equals(currentStudent.getSex())) {
            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_boy);
            imgSex.setBackgroundResource(R.drawable.male_w);
        } else {
            llInfoBg.setBackgroundResource(R.drawable.bg_student_info_girl);
            imgSex.setBackgroundResource(R.drawable.female_w);
        }
        //设置信息
        txtName.setText(currentStudent.getStu_name());

        String relation = WatchUtil.getRelationByKey(currentStudent.getRelation());
        txtRelation.setText("我是TA的" + relation);

        txtCardPhone.setText(currentStudent.getCard_phone());
    }

    @Override
    public void onClick(View view) {
        if (currentStudent != null && currentStudent.getImei_id().isEmpty()) {
            Toast.makeText(mContext, R.string.toast_imei_empty, Toast.LENGTH_SHORT).show();
            return;
        }

        switch (view.getId()) {
            case R.id.imgEdit:
                Intent intent = new Intent(mContext, StuWatchEditActivity.class);
                intent.putExtra("student", currentStudent);
                mContext.startActivity(intent);
                break;
        }
    }

}
