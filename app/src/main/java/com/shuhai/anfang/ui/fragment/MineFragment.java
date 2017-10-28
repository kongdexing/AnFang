package com.shuhai.anfang.ui.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.shuhai.anfang.R;
import com.shuhai.anfang.model.BeanParent;
import com.shuhai.anfang.model.GreenDaoHelper;

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

    @BindView(R.id.txtDot)
    TextView txtDot;

    private Unbinder unbinder;

    public MineFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_mine, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        txtDot = (TextView) mRootView.findViewById(R.id.txtDot);
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    protected void initData() {
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

    @OnClick({R.id.imgHead, R.id.txtChangeAccount, R.id.rlMyChild, R.id.rlMyFences, R.id.rlMyWellet,
            R.id.rlMyCourse, R.id.rlMyContacts, R.id.rlSetting})
    void knifeClick(View view) {
        switch (view.getId()) {
//            case R.id.imgHead:
//                startActivity(new Intent(getContext(), MyInfoActivity.class));
//                break;
//            case R.id.rlMyChild:
//                startActivity(new Intent(getContext(), MyChildActivity.class));
//                break;
//            case R.id.rlMyFences:
//                startActivity(new Intent(getContext(), FenceListActivity.class));
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
        }
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
