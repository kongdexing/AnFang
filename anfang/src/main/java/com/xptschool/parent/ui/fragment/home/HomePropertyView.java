package com.xptschool.parent.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.widget.mygridview.MyGridView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.ui.fragment.HomeItemGridAdapter;
import com.xptschool.parent.ui.main.WebCommonActivity;
import com.xptschool.parent.ui.mine.BaseInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 智慧金融
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomePropertyView extends BaseInfoView {

    @BindView(R.id.llHomeInvest)
    LinearLayout llHomeInvest;
    @BindView(R.id.txtGroupName)
    TextView txtGroupName;
    @BindView(R.id.grd_invest)
    MyGridView grd_invest;

    public HomePropertyView(Context context) {
        super(context);
    }

    public HomePropertyView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_property, this, true);
        ButterKnife.bind(view);
        llHomeInvest.setVisibility(GONE);
    }

    public void bindData(List<BeanHomeCfg> homeCfgs) {
        if (homeCfgs.size() > 0) {
            txtGroupName.setText(homeCfgs.get(0).getProduct_name());

            llHomeInvest.setVisibility(VISIBLE);
            MyPropertyAdapter itemAdapter = new MyPropertyAdapter(mContext);
            grd_invest.setAdapter(itemAdapter);

            itemAdapter.reloadData(homeCfgs);
        } else {
            llHomeInvest.setVisibility(GONE);
        }
    }

    class MyPropertyAdapter extends BaseAdapter {

        private Context mContext;
        public List<BeanHomeCfg> homeItems = new ArrayList<>();

        public MyPropertyAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void reloadData(List<BeanHomeCfg> items) {
            this.homeItems = items;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return homeItems.size();
        }

        @Override
        public BeanHomeCfg getItem(int position) {
            return homeItems.size() > position ? homeItems.get(position) : null;
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(final int position, View convertView, ViewGroup parent) {
            Log.i(TAG, "getView: " + position);
            ViewHolder viewHolder = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.item_home_option, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.llHomeItem = (LinearLayout) convertView.findViewById(R.id.llHomeItem);
                viewHolder.optionImg = (ImageView) convertView.findViewById(R.id.optionImg);
                viewHolder.optionText = (TextView) convertView.findViewById(R.id.optionText);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final BeanHomeCfg homeCfg = getItem(position);
            if (homeCfg != null) {
                ImageLoader.getInstance().displayImage(homeCfg.getImage(),
                        new ImageViewAware(viewHolder.optionImg), CommonUtil.getDefaultImageLoaderOption());
                viewHolder.optionText.setText(homeCfg.getTitle());

                viewHolder.llHomeItem.setOnClickListener(new OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        Intent intent = new Intent(mContext, WebCommonActivity.class);
                        intent.putExtra(ExtraKey.WEB_URL, homeCfg.getUrl());
                        mContext.startActivity(intent);
                    }
                });
            }

            return convertView;
        }
    }

    class ViewHolder {
        LinearLayout llHomeItem;
        TextView optionText;
        ImageView optionImg;
    }

}
