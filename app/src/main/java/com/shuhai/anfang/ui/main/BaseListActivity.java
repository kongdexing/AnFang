package com.shuhai.anfang.ui.main;

import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.shuhai.anfang.R;
import com.shuhai.anfang.adapter.DividerItemDecoration;
import com.shuhai.anfang.adapter.WrapContentLinearLayoutManager;
import com.shuhai.anfang.bean.ResultPage;

public class BaseListActivity extends BaseActivity {

    public ResultPage resultPage = new ResultPage();
    private WrapContentLinearLayoutManager mLayoutManager;

    public void initRecyclerView(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new WrapContentLinearLayoutManager(this);
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new DividerItemDecoration(this,
                    LinearLayoutManager.VERTICAL, R.drawable.line_dotted));
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
        }
//        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                        .getDisplayMetrics()));
    }

    public WrapContentLinearLayoutManager getLayoutManager(){
        return mLayoutManager;
    }


}
