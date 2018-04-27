package com.xptschool.parent.ui.notice;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.adapter.BaseRecycleAdapter;
import com.xptschool.parent.adapter.RecyclerViewHolderBase;
import com.xptschool.parent.bean.BeanClassInfo;
import com.xptschool.parent.bean.BeanNotice;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.UserType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NoticeAdapter extends BaseRecycleAdapter {

    private List<BeanNotice> listNotices = new ArrayList<>();

    public NoticeAdapter(Context context) {
        super(context);
    }

    public void refreshData(List<BeanNotice> data) {
        Log.i(TAG, "refreshData: ");
        listNotices = data;
    }

    public void appendData(List<BeanNotice> data) {
        Log.i(TAG, "refreshData: ");
        listNotices.addAll(data);
    }

    public int deleteData(BeanNotice notice) {
        for (int i = 0; i < listNotices.size(); i++) {
            if (listNotices.get(i).getM_id().equals(notice.getM_id())) {
                listNotices.remove(i);
                return i;
            }
        }
        return -1;
    }

    @Override
    public RecyclerViewHolderBase onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_notice, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        ViewHolder mHolder = (ViewHolder) holder;
        final BeanNotice notice = listNotices.get(position);
        List<BeanClassInfo> classInfos = notice.getClassInfo();
        if (classInfos.size() > 0) {
            BeanClassInfo classInfo = classInfos.get(0);
            String className = classInfo.getG_name() + classInfo.getC_name();
            if (classInfos.size() > 1) {
                className += "...";
            }
            mHolder.txtClassName.setText(className);
        } else {
            mHolder.txtClassName.setVisibility(View.GONE);
        }

        mHolder.txtTitle.setText(notice.getTitle());
        mHolder.txtTime.setText(CommonUtil.parseDate(notice.getCreate_time()));
        mHolder.txtContent.setText(notice.getContent());

        if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            mHolder.imgArrow.setVisibility(View.INVISIBLE);
        } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            mHolder.imgArrow.setVisibility(View.VISIBLE);
//            if (notice.getFs_type().equals("1")) {
//                //自己发布的公告
//                mHolder.imgArrow.setBackgroundResource(R.drawable.arrow_right_up);
//            } else {
//                //接收到的公告
//                mHolder.imgArrow.setBackgroundResource(R.drawable.arrow_left_down);
//            }
        }

        mHolder.llNoticeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                    intent.setClass(mContext, NoticeDetailActivity.class);
                } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                    intent.setClass(mContext, NoticeDetailTActivity.class);
                }
                intent.putExtra(ExtraKey.NOTICE_DETAIL, notice);
                ((Activity) mContext).startActivityForResult(intent, 1);
            }
        });
    }

    @Override
    public int getItemCount() {
        return listNotices.size();
    }

    public class ViewHolder extends RecyclerViewHolderBase {
        private Unbinder unbinder;
        @BindView(R.id.llNoticeItem)
        LinearLayout llNoticeItem;
        @BindView(R.id.txtClassName)
        TextView txtClassName;
        @BindView(R.id.txtTitle)
        TextView txtTitle;
        @BindView(R.id.txtTime)
        TextView txtTime;
        @BindView(R.id.txtContent)
        TextView txtContent;
        @BindView(R.id.imgArrow)
        ImageView imgArrow;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }

    }

}
