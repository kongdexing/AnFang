package com.shuhai.anfang.ui.fragment;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.volley.common.VolleyRequestListener;
import com.android.widget.mygridview.MyGridView;
import com.android.widget.pulltorefresh.PullToRefreshBase;
import com.android.widget.pulltorefresh.PullToRefreshScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.bean.HomeItem;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.ExtraKey;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.common.UserHelper;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.model.BeanBanner;
import com.shuhai.anfang.model.BeanHomeCfg;
import com.shuhai.anfang.model.BeanHotGood;
import com.shuhai.anfang.model.BeanTeacher;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.push.BannerHelper;
import com.shuhai.anfang.ui.alarm.AlarmActivity;
import com.shuhai.anfang.ui.alarm.AlarmTActivity;
import com.shuhai.anfang.ui.checkin.CheckinActivity;
import com.shuhai.anfang.ui.checkin.CheckinTActivity;
import com.shuhai.anfang.ui.fence.FenceListActivity;
import com.shuhai.anfang.ui.homework.HomeWorkParentActivity;
import com.shuhai.anfang.ui.homework.HomeWorkTeacherActivity;
import com.shuhai.anfang.ui.leave.LeaveActivity;
import com.shuhai.anfang.ui.leave.LeaveTActivity;
import com.shuhai.anfang.ui.main.WebViewActivity;
import com.shuhai.anfang.ui.notice.NoticeActivity;
import com.shuhai.anfang.ui.notice.NoticeTeacherActivity;
import com.shuhai.anfang.ui.score.ScoreActivity;
import com.shuhai.anfang.ui.score.ScoreTeacherActivity;
import com.shuhai.anfang.util.NetWorkUsefulUtils;
import com.shuhai.anfang.util.ParentUtil;
import com.shuhai.anfang.view.autoviewpager.GlideImageLoader;
import com.viewpagerindicator.CirclePageIndicator;
import com.youth.banner.Banner;
import com.youth.banner.listener.OnBannerListener;

import org.json.JSONArray;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.Unbinder;

public class HomeFragment extends BaseFragment {

    @BindView(R.id.fragmentHome_titleLinearId)
    RelativeLayout fragmentHome_titleLinearId;
    @BindView(R.id.txtTitle)
    TextView txtTitle;

    @BindView(R.id.scrollView)
    PullToRefreshScrollView ptr_scrollview;

    @BindView(R.id.rlTipAD)
    RelativeLayout rlTipAD;
    @BindView(R.id.topBanner)
    Banner topBanner;
    @BindView(R.id.indicator)
    CirclePageIndicator indicator;
    @BindView(R.id.tipTitle)
    TextView tipTitle;
    List<BeanBanner> advertList = new ArrayList<>();

    @BindView(R.id.llGroup)
    LinearLayout llGroup;

    @BindView(R.id.grd_school)
    MyGridView grd_school;
    HomeItemGridAdapter itemAdapter;

    @BindView(R.id.grd_school_shop)
    MyGridView grd_school_shop;
    HomeItemGridAdapter itemShopAdapter;

    @BindView(R.id.img_hot_good)
    ImageView img_hot_good;

    @BindView(R.id.img_hot_good2)
    ImageView img_hot_good2;

    @BindView(R.id.img_hot_good3)
    ImageView img_hot_good3;

    private Unbinder unbinder;
    //    private MyTopPagerAdapter topAdapter;
    private List<BeanBanner> topBanners = new ArrayList<>();

