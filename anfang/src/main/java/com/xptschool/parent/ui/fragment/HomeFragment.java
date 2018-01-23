package com.xptschool.parent.ui.fragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.support.v4.graphics.ColorUtils;
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
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.volley.common.VolleyRequestListener;
import com.android.widget.mygridview.MyGridView;
import com.android.widget.pulltorefresh.PullToRefreshBase;
import com.android.widget.pulltorefresh.PullToRefreshScrollView;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.gyf.barlibrary.BarHide;
import com.gyf.barlibrary.ImmersionBar;
import com.viewpagerindicator.CirclePageIndicator;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.bean.HomeItem;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanBanner;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.push.BannerHelper;
import com.xptschool.parent.ui.alarm.AlarmActivity;
import com.xptschool.parent.ui.alarm.AlarmTActivity;
import com.xptschool.parent.ui.checkin.CheckinPActivity;
import com.xptschool.parent.ui.checkin.CheckinTActivity;
import com.xptschool.parent.ui.comment.CommentPActivity;
import com.xptschool.parent.ui.comment.CommentTActivity;
import com.xptschool.parent.ui.course.CourseActivity;
import com.xptschool.parent.ui.course.CourseTActivity;
import com.xptschool.parent.ui.fence.FenceListActivity;
import com.xptschool.parent.ui.fragment.home.HomeEduGroupView;
import com.xptschool.parent.ui.fragment.home.HomeEduView;
import com.xptschool.parent.ui.fragment.home.HomeHappyGroupView;
import com.xptschool.parent.ui.fragment.home.HomeHappyGrowView;
import com.xptschool.parent.ui.fragment.home.HomePayMentView;
import com.xptschool.parent.ui.fragment.home.HomePropertyView;
import com.xptschool.parent.ui.fragment.home.HomeShopView;
import com.xptschool.parent.ui.homework.HomeWorkParentActivity;
import com.xptschool.parent.ui.homework.HomeWorkTeacherActivity;
import com.xptschool.parent.ui.honor.HonorPActivity;
import com.xptschool.parent.ui.honor.HonorTActivity;
import com.xptschool.parent.ui.leave.LeaveActivity;
import com.xptschool.parent.ui.leave.LeaveTActivity;
import com.xptschool.parent.ui.main.WebViewActivity;
import com.xptschool.parent.ui.notice.NoticeActivity;
import com.xptschool.parent.ui.notice.NoticeTeacherActivity;
import com.xptschool.parent.ui.score.ScoreActivity;
import com.xptschool.parent.ui.score.ScoreTeacherActivity;
import com.xptschool.parent.util.HomeUtil;
import com.xptschool.parent.util.NetWorkUsefulUtils;
import com.xptschool.parent.util.ParentUtil;
import com.xptschool.parent.view.autoviewpager.GlideImageLoader;
import com.youth.banner.Banner;
import com.youth.banner.BannerConfig;
import com.youth.banner.listener.OnBannerListener;

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

    //教育培训,快乐成长,理财投资,校园购,生活缴费
    @BindView(R.id.eduGroupView)
    HomeEduGroupView eduGroupView;
    @BindView(R.id.happyGrowView)
    HomeHappyGroupView happyGrowView;
    @BindView(R.id.propertyView)
    HomePropertyView propertyView;
    @BindView(R.id.shopView)
    HomeShopView shopView;
    @BindView(R.id.paymentView)
    HomePayMentView paymentView;

    @BindView(R.id.grd_tip)
    MyGridView grd_tip;
    @BindView(R.id.grd_school)
    MyGridView grd_school;

