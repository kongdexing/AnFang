package com.xptschool.parent.ui.watch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.adapter.BaseRecycleAdapter;
import com.xptschool.parent.adapter.RecyclerViewHolderBase;
import com.xptschool.parent.bean.BeanHomeWork;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.ui.homework.HomeWorkTeacherAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * 手表聊天
 */
public class WatchChatAdapter extends BaseRecycleAdapter {

    private List<BeanStudent> beanStudents = new ArrayList<>();

    public WatchChatAdapter(Context context) {
        super(context);
        Log.i(TAG, "HomeWorkParentAdapter: ");
    }

    public void refreshData(List<BeanStudent> listHomeWorks) {
        Log.i(TAG, "refreshData: ");
        beanStudents = listHomeWorks;
    }

    public void appendData(List<BeanStudent> listHomeWorks) {
        Log.i(TAG, "refreshData: ");
        beanStudents.addAll(listHomeWorks);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_chatlist, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(TAG, "showData: ");
        BeanStudent student = beanStudents.get(position);
        final ViewHolder mHolder = (ViewHolder) holder;
        if (student != null) {
            String name = student.getStu_name();
            if (name == null || name.isEmpty()) {
                mHolder.txtNickName.setText(student.getImei_id());
            } else {
                mHolder.txtNickName.setText(name);
            }
            //读取聊天信息


            mHolder.txtChatInfo.setText("暂无聊天信息");
            mHolder.rlItem1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatDetailActivity.class);

                    mContext.startActivity(intent);
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + beanStudents.size());
        return beanStudents.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

        @BindView(R.id.rlItem1)
        RelativeLayout rlItem1;

        @BindView(R.id.txtNickName)
        TextView txtNickName;

        @BindView(R.id.txtChatInfo)
        TextView txtChatInfo;

        @BindView(R.id.txtTime)
        TextView txtTime;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }

    }

}
