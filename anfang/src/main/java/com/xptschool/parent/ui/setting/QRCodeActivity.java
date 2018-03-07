package com.xptschool.parent.ui.setting;

import android.Manifest;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.UMShareAPI;
import com.umeng.socialize.UMShareListener;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMWeb;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.QRCodeUtil;

import java.util.HashMap;

import butterknife.BindView;

/**
 * Created by dexing on 2017-11-15 0015.
 */

public class QRCodeActivity extends BaseActivity {

    @BindView(R.id.QRImg)
    ImageView QRImg;
    String shareURL = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_qrcode);
        setTitle(R.string.mine_qr_code);

//        Bitmap bitmap = QRCodeUtil.createQRCodeBitmap("http://school.xinpingtai.com/register?user_id=100", 100,
//                BitmapFactory.decodeResource(getResources(), R.drawable.logo_shuhai), 0.2f);

        try {
            final String text = XPTApplication.getInstance().getCurrentUserId();
            final byte[] textByte = text.getBytes("UTF-8");
            //编码
            final String encodedText = Base64.encodeToString(textByte, Base64.DEFAULT);

            shareURL = "http://school.xinpingtai.com/index.php/Wap/Register/index?ref=" + encodedText;
            Bitmap bitmap = QRCodeUtil.createCode(shareURL, BitmapFactory.decodeResource(getResources(), R.drawable.logo_shuhai));

            if (bitmap != null) {
                QRImg.setImageBitmap(bitmap);
            }
        } catch (Exception ex) {

        }

        setTxtRight("分享");
        setTextRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                showShare();
            }
        });
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

    private void showShare() {
        UMWeb web = new UMWeb(shareURL);
        web.setTitle("数海信息信平台客户端");//标题
//        web.setThumb(thumb);  //缩略图
        web.setDescription("诚邀您下载数海信平台客户端进行体验");//描述

        new ShareAction(QRCodeActivity.this)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)//传入平台
                .withMedia(web)//分享内容
                .setCallback(new UMShareListener() {

                    @Override
                    public void onStart(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onResult(SHARE_MEDIA share_media) {

                    }

                    @Override
                    public void onError(SHARE_MEDIA share_media, Throwable throwable) {

                    }

                    @Override
                    public void onCancel(SHARE_MEDIA share_media) {

                    }
                })//回调监听器
                .open();
    }
}
