package com.xptschool.parent.ui.watch;

import android.content.Context;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
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
import com.xptschool.parent.ui.fragment.home.HomePropertyView;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.main.WebCommonActivity;
import com.xptschool.parent.ui.shop.ShopListActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.util.WatchUtil;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import butterknife.BindView;

/**
 * 与手表绑定关系
 */
public class RelationActivity extends BaseActivity {

    //1 => ‘爸爸’, 2 => ‘妈妈’, 3 => ‘爷爷’, 4 => ‘奶奶’, 5 => ‘外公’, 6 => ‘外婆’,7=>’哥哥’,8=>’姐姐’,9=>’叔叔’,10=>’舅舅’,11=>’阿姨’,12=>’婶婶’, 0 => ‘其它’
    @BindView(R.id.grd_relation)
    MyGridView grd_relation;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_relation);

        final MyRelationAdapter adapter = new MyRelationAdapter(this);
        grd_relation.setAdapter(adapter);

        //设置默认选中关系

        adapter.reloadData(WatchUtil.getRelationList());

        setBtnRight("保存");
        setBtnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent();
                intent.putExtra("relation", adapter.getCurrentRelation());
                setResult(1, intent);
                finish();
            }
        });
    }

    class MyRelationAdapter extends BaseAdapter {

        private Context mContext;
        List<WatchUtil.WatchRelation> relations = new ArrayList<>();
        private String currentRelation = "1";


        public MyRelationAdapter(Context mContext) {
            super();
            this.mContext = mContext;
        }

        public void setCurrentRelation(String relation) {
            currentRelation = relation;
        }

        public String getCurrentRelation() {
            return currentRelation;
        }

        public void reloadData(List<WatchUtil.WatchRelation> items) {
            this.relations = items;
            notifyDataSetChanged();
        }

        @Override
        public int getCount() {
            return relations.size();
        }

        @Override
        public WatchUtil.WatchRelation getItem(int position) {
            return relations.size() > position ? relations.get(position) : null;
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
                        R.layout.item_relation_option, parent, false);
                viewHolder = new ViewHolder();
                viewHolder.llHomeItem = (LinearLayout) convertView.findViewById(R.id.llItem);
                viewHolder.optionText = (TextView) convertView.findViewById(R.id.optionText);
                convertView.setTag(viewHolder);
            } else {
                viewHolder = (ViewHolder) convertView.getTag();
            }

            final WatchUtil.WatchRelation relation = getItem(position);
            if (relation != null) {
                viewHolder.optionText.setText(relation.getValue());
                if (currentRelation.equals(relation.getKey())) {
                    viewHolder.llHomeItem.setBackgroundResource(R.drawable.btn_relation_select);
                    viewHolder.optionText.setTextColor(getResources().getColor(R.color.colorPrimary));
                } else {
                    viewHolder.llHomeItem.setBackgroundResource(R.drawable.btn_recorder_normal);
                    viewHolder.optionText.setTextColor(getResources().getColor(R.color.color_black_6));
                }

                viewHolder.llHomeItem.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        setCurrentRelation(relation.getKey());
                        reloadData(relations);
                    }
                });
            }

            return convertView;
        }

        class ViewHolder {
            LinearLayout llHomeItem;
            TextView optionText;
        }
    }


}
