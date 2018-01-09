package com.xptschool.parent.ui.honor;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.xptschool.parent.R;
import com.xptschool.parent.adapter.BaseRecycleAdapter;
import com.xptschool.parent.adapter.RecyclerViewHolderBase;
import com.xptschool.parent.bean.BeanHonor;
import com.xptschool.parent.common.ExtraKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by shuhaixinxi on 2017/12/18.
 */

public class HonorAdapter extends BaseRecycleAdapter {

    List<BeanHonor> honors = new ArrayList<>();

    public HonorAdapter(Context context) {
        super(context);
    }

    public void refreshData(List<BeanHonor> listHonors) {
        Log.i(TAG, "refreshData: ");
        honors = listHonors;
    }

    public void appendData(List<BeanHonor> listHonors) {
        Log.i(TAG, "refreshData: ");
        honors.addAll(listHonors);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_honor, parent, false);
        return new HonorAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HonorAdapter.ViewHolder mHolder = (HonorAdapter.ViewHolder) holder;
        final BeanHonor honor = honors.get(position);

        mHolder.txtStuName.setText(honor.getStu_name());
        mHolder.txtTime.setText(honor.getCreate_time());
        mHolder.txtHonorType.setText(honor.getReward_type());
        mHolder.txtContent.setText(honor.getReward_details());

        mHolder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, HonorDetailActivity.class);
                intent.putExtra(ExtraKey.HONOR_DETAIL, honor);
                mContext.startActivity(intent);
            }
        });
    }

    @Override
    public int getItemCount() {
        return honors.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

        @BindView(R.id.llItem)
        LinearLayout llItem;
        @BindView(R.id.txtStuName)
        TextView txtStuName;

        @BindView(R.id.txtTime)
        TextView txtTime;

        @BindView(R.id.txtHonorType)
        TextView txtHonorType;

        @BindView(R.id.txtContent)
        TextView txtContent;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }


}
