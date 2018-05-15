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

import com.android.widget.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.adapter.BaseRecycleAdapter;
import com.xptschool.parent.adapter.RecyclerViewHolderBase;
import com.xptschool.parent.bean.BeanHomeWork;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.BeanWChat;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.homework.HomeWorkTeacherAdapter;
import com.xptschool.parent.util.ChatUtil;

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
        final BeanStudent student = beanStudents.get(position);
        final ViewHolder mHolder = (ViewHolder) holder;
        if (student != null) {
            ImageLoader.getInstance().displayImage(student.getPhoto(),
                    new ImageViewAware(mHolder.imgHead), CommonUtil.getDefaultUserImageLoaderOption());

            String name = student.getStu_name();
            if (name == null || name.isEmpty()) {
                mHolder.txtNickName.setText(student.getImei_id());
            } else {
                mHolder.txtNickName.setText(name);
            }
            //读取聊天信息
            BeanWChat wChat = GreenDaoHelper.getInstance().getLastChatByDeviceId(student.getImei_id());

            if (wChat == null) {
                mHolder.txtChatInfo.setText("暂无聊天信息");
            } else {
                if (ChatUtil.TYPE_TEXT.equals(wChat.getType())) {
                    mHolder.txtChatInfo.setText(wChat.getText());
                } else if (ChatUtil.TYPE_AMR.equals(wChat.getType())) {
                    mHolder.txtChatInfo.setText("[语音]");
                }
                mHolder.txtTime.setText(wChat.getTime());
            }

            mHolder.rlItem1.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, ChatDetailActivity.class);
                    intent.putExtra("student", student);
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

        @BindView(R.id.imgHead)
        CircularImageView imgHead;

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
