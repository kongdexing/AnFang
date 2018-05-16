package com.xptschool.parent;

import android.app.Application;
import android.content.Context;
import android.content.Intent;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.common.VolleyHttpService;
import com.android.widget.audiorecorder.AudioManager;
import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chat.EMOptions;
import com.hyphenate.easeui.utils.EaseLocalUserHelper;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.PushAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.umeng.socialize.Config;
import com.umeng.socialize.PlatformConfig;
import com.umeng.socialize.UMShareAPI;
import com.uuzuche.lib_zxing.activity.ZXingLibrary;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.ease.EaseHelper;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.alarm.AlarmMapActivity;
import com.xptschool.parent.ui.album.LocalImagePHelper;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.ui.checkin.CheckinPActivity;
import com.xptschool.parent.ui.homework.HomeWorkDetailParentActivity;
import com.xptschool.parent.ui.honor.HonorDetailActivity;
import com.xptschool.parent.ui.leave.LeavePDetailActivity;
import com.xptschool.parent.ui.leave.LeaveTDetailActivity;
import com.xptschool.parent.ui.main.MainActivity;
import com.xptschool.parent.ui.notice.NoticeDetailActivity;
import com.xptschool.parent.util.ToastUtils;

import org.json.JSONObject;

import java.io.File;
import java.net.Proxy;
import java.util.List;

public class XPTApplication extends Application {

    //小米推送
    public static final String APP_MIPUSH_ID = "2882303761517599079";
    public static final String APP_MIPUSH_KEY = "5961759967079";
    //魅族推送
    public static final String MZ_APP_ID = "111065";
    public static final String MZ_APP_KEY = "5e44c6c91687442091c945f29c888b18";
    //bugly
    public static final String APP_ID = "3e1429a7a5"; // TODO bugly上注册的appid
    private static XPTApplication mInstance;

    public static final String WXAPP_ID = "wx1af4f660ce9e6b37";
    private Display display;
    public static String TAG = XPTApplication.class.getSimpleName();
    private static String currentWatchIMEI = "";
    private static BeanStudent currentWatchStu = null;

    {
        PlatformConfig.setWeixin("wx1af4f660ce9e6b37", "5bb696d9ccd75a38c8a0bfe0675559b3");
        PlatformConfig.setQQZone("1106232927", "c7394704798a158208a74ab60104f0ba");
        PlatformConfig.setSinaWeibo("3921700954", "04b48b094faeb16683c32669824ebdad", "http://sns.whalecloud.com");
    }

    /**
     * bugly打包
     *
     * @param base
     */
    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        // you must install multiDex whatever tinker is installed!
        MultiDex.install(base);
        // 安装tinker
        Beta.installTinker();
    }

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        init();
        initBugly();
    }

    public static XPTApplication getInstance() {
        return mInstance;
    }

    private void init() {
        initImageLoader(getInstance());
        LocalImagePHelper.init(this);
        LocalImageTHelper.init(this);

        VolleyHttpService.init(this);

        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }

