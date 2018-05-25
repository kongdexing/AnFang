package com.xptschool.parent.ui.main;

import android.Manifest;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.content.LocalBroadcastManager;
import android.util.Log;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.DefaultRetryPolicy;
import com.android.volley.common.CookieUtil;
import com.baidu.location.BDLocation;
import com.baidu.location.BDLocationListener;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.gyf.barlibrary.ImmersionBar;
import com.hyphenate.EMCallBack;
import com.hyphenate.EMMessageListener;
import com.hyphenate.chat.EMClient;
import com.hyphenate.chat.EMMessage;
import com.hyphenate.util.EMLog;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.BroadcastAction;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.ease.Constant;
import com.xptschool.parent.ease.EaseHelper;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.fragment.HomeFragment;
import com.xptschool.parent.ui.fragment.MapFragment;
import com.xptschool.parent.ui.fragment.MessageFragment;
import com.xptschool.parent.ui.fragment.MineFragment;
import com.xptschool.parent.ui.login.LoginActivity;
import com.xptschool.parent.util.ToastUtils;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import butterknife.OnLongClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MainActivity extends BaseMainActivity implements BDLocationListener {

    private List<Fragment> fragmentList;
    private int currentTabIndex = 0;
    private Fragment mCurrentFgt, homeFragment, mapFragment, messageFragment, mineFragment;
    @BindView(R.id.fl_Content)
    FrameLayout fl_Content;

    @BindView(R.id.img_nav_home)
    ImageButton homeBtn;
    @BindView(R.id.img_nav_track)
    ImageButton mapBtn;
    @BindView(R.id.img_nav_message)
    ImageButton messageBtn;
    @BindView(R.id.img_nav_mine)
    ImageButton mineBtn;

    @BindView(R.id.txt_nav_home)
    TextView homeTxt;
    @BindView(R.id.txt_nav_track)
    TextView mapTxt;
    @BindView(R.id.txt_nav_message)
    TextView messageTxt;
    @BindView(R.id.txt_nav_mine)
    TextView mineTxt;

    @BindView(R.id.unread_msg_number)
    TextView unread_msg_number;

    private FragmentManager mFgtManager;
    private FragmentTransaction mFgtTransaction;
    private long mExitTime;
    public LocationClient mLocClient;
    protected ImmersionBar mImmersionBar;
    private android.app.AlertDialog.Builder exceptionBuilder;
    private boolean isExceptionDialogShow = false;

    private BroadcastReceiver broadcastReceiver;
    private LocalBroadcastManager broadcastManager;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mImmersionBar = ImmersionBar.with(this);
//        mImmersionBar.init();

        initView();
        initData();

        //register broadcast receiver to receive the change of group from DemoHelper
        registerBroadcastReceiver();

        //检测异地登录bug
        showExceptionDialogFromIntent(getIntent());
        //申请定位权限
        MainActivityPermissionsDispatcher.onLocationAllowWithCheck(this);
    }

    private void initView() {
        fl_Content = (FrameLayout) findViewById(R.id.fl_Content);

        IntentFilter filter = new IntentFilter(BroadcastAction.RELOAD_BANNER);
        this.registerReceiver(MyBannerReceiver, filter);

        // 定位初始化
        mLocClient = new LocationClient(this);
    }

    private void initData() {
        Log.i(TAG, "initData: ");
        fragmentList = new ArrayList<>();
        homeFragment = new HomeFragment();
        mapFragment = new MapFragment();
        messageFragment = new MessageFragment();
        mineFragment = new MineFragment();

        fragmentList.add(0, homeFragment);
        fragmentList.add(1, mapFragment);
        fragmentList.add(2, messageFragment);
        fragmentList.add(3, mineFragment);

        mFgtManager = getSupportFragmentManager();
        setInitialState();

        //login
        String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
        String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
        Log.i(TAG, "initData: userName:" + userName + "  pwd:" + password);

        if (!userName.isEmpty() && !password.isEmpty()) {
            login(userName, password, new DefaultRetryPolicy(4 * 1000, 0, 1));
        }
    }

    private void setInitialState() {
        resetNavBar();
        homeBtn.setSelected(true);
        homeTxt.setTextColor(getResources().getColor(R.color.text_black));

        mFgtTransaction = mFgtManager.beginTransaction();
        mCurrentFgt = fragmentList.get(0);
        mFgtTransaction.add(R.id.fl_Content, mCurrentFgt).commit();
    }

    @Override
    protected void onResume() {
        super.onResume();

        Log.i(TAG, "onResume: ");

        String cookie = CookieUtil.getCookie();
        if (cookie == null || cookie.isEmpty()) {
            Log.i(TAG, "onResume: cookie is null");

//            String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
//            String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
//            if (!userName.isEmpty() || !password.isEmpty()) {
//                login(userName, password);
//            }
        }

//        String userType = SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_TYPE, "").toString();
////        if (UserType.PARENT.toString().equals(userType) || UserType.TEACHER.toString().equals(userType)) {
////            updateUnreadLabel();
////            // unregister this event listener when this activity enters the
////            // background
////            EaseHelper sdkHelper = EaseHelper.getInstance();
////            sdkHelper.pushActivity(this);
////
////            EMClient.getInstance().chatManager().addMessageListener(messageListener);
////        }

    }

    @Override
    protected void onStop() {
        EMClient.getInstance().chatManager().removeMessageListener(messageListener);
        EaseHelper sdkHelper = EaseHelper.getInstance();
        sdkHelper.popActivity(this);
        super.onStop();
    }

    //QQ，新浪
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        UMShareAPI.get(this).onActivityResult(requestCode, resultCode, data);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null && permissions.length > 0) {
            Log.i(TAG, "onRequestPermissionsResult: " + permissions[0]);
        }
        MainActivityPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);

        if (Build.VERSION.SDK_INT >= 23) {
            String[] mPermissionList = new String[]{Manifest.permission.WRITE_EXTERNAL_STORAGE,
                    Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.CALL_PHONE,
                    Manifest.permission.READ_LOGS, Manifest.permission.READ_PHONE_STATE,
                    Manifest.permission.READ_EXTERNAL_STORAGE, Manifest.permission.SET_DEBUG_APP,
                    Manifest.permission.SYSTEM_ALERT_WINDOW, Manifest.permission.GET_ACCOUNTS,
                    Manifest.permission.WRITE_APN_SETTINGS};
            ActivityCompat.requestPermissions(this, mPermissionList, 123);
        }

    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationAllow() {
        Log.i(TAG, "onLocationAllow: ");
        try {
            mLocClient.registerLocationListener(this);
            LocationClientOption option = new LocationClientOption();
            option.setIsNeedAddress(true);
            option.setCoorType("bd09ll"); // 设置坐标类型
            option.setScanSpan(3000);
            mLocClient.setLocOption(option);
            mLocClient.start();
        } catch (Exception ex) {
            Log.e(TAG, "onLocationAllow: " + ex.getMessage());
        }
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationDenied() {
        Log.i(TAG, "onLocationDenied: ");
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this, R.string.permission_location_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        Log.i(TAG, "showRationaleForLocation: ");
        request.proceed();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationNeverAskAgain() {
        Log.i(TAG, "onLocationNeverAskAgain: ");
        Toast.makeText(this, R.string.permission_location_never_askagain, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onReceiveLocation(BDLocation bdLocation) {
//        ToastUtils.showToast(this, bdLocation.getCityCode() + bdLocation.getCity());
        if (bdLocation == null || bdLocation.getCity() == null) {
            Log.i(TAG, "onReceiveLocation: bdLocation is null");
            mLocClient.stop();
            return;
        }
        Log.i(TAG, "onReceiveLocation: 省：" + bdLocation.getProvince() + "  市： " + bdLocation.getCity());
        //保存本地城市
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_PROVINCE, bdLocation.getProvince());
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_CITY, bdLocation.getCity());

        if (homeFragment != null) {
            ((HomeFragment) homeFragment).getBanners();
        }
        mLocClient.stop();
    }

    @OnLongClick({R.id.nav_home, R.id.nav_track, R.id.nav_mine})
    public boolean buttonOnLongClick(View view) {
        viewClick(view);
        return true;
    }

    @OnClick({R.id.nav_home, R.id.nav_track, R.id.nav_message, R.id.nav_mine})
    public void viewOnclick(View view) {
        viewClick(view);
    }

    public void viewClick(View view) {
        resetNavBar();
        switch (view.getId()) {
            case R.id.nav_home:
                homeBtn.setSelected(true);
                homeTxt.setTextColor(getResources().getColor(R.color.text_black));
                addOrReplaceFgt(0);
                break;
            case R.id.nav_track:
                mapBtn.setSelected(true);
                mapTxt.setTextColor(getResources().getColor(R.color.text_black));
                addOrReplaceFgt(1);
                break;
            case R.id.nav_message:
                messageBtn.setSelected(true);
                messageTxt.setTextColor(getResources().getColor(R.color.text_black));
                addOrReplaceFgt(2);
                break;
            case R.id.nav_mine:
                mineBtn.setSelected(true);
                mineTxt.setTextColor(getResources().getColor(R.color.text_black));
                addOrReplaceFgt(3);
                break;
        }
    }

    public void resetNavBar() {
        homeBtn.setSelected(false);
        mapBtn.setSelected(false);
        messageBtn.setSelected(false);
        mineBtn.setSelected(false);

        homeTxt.setTextColor(getResources().getColor(R.color.color_black_6));
        mapTxt.setTextColor(getResources().getColor(R.color.color_black_6));
        messageTxt.setTextColor(getResources().getColor(R.color.color_black_6));
        mineTxt.setTextColor(getResources().getColor(R.color.color_black_6));
    }

    private void addOrReplaceFgt(int position) {
        currentTabIndex = position;
        if (mCurrentFgt == fragmentList.get(position)) {
            Log.e(TAG, "当前Fragment相同，不需要切换");
            return;
        }

        mFgtTransaction = mFgtManager.beginTransaction();
        if (fragmentList.get(position).isAdded()) {
            mFgtTransaction.hide(mCurrentFgt).show(fragmentList.get(position)).commit();
        } else {
            mFgtTransaction.hide(mCurrentFgt).add(R.id.fl_Content, fragmentList.get(position)).show(fragmentList.get(position)).commit();
        }
        Log.e(TAG, "Replace");
        mCurrentFgt.onPause();
        mCurrentFgt = fragmentList.get(position);
    }

    EMMessageListener messageListener = new EMMessageListener() {

        @Override
        public void onMessageReceived(List<EMMessage> messages) {
            // notify new message
            for (EMMessage message : messages) {
                EaseHelper.getInstance().getNotifier().onNewMsg(message);
            }
            refreshUIWithMessage();
        }

        @Override
        public void onCmdMessageReceived(List<EMMessage> messages) {
            //red packet code : 处理红包回执透传消息
//            for (EMMessage message : messages) {
//                EMCmdMessageBody cmdMsgBody = (EMCmdMessageBody) message.getBody();
//                final String action = cmdMsgBody.action();//获取自定义action
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)) {
//                    RedPacketUtil.receiveRedPacketAckMessage(message);
//                }
//            }
            //end of red packet code
            refreshUIWithMessage();
        }

        @Override
        public void onMessageRead(List<EMMessage> messages) {
        }

        @Override
        public void onMessageDelivered(List<EMMessage> message) {
        }

        @Override
        public void onMessageRecalled(List<EMMessage> messages) {
            refreshUIWithMessage();
        }

        @Override
        public void onMessageChanged(EMMessage message, Object change) {
        }
    };

    private void refreshUIWithMessage() {
        runOnUiThread(new Runnable() {
            public void run() {
                // refresh unread count
                updateUnreadLabel();
            }
        });
    }

    private void registerBroadcastReceiver() {
        broadcastManager = LocalBroadcastManager.getInstance(this);
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(Constant.ACTION_CONTACT_CHANAGED);
        intentFilter.addAction(Constant.ACTION_GROUP_CHANAGED);
        broadcastReceiver = new BroadcastReceiver() {

            @Override
            public void onReceive(Context context, Intent intent) {
                updateUnreadLabel();
                //red packet code : 处理红包回执透传消息
//                if (action.equals(RPConstant.REFRESH_GROUP_RED_PACKET_ACTION)){
//                    if (conversationListFragment != null){
//                        conversationListFragment.refresh();
//                    }
//                }
                //end of red packet code
            }
        };
        broadcastManager.registerReceiver(broadcastReceiver, intentFilter);
    }

    /**
     * update unread message count
     */
    public void updateUnreadLabel() {
        int count = EMClient.getInstance().chatManager().getUnreadMsgsCount();
        if (count > 0) {
            unread_msg_number.setText(String.valueOf(count));
            unread_msg_number.setVisibility(View.VISIBLE);
        } else {
            unread_msg_number.setVisibility(View.INVISIBLE);
        }

        //刷新消息列表
        if (currentTabIndex == 2) {
            // refresh conversation list
            if (messageFragment != null) {
                ((MessageFragment) messageFragment).getConversationListFragment().refresh();
            }
        }
    }

//    private void login(final String account, final String password) {
//        VolleyHttpService.getInstance().sendPostRequest(HttpAction.LOGIN,
//                new MyVolleyHttpParamsEntity()
//                        .addParam("username", account)
//                        .addParam("password", password),
//                new MyVolleyRequestListener() {
//                    @Override
//                    public void onStart() {
//                    }
//
//                    @Override
//                    public void onResponse(VolleyHttpResult httpResult) {
//                        super.onResponse(httpResult);
//                        switch (httpResult.getStatus()) {
//                            case HttpAction.SUCCESS:
//                                try {
//                                    CommonUtil.analyseLoginData(httpResult, account);
//
//                                    Intent intent = new Intent(MainActivity.this, MainActivity.class);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
//                                    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
//                                    startActivity(intent);
//                                } catch (Exception ex) {
//                                    Log.i(TAG, "onResponse: error " + ex.getMessage());
//                                    toLogin();
//                                }
//                                break;
//                            default:
//                                toLogin();
//                                break;
//                        }
//                    }
//
//                    @Override
//                    public void onErrorResponse(VolleyError error) {
//                        Log.i(TAG, "onErrorResponse: " + error);
//                        toLogin();
//                    }
//                });
//    }

    private void toLogin() {
        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
        intent.putExtra(ExtraKey.LOGIN_ORIGIN, "0");
        startActivity(intent);
    }

    @Override
    protected void onStartLogin() {
        super.onStartLogin();
    }

    @Override
    protected void onLoginSuccess(String newAccount) {
        super.onLoginSuccess(newAccount);
        //登录成功后，根据用户角色获取广告位
        Log.i(TAG, "login onLoginSuccess: ");
        if (homeFragment != null) {
            ((HomeFragment) homeFragment).reloadPageData();
        }

        String userId = GreenDaoHelper.getInstance().getCurrentParent().getUser_id();

        Log.i(TAG, "onLoginSuccess: login to em");

        EMClient.getInstance().login(userId, CommonUtil.md5("SHUHAIXINXI" + userId), new EMCallBack() {

            @Override
            public void onSuccess() {
                EMLoginSuccess();
                Log.d(TAG, "登录聊天服务器成功！");
            }

            @Override
            public void onProgress(int progress, String status) {
                Log.i(TAG, "onProgress: " + progress + "  status:" + status);
            }

            @Override
            public void onError(final int code, final String error) {
                if (code == 200) {
                    //USER_ALREADY_LOGIN
                    EMLoginSuccess();
                    Log.d(TAG, "USER_ALREADY_LOGIN！");
                } else {
                    runOnUiThread(new Runnable() {
                        public void run() {
                            Log.i(TAG, "EMUI onError: " + code + " error:" + error);
                            ToastUtils.showToast(getApplicationContext(), "环信 login failed");
                        }
                    });
                }
            }
        });
    }

    private void EMLoginSuccess() {
        EMClient.getInstance().groupManager().loadAllGroups();
        EMClient.getInstance().chatManager().loadAllConversations();
    }

    @Override
    protected void onLoginFailed(String msg) {
        super.onLoginFailed(msg);
        if (homeFragment != null) {
            ((HomeFragment) homeFragment).reloadPageData();
        }
    }

    private int getExceptionMessageId(String exceptionType) {
        if (exceptionType.equals(Constant.ACCOUNT_CONFLICT)) {
            return R.string.connect_conflict;
        } else if (exceptionType.equals(Constant.ACCOUNT_REMOVED)) {
            return R.string.em_user_remove;
        } else if (exceptionType.equals(Constant.ACCOUNT_FORBIDDEN)) {
            return R.string.user_forbidden;
        }
        return R.string.Network_error;
    }

    /**
     * show the dialog when user met some exception: such as login on another device, user removed or user forbidden
     */
    private void showExceptionDialog(String exceptionType) {
        isExceptionDialogShow = true;
        EaseHelper.getInstance().logout(false, null);
        String st = getResources().getString(R.string.Logoff_notification);
        if (!MainActivity.this.isFinishing()) {
            // clear up global variables
            try {
                if (exceptionBuilder == null)
                    exceptionBuilder = new android.app.AlertDialog.Builder(MainActivity.this);
                exceptionBuilder.setTitle(st);
                exceptionBuilder.setMessage(getExceptionMessageId(exceptionType));
                exceptionBuilder.setPositiveButton(R.string.ok, new DialogInterface.OnClickListener() {

                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                        exceptionBuilder = null;
                        isExceptionDialogShow = false;
                        //清除数据
                        SharedPreferencesUtil.clearUserInfo(MainActivity.this);
                        UserHelper.getInstance().userExit();

                        Intent intent = new Intent(MainActivity.this, LoginActivity.class);
//                        intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_NEW_TASK);
                        startActivity(intent);
                    }
                });
                exceptionBuilder.setCancelable(false);
                exceptionBuilder.create().show();
//                isConflict = true;
            } catch (Exception e) {
                EMLog.e(TAG, "---------color conflictBuilder error" + e.getMessage());
            }
        }
    }

    private void showExceptionDialogFromIntent(Intent intent) {
        EMLog.e(TAG, "showExceptionDialogFromIntent");
        if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_CONFLICT, false)) {
            showExceptionDialog(Constant.ACCOUNT_CONFLICT);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_REMOVED, false)) {
            showExceptionDialog(Constant.ACCOUNT_REMOVED);
        } else if (!isExceptionDialogShow && intent.getBooleanExtra(Constant.ACCOUNT_FORBIDDEN, false)) {
            showExceptionDialog(Constant.ACCOUNT_FORBIDDEN);
        } else if (intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_CHANGE_PASSWORD, false) ||
                intent.getBooleanExtra(Constant.ACCOUNT_KICKED_BY_OTHER_DEVICE, false)) {
            this.finish();
            startActivity(new Intent(this, LoginActivity.class));
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        showExceptionDialogFromIntent(intent);
    }

    public void shareLocation(String locationUrl) {
        String locationShare = "点击查看此点位置" + locationUrl;
        new ShareAction(this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)
                .withText(locationShare).setCallback(null).open();
    }

    BroadcastReceiver MyBannerReceiver = new BroadcastReceiver() {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(BroadcastAction.RELOAD_BANNER)) {
                //广告过期后，重新获取广告位
                if (homeFragment != null) {
                    ((HomeFragment) homeFragment).getBanners();
                }
            }
        }
    };

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (exceptionBuilder != null) {
            exceptionBuilder.create().dismiss();
            exceptionBuilder = null;
            isExceptionDialogShow = false;
        }
        broadcastManager.unregisterReceiver(broadcastReceiver);
        try {
            this.unregisterReceiver(MyBannerReceiver);
            if (mImmersionBar != null)
                mImmersionBar.destroy();  //在BaseActivity里销毁
        } catch (Exception ex) {

        }
    }

    @Override
    public void onBackPressed() {
        if ((System.currentTimeMillis() - mExitTime) > 2000) {
            Toast.makeText(this, R.string.toast_exit, Toast.LENGTH_SHORT).show();
            mExitTime = System.currentTimeMillis();
        } else {
            super.onBackPressed();
        }
    }
}