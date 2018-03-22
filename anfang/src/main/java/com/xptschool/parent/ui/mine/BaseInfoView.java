package com.xptschool.parent.ui.mine;

import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.uitl.TFileUtils;
import com.xptschool.parent.R;
import com.xptschool.parent.ui.album.AlbumTeacherActivity;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.util.TakePhotoUtil;
import com.xptschool.parent.view.AlbumSourceView;

import java.io.File;

/**
 * Created by shuhaixinxi on 2017/12/20.
 */

public class BaseInfoView extends LinearLayout {

    public String TAG = "";
    public Context mContext;
    private PopupWindow picPopup;

    public BaseInfoView(Context context) {
        this(context, null);
    }

    public BaseInfoView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
        TAG = this.getClass().getSimpleName();
    }

    //弹起头像选择器
    public void choosePic(View view) {
        //选择相片来源
        if (picPopup == null) {
            TFileUtils.setCacheFile(null);
            AlbumSourceView albumSourceView = new AlbumSourceView(mContext);
            albumSourceView.setOnAlbumSourceClickListener(new AlbumSourceView.OnAlbumSourceClickListener() {
                @Override
                public void onAlbumClick() {
                    if (LocalImageTHelper.getInstance().getLocalCheckedImgs().size() >= LocalImageTHelper.getInstance().getMaxChoiceSize()) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.image_upline, LocalImageTHelper.getInstance().getMaxChoiceSize()), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    TakePhoto takePhoto = ((MyInfoActivity) mContext).getTakePhoto();
                    TakePhotoUtil.configCompress(takePhoto);
                    TakePhotoUtil.configTakePhotoOption(takePhoto);
                    int limit = LocalImageTHelper.getInstance().getCurrentEnableMaxChoiceSize();
                    takePhoto.onPickMultipleWithCrop(limit, TakePhotoUtil.getCropOptions());
                    picPopup.dismiss();
                }

                @Override
                public void onCameraClick() {
                    if (LocalImageTHelper.getInstance().getLocalCheckedImgs().size() >= LocalImageTHelper.getInstance().getMaxChoiceSize()) {
                        Toast.makeText(mContext, mContext.getResources().getString(R.string.image_upline, LocalImageTHelper.getInstance().getMaxChoiceSize()), Toast.LENGTH_SHORT).show();
                        return;
                    }
                    try {
                        //  拍照后保存图片的绝对路径
                        String cameraPath = LocalImageTHelper.getInstance().setCameraImgPath();
                        File file = new File(cameraPath);
                        if (!file.getParentFile().exists()) file.getParentFile().mkdirs();
                        Uri imageUri = Uri.fromFile(file);
                        Log.i(TAG, "onCameraClick: " + file.getAbsolutePath());
                        TakePhoto takePhoto = ((MyInfoActivity) mContext).getTakePhoto();
                        TakePhotoUtil.configCompress(takePhoto);
                        TakePhotoUtil.configTakePhotoOption(takePhoto);

                        takePhoto.onPickFromCaptureWithCrop(imageUri, TakePhotoUtil.getCropOptions());

                    } catch (Exception ex) {
                        Toast.makeText(mContext, ex.getMessage(), Toast.LENGTH_SHORT).show();
                        Log.i(TAG, "onCameraClick: " + ex.getMessage());
                    }
                    picPopup.dismiss();
                }

                @Override
                public void onBack() {
                    picPopup.dismiss();
                }
            });
            picPopup = new PopupWindow(albumSourceView,
                    LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);
            picPopup.setTouchable(true);
            picPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
            picPopup.setBackgroundDrawable(new ColorDrawable());
            picPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    ((MyInfoActivity) mContext).backgroundAlpha(1.0f);
                }
            });
        }
        ((MyInfoActivity) mContext).backgroundAlpha(0.5f);
        picPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

}
