package com.shuhai.anfang.ui.comment;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhai.anfang.R;
import com.shuhai.anfang.adapter.BaseRecycleAdapter;
import com.shuhai.anfang.adapter.RecyclerViewHolderBase;
import com.shuhai.anfang.bean.BeanComment;
import com.shuhai.anfang.bean.BeanHonor;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.ui.honor.HonorTAdapter;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by shuhaixinxi on 2017/12/20.
 */
//评语管理
public class CommentAdapter extends BaseRecycleAdapter {

    List<BeanComment> commentList = new ArrayList<>();

    public CommentAdapter(Context context) {
        super(context);
    }

    public void refreshData(List<BeanComment> comments) {
        Log.i(TAG, "refreshData: ");
        commentList = comments;
    }

    public void appendData(List<BeanComment> comments) {
        Log.i(TAG, "refreshData: ");
        commentList.addAll(comments);
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_comment, parent, false);
        return new CommentAdapter.ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, int position) {
        final CommentAdapter.ViewHolder mHolder = (CommentAdapter.ViewHolder) holder;
        final BeanComment comment = commentList.get(position);

        mHolder.txtStuName.setText(comment.getStu_name());
        mHolder.txtTime.setText(comment.getCreate_time());
        mHolder.txtCommentType.setText(comment.getR_type());
        mHolder.txtContent.setText(comment.getContent());
        mHolder.llItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(mContext, CommentDetailActivity.class);
                intent.putExtra(ExtraKey.COMMENT_DETAIL, comment);
                mContext.startActivity(intent);
            }
        });

    }

    @Override
    public int getItemCount() {
        return commentList.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

        @BindView(R.id.llItem)
        LinearLayout llItem;
        @BindView(R.id.txtStuName)
        TextView txtStuName;

        @BindView(R.id.txtTime)
        TextView txtTime;

        @BindView(R.id.txtCommentType)
        TextView txtCommentType;

        @BindView(R.id.txtContent)
        TextView txtContent;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }
    }

}
