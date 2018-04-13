package com.xptschool.parent.ui.fragment.home;

import android.content.Context;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.widget.mygridview.MyGridView;
import com.xptschool.parent.R;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.ui.main.WebCommonActivity;
import com.xptschool.parent.ui.mine.BaseInfoView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 新闻资讯
 * Created by shuhaixinxi on 2018/1/13.
 */

public class HomeNewsView extends BaseInfoView {

    @BindView(R.id.llHomeNews)
    LinearLayout llHomeNews;

    @BindView(R.id.txtGroupName)
    TextView txtGroupName;
    @BindView(R.id.txtMore)
    TextView txtMore;

    @BindView(R.id.grd_news)
    MyGridView grd_news;

    public HomeNewsView(Context context) {
        super(context);
    }

    public HomeNewsView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.item_home_news, this, true);
        ButterKnife.bind(view);

        txtMore.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent newsIntent = new Intent(mContext, WebCommonActivity.class);
                newsIntent.putExtra(ExtraKey.WEB_URL, "http://school.xinpingtai.com/edunews/");
                mContext.startActivity(newsIntent);
            }
        });
    }

    public void bindData(List<BeanHomeCfg> homeCfgs) {
        if (homeCfgs.size() > 0) {
            llHomeNews.setVisibility(VISIBLE);

            txtGroupName.setText(homeCfgs.get(0).getProduct_name());

            MyNewsAdapter itemAdapter = new MyNewsAdapter(mContext);
            grd_news.setAdapter(itemAdapter);

            itemAdapter.reloadData(homeCfgs);

        } else {
            llHomeNews.setVisibility(GONE);
        }
    }


    class MyNewsAdapter extends BaseAdapter {

        private Context mContext;
        public List<BeanHomeCfg> homeItems = new ArrayList<>();

        public MyNewsAdapter(Context mContext) {
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
            final BeanHomeCfg homeCfg = getItem(position);
            HomeNewsItemView itemView = new HomeNewsItemView(mContext);
            itemView.bindData(homeCfg);
            return itemView;
        }
    }

}
