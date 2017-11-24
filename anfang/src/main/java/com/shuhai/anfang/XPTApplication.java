package com.shuhai.anfang;

import android.content.Context;
import android.os.Environment;
import android.support.multidex.MultiDex;
import android.util.Log;
import android.view.Display;
import android.view.WindowManager;

import com.android.volley.common.VolleyHttpService;
import com.android.widget.audiorecorder.AudioManager;
import com.baidu.mapapi.SDKInitializer;
import com.hyphenate.chat.EMOptions;
import com.liulishuo.filedownloader.FileDownloader;
import com.liulishuo.filedownloader.connection.FileDownloadUrlConnection;
import com.liulishuo.filedownloader.services.DownloadMgrInitialParams;
import com.nostra13.universalimageloader.cache.disc.impl.UnlimitedDiskCache;
import com.nostra13.universalimageloader.cache.disc.naming.Md5FileNameGenerator;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.nostra13.universalimageloader.core.assist.QueueProcessingType;
import com.nostra13.universalimageloader.core.download.BaseImageDownloader;
import com.shuhai.anfang.common.LocalImageHelper;
import com.shuhai.anfang.common.SharedPreferencesUtil;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.ease.EaseHelper;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.main.MainActivity;
import com.tencent.bugly.Bugly;
import com.tencent.bugly.beta.Beta;
import com.umeng.analytics.MobclickAgent;
import com.umeng.message.UmengNotificationClickHandler;
import com.umeng.message.entity.UMessage;
import com.xiaomi.ad.AdSdk;

import org.doubango.ngn.NgnApplication;

import java.io.File;
import java.net.Proxy;

public class XPTApplication extends NgnApplication {

    //小米推送
    public static final String APP_MIPUSH_ID = "2882303761517599079";
    public static final String APP_MIPUSH_KEY = "5961759967079";
    //魅族推送
    public static final String MZ_APP_ID = "111065";
    public static final String MZ_APP_KEY = "5e44c6c91687442091c945f29c888b18";
    //bugly
    public static final String APP_ID = "3e1429a7a5"; // TODO bugly上注册的appid
    private static XPTApplication mInstance;
    private String current_user_type = "";

    public static final String WXAPP_ID = "wx1af4f660ce9e6b37";
    private Display display;
    public static String TAG = XPTApplication.class.getSimpleName();

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
        LocalImageHelper.init(this);
        VolleyHttpService.init(this);

        if (display == null) {
            WindowManager windowManager = (WindowManager)
                    getSystemService(Context.WINDOW_SERVICE);
            display = windowManager.getDefaultDisplay();
        }

        AdSdk.initialize(this, "2882303761517634324");

        //baidumap
        SDKInitializer.initialize(this);
        GreenDaoHelper.getInstance().initGreenDao(this);

        MobclickAgent.setCheckDevice(true);
        //日志加密设置
        MobclickAgent.enableEncrypt(true);

//        mPushAgent.setNotificationPlaySound(MsgConstant.NOTIFICATION_PLAY_SDK_ENABLE);//声音
//        mPushAgent.setNotificationPlayLights(MsgConstant.NOTIFICATION_PLAY_SERVER);//呼吸灯
//        mPushAgent.setNotificationPlayVibrate(MsgConstant.NOTIFICATION_PLAY_SERVER);//振动

        //通知栏数量显示
//        mPushAgent.setDisplayNotificationNumber(0);
//        mPushAgent.setMessageHandler(new MyUmengMessageHandler());
//        mPushAgent.setDebugMode(false);
        /**
         * 自定义行为的回调处理
         * UmengNotificationClickHandler是在BroadcastReceiver中被调用，故
         * 如果需启动Activity，需添加Intent.FLAG_ACTIVITY_NEW_TASK
         * */
        UmengNotificationClickHandler notificationClickHandler = new UmengNotificationClickHandler() {
            @Override
            public void dealWithCustomAction(Context context, UMessage msg) {
                Log.i(TAG, "dealWithCustomAction: " + msg.text);
                //根据msg类型判断
            }
        };
        //使用自定义的NotificationHandler，来结合友盟统计处理消息通知
        //参考http://bbs.umeng.com/thread-11112-1-1.html
        //CustomNotificationHandler notificationClickHandler = new CustomNotificationHandler();
//        mPushAgent.setNotificationClickHandler(notificationClickHandler);

        AudioManager.getInstance(getCachePath());
        FileDownloader.init(getApplicationContext(), new DownloadMgrInitialParams.InitCustomMaker()
                .connectionCreator(new FileDownloadUrlConnection
                        .Creator(new FileDownloadUrlConnection.Configuration()
                        .connectTimeout(15_000) // set connection timeout.
                        .readTimeout(15_000) // set read timeout.
                        .proxy(Proxy.NO_PROXY) // set proxy
                )));

        EMOptions options = new EMOptions();
        // 默认添加好友时，是不需要验证的，改成需要验证
        options.setAcceptInvitationAlways(false);
        // 是否自动将消息附件上传到环信服务器，默认为True是使用环信服务器上传下载，如果设为 false，需要开发者自己处理附件消息的上传和下载
//        options.setAutoTransferMessageAttachments(true);
        // 是否自动下载附件类消息的缩略图等，默认为 true 这里和上边这个参数相关联
//        options.setAutoDownloadThumbnail(true);

        //init demo helper
        EaseHelper.getInstance().init(mInstance);
    }

    private void initBugly() {
        // 设置开发设备
        Bugly.setIsDevelopmentDevice(this, true);
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
        Beta.autoDownloadOnWifi = false;
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
//            config.diskCache(new UnlimitedDiskCache(new File(sdCardDir.getAbsolutePath() + "/XPTteacher")));
        if (cacheDir == null) {
            cacheDir = new File(sdCardDir.getAbsolutePath() + "/XPTteacher");
            Log.i(TAG, "cacheDir is null " + cacheDir.getAbsolutePath());
        }

        if (!cacheDir.exists())
            cacheDir.mkdirs();
        return cacheDir.getAbsolutePath();
    }

    public UserType getCurrent_user_type() {
        if (current_user_type.equals(UserType.PARENT.toString())) {
            return UserType.PARENT;
        } else if (current_user_type.equals(UserType.TEACHER.toString())) {
            return UserType.TEACHER;
        } else {
            return null;
        }
    }

    public void setCurrent_user_type(String current_user_type) {
        this.current_user_type = current_user_type;
        SharedPreferencesUtil.saveData(this, SharedPreferencesUtil.KEY_USER_TYPE, current_user_type);
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
