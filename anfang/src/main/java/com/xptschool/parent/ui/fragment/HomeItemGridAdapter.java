package com.xptschool.parent.ui.fragment;

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

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.bean.HomeItem;
import com.xptschool.parent.ui.login.LoginActivity;
import com.xptschool.parent.view.CustomDialog;

import java.util.ArrayList;
import java.util.List;

public class HomeItemGridAdapter extends BaseAdapter {
    private Context mContext;
    private String TAG = getClass().getSimpleName();
    public List<HomeItem> homeItems = new ArrayList<>();

    public HomeItemGridAdapter(Context mContext) {
        super();
        this.mContext = mContext;
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

        final HomeItem item = getItem(position);
        if (item.getIntent() == null) {
            return convertView;
        }

        viewHolder.llHomeItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (item.isShowForParent() || item.isShowForTeacher()) {
                    //只有家长或老师可查看
                    //1.判断是否登录
                    //2.判断角色(暂时不用)
                    if (XPTApplication.getInstance().isLoggedIn()) {
                        mContext.startActivity(item.getIntent());
                    } else {
                        //弹出登录对话框
                        CustomDialog dialog = new CustomDialog(mContext);
                        dialog.setTitle(R.string.label_tip);
                        dialog.setMessage(R.string.message_tologin);
                        dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                            @Override
                            public void onPositiveClick() {
                                mContext.startActivity(new Intent(mContext, LoginActivity.class));
                            }
                        });
                    }
                } else {
                    //任意角色可见

                }
            }
        });

        viewHolder.optionImg.setBackgroundResource(item.getIconId());
        viewHolder.optionText.setText(item.getTitle());
        return convertView;
    }

    class ViewHolder {
        LinearLayout llHomeItem;
        TextView optionText;
        ImageView optionImg;

    }

}
