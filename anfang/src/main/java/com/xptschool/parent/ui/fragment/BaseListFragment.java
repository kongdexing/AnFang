package com.xptschool.parent.ui.fragment;

import android.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;

import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.adapter.CardDividerItemDecoration;
import com.xptschool.parent.adapter.WrapContentLinearLayoutManager;
import com.xptschool.parent.bean.ResultPage;

public class BaseListFragment extends BaseFragment {

    public ResultPage resultPage = new ResultPage();
    private WrapContentLinearLayoutManager mLayoutManager;

    public void initRecyclerView(RecyclerView recyclerView, SwipeRefreshLayout swipeRefreshLayout) {
        if (recyclerView != null) {
            recyclerView.setHasFixedSize(true);
            mLayoutManager = new WrapContentLinearLayoutManager(XPTApplication.getInstance());
            recyclerView.setLayoutManager(mLayoutManager);
            recyclerView.addItemDecoration(new CardDividerItemDecoration(XPTApplication.getInstance(),
                    LinearLayoutManager.VERTICAL));
        }
        if (swipeRefreshLayout != null) {
            swipeRefreshLayout.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
        }
//        swipeRefreshLayout.setProgressViewOffset(false, 0, (int) TypedValue
//                .applyDimension(TypedValue.COMPLEX_UNIT_DIP, 24, getResources()
//                        .getDisplayMetrics()));
    }

    public WrapContentLinearLayoutManager getLayoutManager() {
        return mLayoutManager;
    }

    @Override
    protected void initData() {

    }
}
