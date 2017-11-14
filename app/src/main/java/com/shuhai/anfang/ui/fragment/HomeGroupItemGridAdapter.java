package com.shuhai.anfang.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.shuhai.anfang.R;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.model.BeanHomeCfgChild;
import com.shuhai.anfang.ui.main.WebViewActivity;

import java.util.ArrayList;
import java.util.List;

public class HomeGroupItemGridAdapter extends BaseAdapter {
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    public List<BeanHomeCfgChild> homeItems = new ArrayList<>();

    public HomeGroupItemGridAdapter(Context mContext) {
        super();
        this.mContext = mContext;
    }

    public void reloadData(List<BeanHomeCfgChild> items) {
        this.homeItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return homeItems == null ? 0 : homeItems.size();
    }

    @Override
    public BeanHomeCfgChild getItem(int position) {
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

        final BeanHomeCfgChild item = getItem(position);

        viewHolder.llHomeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, WebViewActivity.class);
                intent.putExtra(ExtraKey.WEB_URL, item.getUrl());
                mContext.startActivity(intent);
            }
        });

        ImageLoader.getInstance().displayImage(item.getImg(),
                new ImageViewAware(viewHolder.optionImg), CommonUtil.getDefaultImageLoaderOption());

        viewHolder.optionText.setText(item.getTitle());
        return convertView;
    }

    class ViewHolder {
        LinearLayout llHomeItem;
        TextView optionText;
        ImageView optionImg;

    }

}
