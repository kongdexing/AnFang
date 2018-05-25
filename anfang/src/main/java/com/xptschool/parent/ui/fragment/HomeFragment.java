package com.xptschool.parent.ui.fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.volley.common.VolleyRequestListener;
import com.android.widget.banner.Banner;
import com.android.widget.banner.BannerConfig;
import com.android.widget.banner.OnBannerListener;
import com.android.widget.mygridview.MyGridView;
import com.android.widget.pulltorefresh.PullToRefreshBase;
import com.android.widget.pulltorefresh.PullToRefreshScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.bean.HomeItem;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanBanner;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.push.BannerHelper;
import com.xptschool.parent.ui.alarm.AlarmActivity;
import com.xptschool.parent.ui.fence.FenceListActivity;
import com.xptschool.parent.ui.fragment.home.HomeHappyGroupView;
import com.xptschool.parent.ui.fragment.home.HomePropertyView;
import com.xptschool.parent.ui.main.MainActivity;
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.ui.watch.ChatListActivity;
import com.xptschool.parent.ui.watch.ClockActivity;
import com.xptschool.parent.ui.watch.FriendActivity;
import com.xptschool.parent.ui.watch.MoniterActivity;
import com.xptschool.parent.ui.watch.ShutDownActivity;
import com.xptschool.parent.ui.watch.WalkActivity;
import com.xptschool.parent.util.HomeUtil;
import com.xptschool.parent.util.NetWorkUsefulUtils;
import com.xptschool.parent.view.autoviewpager.GlideImageLoader;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.scrollView)
    PullToRefreshScrollView ptr_scrollview;
    @BindView(R.id.toolbar)
    Toolbar mToolbar;

    @BindView(R.id.rlTipAD)
    RelativeLayout rlTipAD;
    @BindView(R.id.topBanner)
    Banner topBanner;
    @BindView(R.id.tipTitle)
    TextView tipTitle;
    List<BeanBanner> advertList = new ArrayList<>();

    @BindView(R.id.llTip)
    LinearLayout llTip;

    @BindView(R.id.happyGrowView)
    HomeHappyGroupView happyGrowView;

    @BindView(R.id.propertyView)
    HomePropertyView propertyView;

    @BindView(R.id.grd_school)
    MyGridView grd_school;

    public MainActivity activity;
    private Unbinder unbinder;

    public HomeFragment() {
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
//        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar
//                .navigationBarColor(R.color.colorPrimary)
//                .fullScreen(true)
//                .addTag("PicAndColor")  //给上面参数打标记，以后可以通过标记恢复
//                .init();
//        ImmersionBar.setTitleBar(getActivity(), mToolbar);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "HomeFragment onResume: startAutoPlay");
        topBanner.startAutoPlay();
        //根据登录状态显示不同icon
        //当状态有更改时才变化Item
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "HomeFragment onPause: stopAutoPlay");
//        topBanner.stopAutoPlay();
    }

    @Override
    protected void initData() {
        Log.i(TAG, "HomeFragment initData: ");
        //先获取本地数据进行展示
        reloadTopFragment(GreenDaoHelper.getInstance().getBanners());
        initHomeCfg();

        //1获取广告位，2获取分组数据，3获取商品推荐
        getBanners();
        //获取分组数据
        getHomeGroupCfg();
    }

    public void reloadPageData() {
        Log.i(TAG, "reloadPageData: ");
        //用户切换后，重新获取广告位信息
        getBanners();
        //重新分配Intent
        initSchoolItem();
    }

    protected void initView() {
        Log.i(TAG, "HomeFragment initView: ");
        int width = XPTApplication.getInstance().getWindowWidth();

        int height = width * 3 / 5;
        Log.i(TAG, "initView: " + width + "  " + height);

        try {
            LinearLayout.LayoutParams lp = (LinearLayout.LayoutParams) rlTipAD.getLayoutParams();
            lp.width = width;
            lp.height = height;
            rlTipAD.setLayoutParams(lp);
        } catch (Exception ex) {
            Log.i(TAG, "initView setLayoutParams error: " + ex.getMessage());
        }

        //设置图片加载器
        // 1.设置幕后item的缓存数目
        topBanner.setOffscreenPageLimit(1);
        topBanner.setBackgroundResource(R.drawable.tip_ad_def);
        topBanner.updateBannerStyle(BannerConfig.CIRCLE_INDICATOR_TITLE);
//        topBanner.setImages(new ArrayList<Integer>(){R.drawable.tip_ad_def});
//        topBanner.setImages(listBannerImages);
        topBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        //设置banner动画效果
//        topBanner.setPageTransformer(true, new GalleryTransformer(getActivity()));
        topBanner.start();

        //banner设置方法全部调用完毕时最后调用
        topBanner.setOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
            }

            @Override
            public void onPageSelected(int position) {
            }

            @Override
            public void onPageScrollStateChanged(int state) {
            }
        });
        topBanner.setOnBannerListener(new OnBannerListener() {
            @Override
            public void OnBannerClick(int position) {
                if (advertList.size() == 0) {
                    return;
                }
                try {
                    BeanBanner banner = advertList.get(position);
                    if (banner.getTurn_type().equals("1") && !banner.getUrl().isEmpty()) {
                        Intent intent = new Intent(activity, WebViewActivity.class);
                        intent.putExtra(ExtraKey.WEB_URL, banner.getUrl());
                        activity.startActivity(intent);
                        BannerHelper.postShowBanner(banner, "2");
                    }
                } catch (Exception ex) {

                }
            }
        });

        initSchoolItem();

        mToolbar.setAlpha(0);
        ptr_scrollview.setMode(PullToRefreshBase.Mode.PULL_FROM_START);
        ptr_scrollview.setOnRefreshListener(new PullToRefreshBase.OnRefreshListener2<ScrollView>() {
            @Override
            public void onPullDownToRefresh(PullToRefreshBase<ScrollView> refreshView) {
                ptr_scrollview.onRefreshComplete();
                if (!NetWorkUsefulUtils.getActiveNetwork(getContext())) {
                    Toast.makeText(getContext(), "网络不可用", Toast.LENGTH_SHORT).show();
                } else {
                    initData();
                }
            }

            @Override
            public void onPullUpToRefresh(PullToRefreshBase<ScrollView> refreshView) {

            }
        });

        ptr_scrollview.setOnScrollChangedListener(new PullToRefreshScrollView.OnScrollChangedListener() {
            @Override
            public void onScrollChanged(PullToRefreshScrollView who, int x, int y, int oldl, int oldt) {
                float alpha = 0;
//                if (oldt <= 180) {
//                    alpha = ((float) oldt) / 180;
//                    mToolbar.setAlpha(alpha);
//                } else {
//                    mToolbar.setAlpha(1);
//                }

            }
        });

        UserHelper.getInstance().addUserChangeListener(new UserHelper.UserChangeListener() {
            @Override
            public void onUserLoginSuccess() {
                //用户切换后，重新获取广告位信息
                reloadPageData();
            }

            @Override
            public void onUserExit() {
                reloadPageData();
            }
        });

    }

    /*
     * 获取广告位
     * */
    public void getBanners() {
        Log.i(TAG, "getBanners: ");
        String s_id = "";
        String cityName = SharedPreferencesUtil.getData(XPTApplication.getInstance(), SharedPreferencesUtil.KEY_CITY, "").toString();

        String url = HttpAction.HOME_Banner;
        VolleyHttpService.getInstance().sendPostRequest(url, new MyVolleyHttpParamsEntity()
                .addParam("s_id", s_id)
                .addParam("area_name", cityName), new MyVolleyRequestListener() {
            @Override
            public void onStart() {
                super.onStart();
            }

            @Override
            public void onResponse(VolleyHttpResult volleyHttpResult) {
                super.onResponse(volleyHttpResult);
                switch (volleyHttpResult.getStatus()) {
                    case HttpAction.SUCCESS:
                        try {
                            String info = volleyHttpResult.getData().toString();
                            Log.i(TAG, "onResponse: data " + info);
                            Gson gson = new Gson();
                            advertList = gson.fromJson(info, new TypeToken<List<BeanBanner>>() {
                            }.getType());
                            if (advertList.size() > 0) {
                                GreenDaoHelper.getInstance().insertBanner(advertList);
                            }
                            Log.i(TAG, "onResponse: size " + advertList.size());
                            reloadTopFragment(advertList);
                        } catch (Exception ex) {
                            Log.i(TAG, "onResponse: error " + ex.getMessage());
                            //错误
                            reloadTopFragment(GreenDaoHelper.getInstance().getBanners());
                        }
                        break;
                    default:
                        //获取失败后，读取本地数据
                        reloadTopFragment(GreenDaoHelper.getInstance().getBanners());
                        break;
                }

            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                reloadTopFragment(GreenDaoHelper.getInstance().getBanners());
            }
        });
    }

    public void reloadTopFragment(List<BeanBanner> banners) {

        List<String> listBannerImages = new ArrayList<>();
        List<String> listTitles = new ArrayList<>();

        for (int i = 0; i < banners.size(); i++) {
            listBannerImages.add(banners.get(i).getImg());
//            listTitles.add(banners.get(i).getTitle());
            listTitles.add("");
            Log.i(TAG, "reloadTopFragment: " + banners.get(i).getImg());
        }
        if (topBanner == null) {
            return;
        }
        topBanner.update(listBannerImages, listTitles);
    }

    private void getHomeGroupCfg() {
        Log.i(TAG, "getHomeGroupCfg: ");

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Home_GroupCfg, new MyVolleyHttpParamsEntity(),
                new VolleyRequestListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    JSONObject jsonData = new JSONObject(volleyHttpResult.getData().toString());
                                    Gson gson = new Gson();

                                    //合作产品
                                    List<BeanHomeCfg> children_goods = gson.fromJson(jsonData.getJSONArray(HomeUtil.CHILDREN_GOODS).toString(),
                                            new TypeToken<List<BeanHomeCfg>>() {
                                            }.getType());
                                    for (int i = 0; i < children_goods.size(); i++) {
                                        children_goods.get(i).setType(HomeUtil.CHILDREN_GOODS);
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(children_goods, HomeUtil.CHILDREN_GOODS);
                                    happyGrowView.bindData(children_goods);

                                    //智慧金融
                                    List<BeanHomeCfg> invests = gson.fromJson(jsonData.getJSONArray(HomeUtil.INVEST).toString(),
                                            new TypeToken<List<BeanHomeCfg>>() {
                                            }.getType());
                                    for (int i = 0; i < invests.size(); i++) {
                                        invests.get(i).setType(HomeUtil.INVEST);
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(invests, HomeUtil.INVEST);
                                    propertyView.bindData(invests);

                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: error " + ex.getMessage());
                                    //错误
//                                    initHomeCfg();
                                }
                                break;
                            default:
//                                initHomeCfg();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
//                        initHomeCfg();
                    }
                });
    }

    private void initHomeCfg() {
        happyGrowView.bindData(GreenDaoHelper.getInstance().getHomeCfgByType(HomeUtil.CHILDREN_GOODS));
        propertyView.bindData(GreenDaoHelper.getInstance().getHomeCfgByType(HomeUtil.INVEST));
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        activity = (MainActivity) context;
    }

    private void initSchoolItem() {
        Log.i(TAG, "initSchoolItem: ");
        List<HomeItem> homeItems = new ArrayList<HomeItem>();
        //监听设置
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_moniter)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_moniter))
                .setIntent(new Intent(activity, MoniterActivity.class)));
        //碰碰交友
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_friend)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_friend))
                .setIntent(new Intent(activity, FriendActivity.class)));

        //微聊
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_chat)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_chat))
                .setIntent(new Intent(activity, ChatListActivity.class)));
        //计步
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_walk)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_walk))
                .setIntent(new Intent(activity, WalkActivity.class)));

        //报警查询
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_alarm)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_alarm))
                .setIntent(new Intent(activity, AlarmActivity.class)));

        //电子围栏
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_fence)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_fence))
                .setIntent(new Intent(activity, FenceListActivity.class)));

        //远程关机
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_shutdown)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_shutdown))
                .setIntent(new Intent(activity, ShutDownActivity.class)));

        //闹钟设置
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_clock)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_clock))
                .setIntent(new Intent(activity, ClockActivity.class)));

        HomeItemGridAdapter itemAdapter = new HomeItemGridAdapter(activity);
        if (grd_school != null)
            grd_school.setAdapter(itemAdapter);
        itemAdapter.reloadData(homeItems);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onDestroyView() {
        unbinder.unbind();
        super.onDestroyView();
    }

}
