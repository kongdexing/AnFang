package com.xptschool.parent.ui.mine.role;

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

import com.android.widget.view.CircularImageView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.uitl.TFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.ui.fence.FenceDrawActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.util.TakePhotoUtil;
import com.xptschool.parent.view.AlbumSourceView;
import com.xptschool.parent.view.CustomEditDialog;
import com.xptschool.parent.view.CustomSexDialog;

import java.io.File;

import butterknife.BindView;

/**
 * Created by shuhaixinxi on 2018/3/23.
 */

public class BaseUserView extends LinearLayout {

    public String TAG = "";
    public Context mContext;
    private PopupWindow picPopup;

    @BindView(R.id.imgHead)
    CircularImageView imgHead;

    public BaseUserView(Context context) {
        this(context, null);
    }

    public BaseUserView(Context context, @Nullable AttributeSet attrs) {
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
                    takePhoto.onPickMultipleWithCrop(1, TakePhotoUtil.getCropOptions());
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

    //更改用户性别
    public void changeSex(String currentSex, String userId) {
        CustomSexDialog dialog = new CustomSexDialog(mContext);
        dialog.setSexVal(currentSex);
        dialog.setAlertDialogClickListener(new CustomSexDialog.DialogClickListener() {
            @Override
            public void onPositiveClick(String value) {
                Toast.makeText(mContext, value, Toast.LENGTH_SHORT).show();
            }
        });
    }

    public void setHeadImage(String result, String userId) {
        Log.i(TAG, "setHeadImage: " + result + "  userId:" + userId);
        ImageLoader.getInstance().displayImage(result,
                new ImageViewAware(imgHead), CommonUtil.getDefaultImageLoaderOption());
    }

}
