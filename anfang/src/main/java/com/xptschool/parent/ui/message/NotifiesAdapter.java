package com.xptschool.parent.ui.message;

import android.content.Context;
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
import com.xptschool.parent.bean.BeanNotify;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class NotifiesAdapter extends BaseRecycleAdapter {

    private List<BeanNotify> beanNotifies = new ArrayList<>();

    public NotifiesAdapter(Context context) {
        super(context);
        Log.i(TAG, "HomeWorkParentAdapter: ");
    }

    public void refreshData(List<BeanNotify> newsList) {
        Log.i(TAG, "refreshData: ");
        beanNotifies = newsList;
//        notifyDataSetChanged();
    }

    public void appendData(List<BeanNotify> newsList) {
        Log.i(TAG, "appendData: ");
        beanNotifies.addAll(newsList);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_notifies_item, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(TAG, "showData: " + position);
        final ViewHolder mHolder = (ViewHolder) holder;
        final BeanNotify news = beanNotifies.get(position);
        mHolder.news_title.setText(news.getTitle());
        mHolder.news_intro.setText(news.getContent());
        mHolder.news_time.setText(news.getCreate_time());
    }

    @Override
    public int getItemCount() {
        return beanNotifies.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

        @BindView(R.id.llNewsItem)
        LinearLayout llNewsItem;

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