//        AdSdk.initialize(this, "2882303761517634324");

        //baidumap
        SDKInitializer.initialize(this);
        //本地数据库
        GreenDaoHelper.getInstance().initGreenDao(this);
        EaseLocalUserHelper.getInstance().initGreenDao(this);

        MobclickAgent.setCheckDevice(true);
        //日志加密设置
        MobclickAgent.enableEncrypt(true);

        //友盟
        final PushAgent mPushAgent = PushAgent.getInstance(this);
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
//              @param activity
//                |   notice  公告模块
//                |   leave   请假管理
//                |   homework 作业
//                |   warning  警报
//                |   attendance  考勤
//                |   remark   评语
//                |   honor  荣誉
//                @param id
                resolvePushMsg(msg.custom);
            }
        };
        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        AudioManager.getInstance(getCachePath());
        FileDownloader.init(getApplicationContext(), new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                )));

        //环信sdk集成
        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//        options.setAutoDownloadThumbnail(true);

        //init demo helper
        EaseHelper.getInstance().init(mInstance);
        Config.DEBUG = false;
        try {
            UMShareAPI.get(this);
        } catch (Exception ex) {
            Log.i(TAG, "init: UMShareAPI " + ex.getMessage());
        }
        ZXingLibrary.initDisplayOpinion(this);
    }

    public void resolvePushMsg(String message) {
        Log.i(TAG, "resolvePushMsg: " + message);
        try {
            JSONObject object = new JSONObject(message);
//                    Map<String, String> msgExtra = msg.extra;
            String activity = object.getString("activity");
            String id = object.getString("id");

            Log.i(TAG, "dealWithCustomAction: " + message);
            Log.i(TAG, "activity: " + activity + " id:" + id);
            if ("homework".equals(activity)) {
                Intent intent = new Intent(XPTApplication.this, HomeWorkDetailParentActivity.class);
                intent.putExtra(ExtraKey.DETAIL_ID, id);
                startActivity(intent);
            } else if ("notice".equals(activity)) {
                Intent intent = new Intent(XPTApplication.this, NoticeDetailActivity.class);
                intent.putExtra(ExtraKey.DETAIL_ID, id);
                startActivity(intent);
            } else if ("attendance".equals(activity)) {
                Intent intent = new Intent(XPTApplication.this, CheckinPActivity.class);
                intent.putExtra(ExtraKey.DETAIL_ID, id);
                startActivity(intent);
            } else if ("leave".equals(activity)) {
                if (getCurrent_user_type().equals(UserType.PARENT)) {
                    Intent intent = new Intent(XPTApplication.this, LeavePDetailActivity.class);
                    intent.putExtra(ExtraKey.DETAIL_ID, id);
                    startActivity(intent);
                } else if (getCurrent_user_type().equals(UserType.TEACHER)) {
                    Intent intent = new Intent(XPTApplication.this, LeaveTDetailActivity.class);
                    intent.putExtra(ExtraKey.DETAIL_ID, id);
                    startActivity(intent);
                }
            } else if ("warning".equals(activity)) {
                Intent intent = new Intent(XPTApplication.this, AlarmMapActivity.class);
                intent.putExtra(ExtraKey.DETAIL_ID, id);
                startActivity(intent);
            } else if ("honor".equals(activity)) {
                //荣誉
                Intent intent = new Intent(XPTApplication.this, HonorDetailActivity.class);
                intent.putExtra(ExtraKey.DETAIL_ID, id);
                startActivity(intent);
            } else if ("register".equals(activity)) {

                ToastUtils.showToast(XPTApplication.this, "您的用户注册成功了。。。");

            }
        } catch (Exception ex) {
            Log.i(TAG, "resolvePushMsg: " + ex.getMessage());
        }
    }

    private void initBugly() {
        // 设置开发设备
//        Bugly.setIsDevelopmentDevice(this, true);
        // 这里实现SDK初始化，appId替换成你的在Bugly平台申请的appId
        Beta.autoInit = true;
        Beta.autoCheckUpgrade = true;
        Beta.initDelay = 3 * 1000;
        Beta.largeIconId = R.mipmap.ic_launcher;
        Beta.smallIconId = R.mipmap.ic_launcher;
        Beta.defaultBannerId = R.mipmap.ic_launcher;
        Beta.storageDir = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS);
        Beta.showInterruptedStrategy = true;
        Beta.canShowUpgradeActs.add(MainActivity.class);
        Beta.autoDownloadOnWifi = true;
        Bugly.init(this, APP_ID, false);
    }

    // Initialize the image loader stratetry
    public static void initImageLoader(Context context) {
        ImageLoaderConfiguration.Builder config = new ImageLoaderConfiguration.Builder(context);
        config.threadPriority(Thread.NORM_PRIORITY);
        // do not cache multiple images
        config.denyCacheImageMultipleSizesInMemory();
        config.memoryCacheSize((int) Runtime.getRuntime().maxMemory() / 4);
        // the files name generator which are cached
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
//            File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
//            config.diskCache(new UnlimitedDiskCache(new File(sdCardDir.getAbsolutePath() + "/XPTteacher")));
        }

        config.diskCache(new UnlimitedDiskCache(new File(XPTApplication.getInstance().getCachePath())));

        config.diskCacheFileNameGenerator(new Md5FileNameGenerator());
        // the disk cache size : here is 100M
        config.diskCacheSize(100 * 1024 * 1024); // 100 MiB
        config.tasksProcessingOrder(QueueProcessingType.LIFO);
        //修改连接超时时间5秒，下载超时时间5秒
        config.imageDownloader(new BaseImageDownloader(mInstance, 5 * 1000, 5 * 1000));
        //		config.writeDebugLogs(); // Remove for release app
        // Initialize ImageLoader with configuration.
        ImageLoader.getInstance().init(config.build());
    }

    public String getCachePath() {
        File cacheDir = null;
        if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {
            cacheDir = getExternalCacheDir();
        } else {
            cacheDir = getCacheDir();
        }
        File sdCardDir = Environment.getExternalStorageDirectory();//获取SDCard目录
//            config.diskCache(new UnlimitedDiskCache(new File(sdCardDir.getAbsolutePath() + "/XPTSchool")));
        if (cacheDir == null) {
            cacheDir = new File(sdCardDir.getAbsolutePath() + "/XPTSchool");
            Log.i(TAG, "cacheDir is null " + cacheDir.getAbsolutePath());
        }

        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    public UserType getCurrent_user_type() {
        if (!isLoggedIn()) {
            return null;
        }
        String current_user_type = SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_TYPE, "").toString();
        return UserType.getUserTypeByStr(current_user_type);
    }

    public String getCurrentUserId() {
        String userId = "";
        UserType type = getCurrent_user_type();
        if (UserType.PARENT.equals(type)) {
            userId = GreenDaoHelper.getInstance().getCurrentParent().getU_id();
        } else if (UserType.TEACHER.equals(type)) {
            userId = GreenDaoHelper.getInstance().getCurrentTeacher().getU_id();
        } else {
            userId = SharedPreferencesUtil.getData(XPTApplication.getInstance(), SharedPreferencesUtil.KEY_UID, "").toString();
        }
        return userId;
    }

    public String getCurrentUserName() {
        String userName = "";
        UserType type = getCurrent_user_type();
        if (UserType.PARENT.equals(type)) {
            userName = GreenDaoHelper.getInstance().getCurrentParent().getParent_name();
        } else if (UserType.TEACHER.equals(type)) {
            userName = GreenDaoHelper.getInstance().getCurrentTeacher().getName();
        } else {
            userName = SharedPreferencesUtil.getData(XPTApplication.getInstance(), SharedPreferencesUtil.KEY_USER_NAME, "").toString();
        }
        return userName;
    }

    public String getCurrentRefId() {
        String refId = "10847";
        UserType type = getCurrent_user_type();
        if (UserType.PARENT.equals(type)) {
            refId = GreenDaoHelper.getInstance().getCurrentParent().getRef_id();
        } else if (UserType.TEACHER.equals(type)) {
            refId = GreenDaoHelper.getInstance().getCurrentTeacher().getRef_id();
        } else {
            refId = SharedPreferencesUtil.getData(XPTApplication.getInstance(), SharedPreferencesUtil.KEY_REF_ID, "").toString();
        }
        return refId;
    }

    public void setCurrent_user_type(String current_user_type) {
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_USER_TYPE, current_user_type);
    }

    public boolean hasWatch() {
        Boolean hasWatch = false;
        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        for (int i = 0; i < students.size(); i++) {
            BeanStudent student = students.get(i);
            if (student.getDevice_type().equals("2")) {
                hasWatch = true;
                break;
            }
        }
        return hasWatch;
    }

    public String getCurrentWatchIMEI() {
        currentWatchIMEI = "";
        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        for (int i = 0; i < students.size(); i++) {
            BeanStudent student = students.get(i);
            if (student.getDevice_type().equals("2")) {
                currentWatchIMEI = student.getImei_id();
                break;
            }
        }
        return currentWatchIMEI;
    }

    public BeanStudent getCurrentWatchStu() {
        currentWatchStu = GreenDaoHelper.getInstance().getStudentByIMEI(getCurrentWatchIMEI());
        return currentWatchStu;
    }

    public boolean isLoggedIn() {
        String userName = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_USER_NAME, "");
        String password = (String) SharedPreferencesUtil.getData(this, SharedPreferencesUtil.KEY_PWD, "");
        if (userName.isEmpty() || password.isEmpty()) {
            return false;
        } else {
            return true;
        }
    }

    /**
     * @return
     * @Description： 获取当前屏幕的宽度
     */
    public int getWindowWidth() {
        return display.getWidth();
    }

    /**
     * @return
     * @Description： 获取当前屏幕的高度
     */
    public int getWindowHeight() {
        return display.getHeight();
    }

    public int getQuarterWidth() {
        return display.getWidth() / 4;
    }

}
