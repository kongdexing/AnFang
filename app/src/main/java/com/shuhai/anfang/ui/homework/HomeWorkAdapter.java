package com.shuhai.anfang.ui.homework;

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

import com.shuhai.anfang.R;
import com.shuhai.anfang.adapter.BaseRecycleAdapter;
import com.shuhai.anfang.adapter.RecyclerViewHolderBase;
import com.shuhai.anfang.bean.BeanHomeWork;
import com.shuhai.anfang.common.ExtraKey;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

/**
 * Created by Administrator on 2016/10/26.
 */
public class HomeWorkAdapter extends BaseRecycleAdapter {

    private List<BeanHomeWork> beanHomeWorks = new ArrayList<>();
    private String TAG = getClass().getSimpleName();
    private Context mContext;

    public HomeWorkAdapter(Context context) {
        super(context);
        Log.i(TAG, "HomeWorkAdapter: ");
        mContext = context;
    }

    public void refreshData(List<BeanHomeWork> listHomeWorks) {
        Log.i(TAG, "refreshData: ");
        beanHomeWorks = listHomeWorks;
    }

    public void appendData(List<BeanHomeWork> listHomeWorks) {
        Log.i(TAG, "refreshData: ");
        beanHomeWorks.addAll(listHomeWorks);
    }

    public int deleteData(BeanHomeWork homeWork) {
        for (int i = 0; i < beanHomeWorks.size(); i++) {
            if (beanHomeWorks.get(i).getH_id().equals(homeWork.getH_id())) {
                beanHomeWorks.remove(i);
                Log.i(TAG, "deleteData:  " + i);
                return i;
            }
        }
        return -1;
    }

    @Override
    public RecyclerView.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Log.i(TAG, "onCreateViewHolder: ");
        View view = LayoutInflater.from(parent.getContext())
                .inflate(R.layout.item_homework, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RecyclerView.ViewHolder holder, final int position) {
        Log.i(TAG, "showData: ");
        final ViewHolder mHolder = (ViewHolder) holder;
        final BeanHomeWork work = beanHomeWorks.get(position);
        String course = work.getCrs_name().substring(0, 1);
        if (course.equals("语")) {
            work.setSubjectBgColor(R.drawable.bg_circle_yuwen);
        } else if (course.equals("数")) {
            work.setSubjectBgColor(R.drawable.bg_circle_shuxue);
        } else if (course.equals("英")) {
            work.setSubjectBgColor(R.drawable.bg_circle_yingyu);
        } else if (course.equals("物")) {
            work.setSubjectBgColor(R.drawable.bg_circle_wuli);
        } else if (course.equals("地")) {
            work.setSubjectBgColor(R.drawable.bg_circle_dili);
        } else if (course.equals("化")) {
            work.setSubjectBgColor(R.drawable.bg_circle_huaxue);
        } else if (course.equals("政") || course.equals("品") || course.equals("思")) {
            work.setSubjectBgColor(R.drawable.bg_circle_zhengzhi);
        } else if (course.equals("生")) {
            work.setSubjectBgColor(R.drawable.bg_circle_shengwu);
        } else if (course.equals("体")) {
            work.setSubjectBgColor(R.drawable.bg_circle_tiyu);
        } else {
            work.setSubjectBgColor(R.drawable.bg_circle_other);
        }
        mHolder.txtSubject.setBackgroundResource(work.getSubjectBgColor());
        mHolder.txtClassName.setText(work.getG_name() + work.getC_name());
        mHolder.txtSubject.setText(course);

        mHolder.txtTeacher.setText(mContext.getString(R.string.label_teacher_option, work.getUser_name()));
        mHolder.txtTitle.setText(mContext.getString(R.string.label_homework_title_option, work.getName()));
        mHolder.txtTime.setText(mContext.getString(R.string.homework_start_end_time, work.getCreate_time(), work.getFinish_time()));
        String content = work.getWork_content();
        if (content.length() > 100) {
            content = content.substring(0, 100);
        }
        mHolder.txtContent.setText(content);

        mHolder.llhomeworkItem.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(mContext, HomeWorkDetailActivity.class);
                intent.putExtra(ExtraKey.HOMEWORK_DETAIL, work);
                ((HomeWorkActivity) mContext).startActivityForResult(intent, 1);
            }
        });

        Log.i(TAG, "onBindViewHolder: file_path " + work.getFile_path());

    }

    @Override
    public int getItemCount() {
        Log.i(TAG, "getItemCount: " + beanHomeWorks.size());
        return beanHomeWorks.size();
    }

    class ViewHolder extends RecyclerViewHolderBase {

        private Unbinder unbinder;

        @BindView(R.id.llhomeworkItem)
        LinearLayout llhomeworkItem;

        @BindView(R.id.txtSubject)
        TextView txtSubject;

        @BindView(R.id.txtClassName)
        TextView txtClassName;

        @BindView(R.id.txtTitle)
        TextView txtTitle;

        @BindView(R.id.txtTeacher)
        TextView txtTeacher;

        @BindView(R.id.txtTime)
        TextView txtTime;

        @BindView(R.id.txtContent)
        TextView txtContent;

        @BindView(R.id.imgDelete)
        ImageView imgDelete;

        public ViewHolder(View itemView) {
            super(itemView);
            unbinder = ButterKnife.bind(this, itemView);
        }

    }

}
