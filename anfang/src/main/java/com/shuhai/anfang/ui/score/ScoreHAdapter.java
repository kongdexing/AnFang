package com.shuhai.anfang.ui.score;

import android.content.Context;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.shuhai.anfang.R;
import com.shuhai.anfang.bean.BeanScore;
import com.shuhai.anfang.bean.BeanScoreTeacher;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

/**
 * Created by dexing on 2016/11/27.
 * No1
 */

public class ScoreHAdapter extends BaseAdapter {

    private String TAG = getClass().getSimpleName();
    private List<BeanScoreTeacher> datas = new ArrayList<BeanScoreTeacher>();
    private Context mContext;

    public ScoreHAdapter(Context context) {
        super();
        this.mContext = context;
    }

    public void loadScore(List<BeanScoreTeacher> data) {
        this.datas.clear();
        if (data != null) {
            this.datas = data;
        }
        notifyDataSetChanged();
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public Object getItem(int i) {
        return null;
    }

    @Override
    public int getCount() {
        return datas.size();
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        ViewHolder viewHolder = null;
        if (convertView == null) {
            viewHolder = new ViewHolder();
            convertView = LayoutInflater.from(mContext).inflate(R.layout.common_item_hlistview, parent, false);
            viewHolder.studentName = (TextView) convertView.findViewById(R.id.item_titlev);
            viewHolder.scoreScrollView = (SyncScrollHorizontalView) convertView.findViewById(R.id.item_chscroll_scroll);
            viewHolder.llScoreContent = (LinearLayout) convertView.findViewById(R.id.llScoreContent);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }
        BeanScoreTeacher score = datas.get(position);
        viewHolder.studentName.setText(score.getStudentName());
        LinkedHashMap<String, String> scoreMap = score.getScoreMap();
        Log.i(TAG, "getView position:" + position + " size:" + scoreMap.size());

        viewHolder.llScoreContent.removeAllViews();
        Iterator iter = scoreMap.entrySet().iterator();
        while (iter.hasNext()) {
            Map.Entry entry = (Map.Entry) iter.next();
            Object key = entry.getKey();
            String val = (String) entry.getValue();
            TextView title = new TextView(mContext);
            LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(mContext.getResources().getDimensionPixelSize(R.dimen.dp_60), ViewGroup.LayoutParams.MATCH_PARENT);
            title.setTextColor(mContext.getResources().getColor(R.color.text_black));
            title.setLayoutParams(lp);
            title.setGravity(Gravity.CENTER);
            title.setText(val);
            viewHolder.llScoreContent.addView(title);
        }
        ((ScoreTeacherActivity) mContext).addHViews(viewHolder.scoreScrollView);
        return convertView;
    }

    class ViewHolder {
        TextView studentName;
        SyncScrollHorizontalView scoreScrollView;
        LinearLayout llScoreContent;
    }

}
