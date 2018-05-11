package com.xptschool.parent.ui.watch;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.android.widget.view.CircularImageView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.uitl.TFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.ui.album.TakePhotoActivity;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.util.TakePhotoUtil;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.AlbumSourceView;

import java.io.File;

import butterknife.BindView;
import butterknife.OnClick;

/**
 * 孩子手表编辑页面
 */
public class StuWatchEditActivity extends TakePhotoActivity {

    @BindView(R.id.imgHead)
    CircularImageView imgHead;
    private PopupWindow picPopup;
    @BindView(R.id.imgMale)
    ImageView imgMale;
    @BindView(R.id.imgFemale)
    ImageView imgFemale;
    @BindView(R.id.edtNickName)
    EditText edtNickName;
    @BindView(R.id.edtPhone)
    EditText edtPhone;

    private BeanStudent currentStudent;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_watch_edit);
        setTitle(R.string.title_device_edit);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentStudent = (BeanStudent) bundle.getSerializable("student");
        }

        if (currentStudent != null) {
            initView();
        } else {
            ToastUtils.showToast(this, "学生资料为空");
            finish();
        }

        setBtnRight("保存");
        setBtnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

            }
        });
    }

    private void initView() {
        ImageLoader.getInstance().displayImage(currentStudent.getA_id(),
                new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());

        if ("1".equals(currentStudent.getSex())) {
            imgMale.setBackgroundResource(R.drawable.bg_male_normal);
            imgFemale.setBackgroundResource(R.drawable.bg_female_selected);
        } else {
            imgMale.setBackgroundResource(R.drawable.bg_male_selected);
            imgFemale.setBackgroundResource(R.drawable.bg_female_normal);
        }

        edtNickName.setText(currentStudent.getStu_name());
        edtNickName.setSelection(currentStudent.getStu_name().length());
        edtPhone.setText(currentStudent.getCard_phone());

    }

    @OnClick({R.id.imgHead, R.id.imgMale, R.id.imgFemale, R.id.rlRelation})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.imgHead:
                choosePic(imgHead);
                break;
            case R.id.imgMale:
                imgMale.setBackgroundResource(R.drawable.bg_male_normal);
                imgFemale.setBackgroundResource(R.drawable.bg_female_selected);
                currentStudent.setSex("1");
                break;
            case R.id.imgFemale:
                imgMale.setBackgroundResource(R.drawable.bg_male_selected);
                imgFemale.setBackgroundResource(R.drawable.bg_female_normal);
                currentStudent.setSex("0");
                break;
            case R.id.rlRelation:
                Intent intent = new Intent(this,RelationActivity.class);
                startActivity(intent);
                break;
        }
    }

    @Override
    public void takeCancel() {
        super.takeCancel();
    }

    @Override
    public void takeFail(TResult result, String msg) {
        super.takeFail(result, msg);
        Log.i(TAG, "takeFail: " + msg);
    }

    @Override
    public void takeSuccess(TResult result) {
        super.takeSuccess(result);
//        showImg(result.getImages());
        ImageLoader.getInstance().displayImage(result.getImage().getCompressPath(),
                new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());

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
                    TakePhoto takePhoto = getTakePhoto();
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
                        TakePhoto takePhoto = getTakePhoto();
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
                    backgroundAlpha(1.0f);
                }
            });
        }
        backgroundAlpha(0.5f);
        picPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

}