    public HomeFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        Log.i(TAG, "HomeFragment inflateView: ");
        mRootView = inflater.inflate(R.layout.fragment_home, container, false);
        unbinder = ButterKnife.bind(this, mRootView);
        initView();
        Log.i(TAG, "onCreateView start location: ");
        return mRootView;
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: startAutoPlay");
        topBanner.startAutoPlay();
        //根据登录状态显示不同icon
        //当状态有更改时才变化Item

    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: stopAutoPlay");
        topBanner.stopAutoPlay();
    }

    @Override
    protected void initData() {
        Log.i(TAG, "HomeFragment initData: ");
        //先获取本地数据进行展示
        reloadTopFragment(GreenDaoHelper.getInstance().getBanners());
        initHomeCfg();
        initHotGood();

        //1获取广告位，2获取分组数据，3获取商品推荐
        getBanners();
        //获取分组数据
        getHomeGroupCfg();
        //获取热门商品数据
        getHotGoods();
        UserHelper.getInstance().addUserChangeListener(new UserHelper.UserChangeListener() {
            @Override
            public void onUserLoginSuccess() {
                //用户切换后，重新获取广告位信息
                getBanners();
                //重新分配Intent
                initSchoolItem();
                initShopItem();
            }

            @Override
            public void onUserExit() {

            }
        });
    }

    private void initView() {
        Log.i(TAG, "HomeFragment initView: ");
        int width = XPTApplication.getInstance().getWindowWidth();

        int height = width / 2;
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
//        topBanner.setImages(new ArrayList<Integer>(){R.drawable.tip_ad_def});
//        topBanner.setImages(listBannerImages);
        topBanner.setImageLoader(new GlideImageLoader());
        //设置图片集合
        //设置banner动画效果
//        topBanner.setPageTransformer(true, new GalleryTransformer(getActivity()));
        //设置自动轮播，默认为true
        topBanner.isAutoPlay(true);
        //设置轮播时间
        topBanner.setDelayTime(3000);
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
                    if (banner.getTurn_type().equals("1")) {
                        Intent intent = new Intent(mContext, WebViewActivity.class);
                        intent.putExtra(ExtraKey.WEB_URL, banner.getUrl());
                        mContext.startActivity(intent);
                        BannerHelper.postShowBanner(banner, "2");
                    }
                } catch (Exception ex) {

                }
            }
        });

        initSchoolItem();

        initShopItem();

        fragmentHome_titleLinearId.setAlpha(0);
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
                if (oldt <= 180) {
                    alpha = ((float) oldt) / 180;
                    fragmentHome_titleLinearId.setAlpha(alpha);
                } else {
                    fragmentHome_titleLinearId.setAlpha(1);
                }
            }
        });

    }

    private void initShopItem() {
        List<HomeItem> homeShopItems = new ArrayList<HomeItem>();
        /*课程表*/
        homeShopItems.add(new HomeItem()
                .setIconId(R.drawable.home_course)
                .setTitle(getString(R.string.home_course))
                .setIntent(new Intent(mContext, LeaveActivity.class)));
        /*老师评语*/
        homeShopItems.add(new HomeItem()
                .setIconId(R.drawable.home_remark)
                .setTitle(getString(R.string.home_remark))
                .setIntent(new Intent(mContext, LeaveActivity.class)));
        /*荣誉墙*/
        homeShopItems.add(new HomeItem()
                .setIconId(R.drawable.home_honour)
                .setTitle(getString(R.string.home_honour))
                .setIntent(new Intent(mContext, LeaveActivity.class)));

        itemShopAdapter = new HomeItemGridAdapter(mContext);
        grd_school_shop.setAdapter(itemShopAdapter);
        itemShopAdapter.reloadData(homeShopItems);
    }

    private void initSchoolItem() {
        boolean isParent = false;
        if (XPTApplication.getInstance().isLoggedIn()) {
            if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                isParent = false;
            } else if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                isParent = true;
            }
        }

        List<HomeItem> homeItems = new ArrayList<HomeItem>();
        //家庭作业
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_homework)
                .setTitle(getString(R.string.home_homework))
                .setIntent(new Intent(mContext, isParent ? HomeWorkParentActivity.class : HomeWorkTeacherActivity.class)));
        //班级公告
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_notice)
                .setTitle(getString(R.string.home_notice))
                .setIntent(new Intent(mContext, isParent ? NoticeActivity.class : NoticeTeacherActivity.class)));
        //成绩
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_classes)
                .setTitle(getString(R.string.home_score))
                .setIntent(new Intent(mContext, isParent ? ScoreActivity.class : ScoreTeacherActivity.class)));
        //考勤
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_checkin)
                .setTitle(getString(R.string.home_checkin))
                .setIntent(new Intent(mContext, isParent ? CheckinActivity.class : CheckinTActivity.class)));

        //报警查询
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_alarm)
                .setTitle(getString(R.string.home_alarm))
                .setIntent(new Intent(mContext, isParent ? AlarmActivity.class : AlarmTActivity.class)));

        //电子围栏
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_fence)
                .setTitle(getString(R.string.home_fence))
                .setIntent(new Intent(mContext, FenceListActivity.class)));

        //在线请假
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_notice)
                .setTitle(getString(R.string.home_leave))
                .setIntent(new Intent(mContext, isParent ? LeaveActivity.class : LeaveTActivity.class)));

        Intent newsIntent = new Intent(mContext, WebViewActivity.class);
        newsIntent.putExtra(ExtraKey.WEB_URL, "http://school.xinpingtai.com/edunews/");
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_news)
                .setTitle(getString(R.string.home_news))
                .setIntent(newsIntent));

        itemAdapter = new HomeItemGridAdapter(mContext);

        grd_school.setAdapter(itemAdapter);
        itemAdapter.reloadData(homeItems);
    }

    /*
    * 获取广告位
    * */
    public void getBanners() {
        Log.i(TAG, "getBanners: ");
        String s_id = "";
        if (XPTApplication.getInstance().isLoggedIn()) {
            //家长登录
            if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                s_id = ParentUtil.getStuSid();
            } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                //老师登录
                BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
                if (teacher != null) {
                    s_id = teacher.getS_id();
                }
            }
        }

        if (s_id == null) {
            s_id = "";
        }

        String cityName = SharedPreferencesUtil.getData(mContext, SharedPreferencesUtil.KEY_CITY, "").toString();

        String url = HttpAction.HOME_Banner;
        VolleyHttpService.getInstance().sendPostRequest(url, new VolleyHttpParamsEntity()
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
            listTitles.add(banners.get(i).getTitle());
            Log.i(TAG, "reloadTopFragment: " + banners.get(i).getImg());
        }
        topBanner.update(listBannerImages, listTitles);
    }

    private void getHomeGroupCfg() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Home_GroupCfg,
                new VolleyHttpParamsEntity()
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Home_GroupCfg)),
                new VolleyRequestListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    String info = volleyHttpResult.getData().toString();
                                    Log.i(TAG, "onResponse: data " + info);
                                    Gson gson = new Gson();
                                    List<BeanHomeCfg> beanHomeCfgs = gson.fromJson(info, new TypeToken<List<BeanHomeCfg>>() {
                                    }.getType());
                                    //删除所有子项
                                    GreenDaoHelper.getInstance().deleteHomeChildCfg();
                                    if (beanHomeCfgs.size() > 0) {
                                        for (int i = 0; i < beanHomeCfgs.size(); i++) {
                                            GreenDaoHelper.getInstance().insertHomeChildCfg(beanHomeCfgs.get(i).getChild());
                                        }
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(beanHomeCfgs);
                                    Log.i(TAG, "onResponse: size " + beanHomeCfgs.size());
                                    initHomeCfg();
//                                    bindHotGood(beanHomeCfgs.get(0));
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: error " + ex.getMessage());
                                    //错误
                                    initHomeCfg();
                                }
                                break;
                            default:
                                initHomeCfg();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        initHomeCfg();
                    }
                });
    }

    private void initHomeCfg() {
        List<BeanHomeCfg> beanHomeCfgs = GreenDaoHelper.getInstance().getHomeCfg();
        llGroup.removeAllViews();
        for (int i = 0; i < beanHomeCfgs.size(); i++) {
            HomeGroupView view = new HomeGroupView(mContext);
            view.bindData(beanHomeCfgs.get(i));
            llGroup.addView(view);
        }
    }

    /*获取推荐商品*/
    private void getHotGoods() {
        Log.i(TAG, "getHotGoods: ");
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.GETHotGoods,
                new VolleyHttpParamsEntity()
                        .addParam("token", CommonUtil.encryptToken(HttpAction.GETHotGoods)), new VolleyRequestListener() {
                    @Override
                    public void onStart() {

                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    String info = volleyHttpResult.getData().toString();
                                    Log.i(TAG, "onResponse: data " + info);

                                    JSONArray array = new JSONArray(info);
                                    List<BeanHotGood> hotGoods = new ArrayList<>();
                                    for (int i = 0; i < array.length(); i++) {
                                        JSONObject object = array.getJSONObject(i);
                                        BeanHotGood hotGood = new BeanHotGood();
                                        hotGood.setId(object.getString("id"));
                                        hotGood.setImage(object.getString("image"));
                                        hotGood.setUrl_pc_short(object.getString("url_pc_short"));
                                        hotGoods.add(hotGood);
                                    }

                                    if (hotGoods.size() > 0) {
                                        GreenDaoHelper.getInstance().insertHotGoods(hotGoods);
                                    }
                                    Log.i(TAG, "onResponse: size " + hotGoods.size());
                                    bindHotGood(hotGoods);
                                } catch (Exception ex) {
                                    Log.i(TAG, "onResponse: error " + ex.getMessage());
                                    //错误
                                    initHotGood();
                                }
                                break;
                            default:
                                initHotGood();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        initHotGood();
                    }
                });
    }

    private void initHotGood() {
        List<BeanHotGood> hotGoods = GreenDaoHelper.getInstance().getHotGoods();
        if (hotGoods.size() > 0) {
            bindHotGood(hotGoods);
        } else {
            img_hot_good.setVisibility(View.GONE);
        }
    }

    private void bindHotGood(final List<BeanHotGood> hotGoods) {
        if (hotGoods == null || img_hot_good == null) {
            return;
        }

        try {
            ImageLoader.getInstance().displayImage(hotGoods.get(0).getImage(),
                    new ImageViewAware(img_hot_good), CommonUtil.getDefaultImageLoaderOption());
            img_hot_good.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(ExtraKey.WEB_URL, hotGoods.get(0).getUrl_pc_short());
                    mContext.startActivity(intent);
                }
            });

            ImageLoader.getInstance().displayImage(hotGoods.get(1).getImage(),
                    new ImageViewAware(img_hot_good2), CommonUtil.getDefaultImageLoaderOption());
            img_hot_good2.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(ExtraKey.WEB_URL, hotGoods.get(1).getUrl_pc_short());
                    mContext.startActivity(intent);
                }
            });

            ImageLoader.getInstance().displayImage(hotGoods.get(2).getImage(),
                    new ImageViewAware(img_hot_good3), CommonUtil.getDefaultImageLoaderOption());
            img_hot_good3.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(mContext, WebViewActivity.class);
                    intent.putExtra(ExtraKey.WEB_URL, hotGoods.get(2).getUrl_pc_short());
                    mContext.startActivity(intent);
                }
            });
        } catch (Exception ex) {

        }
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