//    protected ImmersionBar mImmersionBar;

    private Unbinder unbinder;
    //    private MyTopPagerAdapter topAdapter;
    private List<BeanBanner> topBanners = new ArrayList<>();

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

        //1获取广告位，2获取分组数据，3获取商品推荐
        getBanners();
        //获取分组数据
        getHomeGroupCfg();

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

    public void reloadPageData() {
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
                    if (banner.getTurn_type().equals("1")) {
                        Intent intent = new Intent(getActivity(), WebViewActivity.class);
                        intent.putExtra(ExtraKey.WEB_URL, banner.getUrl());
                        getActivity().startActivity(intent);
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

        String cityName = SharedPreferencesUtil.getData(XPTApplication.getInstance(), SharedPreferencesUtil.KEY_CITY, "").toString();

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

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Home_GroupCfg, new VolleyHttpParamsEntity(),
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

                                    List<BeanHomeCfg> onlines = gson.fromJson(jsonData.getJSONArray(HomeUtil.ONLINE_VIDEO).toString(),
                                            new TypeToken<List<BeanHomeCfg>>() {
                                            }.getType());
                                    for (int i = 0; i < onlines.size(); i++) {
                                        onlines.get(i).setType(HomeUtil.ONLINE_VIDEO);
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(onlines, HomeUtil.ONLINE_VIDEO);
                                    eduGroupView.initEduOnLine(onlines);

                                    List<BeanHomeCfg> children_goods = gson.fromJson(jsonData.getJSONArray(HomeUtil.CHILDREN_GOODS).toString(),
                                            new TypeToken<List<BeanHomeCfg>>() {
                                            }.getType());
                                    for (int i = 0; i < children_goods.size(); i++) {
                                        children_goods.get(i).setType(HomeUtil.CHILDREN_GOODS);
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(children_goods, HomeUtil.CHILDREN_GOODS);
                                    happyGrowView.bindData(children_goods);

                                    List<BeanHomeCfg> invests = gson.fromJson(jsonData.getJSONArray(HomeUtil.INVEST).toString(),
                                            new TypeToken<List<BeanHomeCfg>>() {
                                            }.getType());
                                    for (int i = 0; i < invests.size(); i++) {
                                        invests.get(i).setType(HomeUtil.INVEST);
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(invests, HomeUtil.INVEST);
                                    propertyView.bindData(invests);

                                    //校园购
                                    List<BeanHomeCfg> shops = gson.fromJson(jsonData.getJSONArray(HomeUtil.SHOPPING).toString(),
                                            new TypeToken<List<BeanHomeCfg>>() {
                                            }.getType());
                                    for (int i = 0; i < shops.size(); i++) {
                                        shops.get(i).setType(HomeUtil.SHOPPING);
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(shops, HomeUtil.SHOPPING);
                                    shopView.bindData(shops);

                                    //生活缴费
                                    List<BeanHomeCfg> payments = gson.fromJson(jsonData.getJSONArray(HomeUtil.LIVING_PAYMENT).toString(),
                                            new TypeToken<List<BeanHomeCfg>>() {
                                            }.getType());
                                    for (int i = 0; i < payments.size(); i++) {
                                        payments.get(i).setType(HomeUtil.LIVING_PAYMENT);
                                    }
                                    GreenDaoHelper.getInstance().insertHomeCfg(payments, HomeUtil.LIVING_PAYMENT);
                                    paymentView.bindData(payments);

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
        eduGroupView.initEduOnLine(GreenDaoHelper.getInstance().getHomeCfgByType(HomeUtil.ONLINE_VIDEO));
        happyGrowView.bindData(GreenDaoHelper.getInstance().getHomeCfgByType(HomeUtil.CHILDREN_GOODS));
        propertyView.bindData(GreenDaoHelper.getInstance().getHomeCfgByType(HomeUtil.INVEST));
        shopView.bindData(GreenDaoHelper.getInstance().getHomeCfgByType(HomeUtil.SHOPPING));
        paymentView.bindData(GreenDaoHelper.getInstance().getHomeCfgByType(HomeUtil.LIVING_PAYMENT));
    }

    private void initSchoolItem() {
        boolean isParent = false;
        if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            isParent = false;
        } else if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            isParent = true;
        }

        List<HomeItem> tipItems = new ArrayList<>();
         /*课程表*/
        tipItems.add(new HomeItem()
                .setIconId(R.drawable.home_course)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_course))
                .setIntent(new Intent(getActivity(), isParent ? CourseActivity.class : CourseTActivity.class)));
        /*老师评语*/
        tipItems.add(new HomeItem()
                .setIconId(R.drawable.home_remark)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_comment))
                .setIntent(new Intent(getActivity(), isParent ? CommentPActivity.class : CommentTActivity.class)));
        /*教育新闻*/
        Intent newsIntent = new Intent(getActivity(), WebViewActivity.class);
        newsIntent.putExtra(ExtraKey.WEB_URL, "http://school.xinpingtai.com/edunews/");
        tipItems.add(new HomeItem()
                .setShowForParent(false)
                .setShowForTeacher(false)
                .setIconId(R.drawable.home_news)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_news))
                .setIntent(newsIntent));
        HomeItemGridAdapter tipAdapter = new HomeItemGridAdapter(getActivity());

        grd_tip.setAdapter(tipAdapter);
        tipAdapter.reloadData(tipItems);

        List<HomeItem> homeItems = new ArrayList<HomeItem>();
        //家庭作业
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_homework)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_homework))
                .setIntent(new Intent(getActivity(), isParent ? HomeWorkParentActivity.class : HomeWorkTeacherActivity.class)));
        //成绩
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_classes)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_score))
                .setIntent(new Intent(getActivity(), isParent ? ScoreActivity.class : ScoreTeacherActivity.class)));
        //电子围栏
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_fence)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_fence))
                .setIntent(new Intent(getActivity(), FenceListActivity.class)));
        //报警查询
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_alarm)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_alarm))
                .setIntent(new Intent(getActivity(), isParent ? AlarmActivity.class : AlarmTActivity.class)));

        //在线请假
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_notice)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_leave))
                .setIntent(new Intent(getActivity(), isParent ? LeaveActivity.class : LeaveTActivity.class)));
        //考勤
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_checkin)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_checkin))
                .setIntent(new Intent(getActivity(), isParent ? CheckinPActivity.class : CheckinTActivity.class)));
        //班级公告
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_notice)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_notice))
                .setIntent(new Intent(getActivity(), isParent ? NoticeActivity.class : NoticeTeacherActivity.class)));

        /*荣誉墙*/
        homeItems.add(new HomeItem()
                .setIconId(R.drawable.home_honour)
                .setTitle(XPTApplication.getInstance().getResources().getString(R.string.home_honour))
                .setIntent(new Intent(getActivity(), isParent ? HonorPActivity.class : HonorTActivity.class)));

        HomeItemGridAdapter itemAdapter = new HomeItemGridAdapter(getActivity());

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
