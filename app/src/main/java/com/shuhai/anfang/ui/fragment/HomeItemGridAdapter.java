package com.shuhai.anfang.ui.fragment;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhai.anfang.R;
import com.shuhai.anfang.bean.HomeItem;

import java.util.ArrayList;
import java.util.List;

public class HomeItemGridAdapter extends BaseAdapter {
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    public List<HomeItem> homeItems = new ArrayList<>();
    private MyGridViewClickListener myGridViewClickListener;
    private boolean canDelete = true;

    public HomeItemGridAdapter(Context mContext, MyGridViewClickListener listener) {
        super();
        this.mContext = mContext;
        this.myGridViewClickListener = listener;
    }

    public void reloadData(List<HomeItem> items) {
        this.homeItems = items;
        notifyDataSetChanged();
    }

    @Override
    public int getCount() {
        return homeItems.size();
    }

    @Override
    public HomeItem getItem(int position) {
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
        viewHolder.llHomeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (myGridViewClickListener != null) {

                }
            }
        });
        HomeItem item = getItem(position);
        if (item != null) {
            viewHolder.optionImg.setBackgroundResource(item.getIconId());
            viewHolder.optionText.setText(item.getTitle());
        }
        return convertView;
    }

    class ViewHolder {
        LinearLayout llHomeItem;
        TextView optionText;
        ImageView optionImg;

    }

    public interface MyGridViewClickListener {
        void onGridViewItemClick(int position, String imgPath);
    }

}
