package com.xptschool.parent.ui.message;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.adapter.BaseRecycleAdapter;
import com.xptschool.parent.adapter.RecyclerViewHolderBase;
import com.xptschool.parent.bean.BeanNews;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NewsAdapter extends BaseRecycleAdapter {

    private List<BeanNews> beanNews = new ArrayList<>();

    public NewsAdapter(Context context) {
        super(context);
        Log.i(TAG, "HomeWorkParentAdapter: ");
    }

    public void refreshData(List<BeanNews> newsList) {
        Log.i(TAG, "refreshData: ");
        beanNews = newsList;
//        notifyDataSetChanged();
    }

    public void appendData(List<BeanNews> newsList) {
        Log.i(TAG, "appendData: ");
        beanNews.addAll(newsList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_news_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(TAG, "showData: " + position);
        final ViewHolder mHolder = (ViewHolder) holder;
        final BeanNews news = beanNews.get(position);
        mHolder.news_title.setText(news.getTitle());
        mHolder.news_intro.setText(news.getDescribe());
        mHolder.news_time.setText(news.getRelease_time());
        if (news.getImages_path() == null || news.getImages_path().isEmpty()) {
            mHolder.news_img.setVisibility(View.GONE);
        } else {
            mHolder.news_img.setVisibility(View.VISIBLE);
            ImageLoader.getInstance().displayImage(news.getImages_path(),
                    new ImageViewAware(mHolder.news_img), CommonUtil.getDefaultImageLoaderOption());
        }

        mHolder.llNewsItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, NewsDetailActivity.class);
                intent.putExtra(ExtraKey.DETAIL_ID, news.getM_id());
                mContext.startActivity(intent);

            }
        });


        Log.i(TAG, "onBindViewHolder: file_path " + news.getImages_path());
    }

    @Override
    public int getItemCount() {
        return beanNews.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

        @BindView(R.id.llNewsItem)
        LinearLayout llNewsItem;

        @BindView(R.id.news_img)
        ImageView news_img;

        @BindView(R.id.news_title)
        TextView news_title;

        @BindView(R.id.news_intro)
        TextView news_intro;

        @BindView(R.id.news_time)
        TextView news_time;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }
}
