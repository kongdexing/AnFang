package com.shuhai.anfang.ui.honor;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhai.anfang.R;
import com.shuhai.anfang.adapter.BaseRecycleAdapter;
import com.shuhai.anfang.adapter.RecyclerViewHolderBase;
import com.shuhai.anfang.bean.BeanHomeWork;
import com.shuhai.anfang.bean.BeanHonor;
import com.shuhai.anfang.ui.homework.HomeWorkParentAdapter;
import com.shuhai.anfang.ui.homework.HomeWorkTeacherAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by shuhaixinxi on 2017/12/18.
 */

public class HonorTAdapter extends BaseRecycleAdapter {

    List<BeanHonor> honors = new ArrayList<>();

    public HonorTAdapter(Context context) {
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
        return new HonorTAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final HonorTAdapter.ViewHolder mHolder = (HonorTAdapter.ViewHolder) holder;
        final BeanHonor honor = honors.get(position);

        mHolder.txtStuName.setText(honor.getStu_name());
        mHolder.txtTime.setText(honor.getCreate_time());
        mHolder.txtHonorType.setText(honor.getReward_type());
        mHolder.txtContent.setText(honor.getReward_details());

    }

    @Override
    public int getItemCount() {
        return honors.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

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
