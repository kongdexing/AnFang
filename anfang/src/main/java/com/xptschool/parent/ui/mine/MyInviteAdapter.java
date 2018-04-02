package com.xptschool.parent.ui.mine;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.widget.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.adapter.BaseRecycleAdapter;
import com.xptschool.parent.adapter.RecyclerViewHolderBase;
import com.xptschool.parent.bean.BeanInvite;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.UserType;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by shuhaixinxi on 2018/3/9.
 */

public class MyInviteAdapter extends BaseRecycleAdapter {

    private List<BeanInvite> beanInvites = new ArrayList<>();

    public MyInviteAdapter(Context context) {
        super(context);
    }

    public void appendData(List<BeanInvite> invites) {
        Log.i(TAG, "refreshData: ");
        beanInvites.addAll(invites);
    }

    public void refreshData(List<BeanInvite> invites) {
        beanInvites.clear();
        beanInvites.addAll(invites);
        Log.i(TAG, "refreshData: " + beanInvites.size());
        notifyDataSetChanged();
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(
                R.layout.item_invite, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final ViewHolder mHolder = (ViewHolder) holder;
        Log.i(TAG, "onBindViewHolder: " + position);
        final BeanInvite beanInvite = beanInvites.get(position);
        mHolder.txtUserName.setText(beanInvite.getName());
        mHolder.txtPhone.setText(beanInvite.getUsername());
        mHolder.txtTime.setText(beanInvite.getCreate_time());
        mHolder.txtStatus.setText(UserType.getUserTypeByStr(beanInvite.getType()).getRoleName());

        ImageLoader.getInstance().displayImage(beanInvite.getHead_portrait(),
                new ImageViewAware(mHolder.imgHead), CommonUtil.getDefaultUserImageLoaderOption());

        if (UserType.getUserTypeByStr(beanInvite.getType()).equals(UserType.PROXY)) {
            Log.i(TAG, "onBindViewHolder 代理商: ");
            mHolder.llItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    Intent intent = new Intent(mContext, MyInviteActivity.class);
                    intent.putExtra("user_id", beanInvite.getUser_id());
                    intent.putExtra("user_name", beanInvite.getName());
                    mContext.startActivity(intent);
                }
            });
        }
    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + beanInvites.size());
        return beanInvites.size();
    }

    public class ViewHolder extends RecyclerViewHolderBase {
        private Unbinder unbinder;

        @BindView(R.id.llItem)
        LinearLayout llItem;

        @BindView(R.id.imgHead)
        CircularImageView imgHead;

        @BindView(R.id.txtUserName)
        TextView txtUserName;

        @BindView(R.id.txtTime)
        TextView txtTime;

        @BindView(R.id.txtPhone)
        TextView txtPhone;

        @BindView(R.id.txtStatus)
        TextView txtStatus;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }

    }

}
