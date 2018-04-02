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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.CircularImageView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.uitl.TFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.BeanUser;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.ui.fence.FenceDrawActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.util.TakePhotoUtil;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.AlbumSourceView;
import com.xptschool.parent.view.CustomEditDialog;
import com.xptschool.parent.view.CustomSexDialog;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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

    protected void initData() {

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
    public void changeSex(String currentSex) {
        CustomSexDialog dialog = new CustomSexDialog(mContext);
        dialog.setSexVal(currentSex);
        dialog.setAlertDialogClickListener(new CustomSexDialog.DialogClickListener() {
            @Override
            public void onPositiveClick(String value) {
                postUserInfo(new MyVolleyHttpParamsEntity()
                        .addParam("sex", value)
                        .addParam("user_id", XPTApplication.getInstance().getCurrentUserId()));
            }
        });
    }

    public void setHeadImage(String result) {
        Log.i(TAG, "setHeadImage: " + result);
        uploadHeadImg(result);
    }

    //更改用户名称
    public void changeName(String name) {
        CustomEditDialog dialog = new CustomEditDialog(mContext);
        dialog.setTitle("姓名");
        dialog.setEdtMessage(name);
        dialog.setAlertDialogClickListener(new CustomEditDialog.DialogClickListener() {
            @Override
            public void onPositiveClick(String value) {
                if (value.isEmpty()) {
                    Toast.makeText(mContext, R.string.input_user_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                postUserInfo(new MyVolleyHttpParamsEntity()
                        .addParam("name", value)
                        .addParam("user_id", XPTApplication.getInstance().getCurrentUserId()));
            }
        });
    }

    public void changeEmail(String email) {
        CustomEditDialog dialog = new CustomEditDialog(mContext);
        dialog.setTitle("邮箱");
        dialog.setEdtMessage(email);
        dialog.setAlertDialogClickListener(new CustomEditDialog.DialogClickListener() {
            @Override
            public void onPositiveClick(String value) {
                if (value.isEmpty()) {
                    Toast.makeText(mContext, R.string.input_user_name, Toast.LENGTH_SHORT).show();
                    return;
                }
                postUserInfo(new MyVolleyHttpParamsEntity()
                        .addParam("email", value)
                        .addParam("user_id", XPTApplication.getInstance().getCurrentUserId()));
            }
        });
    }

    private void uploadHeadImg(String imgFile) {
        List<String> imgs = new ArrayList<>();
        imgs.add(imgFile);

        VolleyHttpService.getInstance().uploadFiles(HttpAction.UserInfo, new MyVolleyHttpParamsEntity()
                .addParam("user_id", XPTApplication.getInstance().getCurrentUserId()), imgs, new MyVolleyRequestListener() {
            @Override
            public void onStart() {
                super.onStart();
                ((MyInfoActivity) mContext).showProgress("正在上传头像");
            }

            @Override
            public void onResponse(VolleyHttpResult volleyHttpResult) {
                super.onResponse(volleyHttpResult);
                ((MyInfoActivity) mContext).hideProgress();

                switch (volleyHttpResult.getStatus()) {
                    case HttpAction.SUCCESS:
                        try {
                            JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                            String head_img = object.getString("head_portrait");
                            if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                                BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
                                parent.setHead_portrait(head_img);
                                GreenDaoHelper.getInstance().insertParent(parent);

                            } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                                BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
                                teacher.setHead_portrait(head_img);
                                GreenDaoHelper.getInstance().insertTeacher(teacher);
                            } else {
                                BeanUser currentUser = GreenDaoHelper.getInstance().getCurrentUser();
                                if (currentUser != null) {
                                    currentUser.setHead_portrait(head_img);
                                    GreenDaoHelper.getInstance().insertUser(currentUser);
                                    ImageLoader.getInstance().displayImage(currentUser.getHead_portrait(),
                                            new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());
                                }
                            }
                            initData();
                            ToastUtils.showToast(mContext, R.string.toast_modify_success);
                        } catch (Exception ex) {
                            ToastUtils.showToast(mContext, R.string.toast_modify_failed);
                        }
                        break;
                    default:
                        ToastUtils.showToast(mContext, R.string.toast_modify_failed);
                        break;
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                ((MyInfoActivity) mContext).hideProgress();
                ToastUtils.showToast(mContext, R.string.toast_modify_failed);
            }
        });

    }

    private void postUserInfo(VolleyHttpParamsEntity entity) {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.UserInfo, entity, new MyVolleyRequestListener() {

            @Override
            public void onStart() {
                super.onStart();
                ((MyInfoActivity) mContext).showProgress("正在保存用户信息");
            }

            @Override
            public void onResponse(VolleyHttpResult volleyHttpResult) {
                super.onResponse(volleyHttpResult);
                ((MyInfoActivity) mContext).hideProgress();

                switch (volleyHttpResult.getStatus()) {
                    case HttpAction.SUCCESS:
                        try {
                            JSONObject object = new JSONObject(volleyHttpResult.getData().toString());
                            String sex = object.getString("sex");
                            String email = object.getString("email");
                            String name = object.getString("name");

                            if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
                                BeanParent parent = GreenDaoHelper.getInstance().getCurrentParent();
                                parent.setSex(sex);
                                parent.setEmail(email);
                                parent.setParent_name(name);
                                GreenDaoHelper.getInstance().insertParent(parent);
                            } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {

                            } else {
                                BeanUser currentUser = GreenDaoHelper.getInstance().getCurrentUser();
                                if (currentUser != null) {
                                    currentUser.setName(name);
                                    currentUser.setSex(sex);
                                    currentUser.setEmail(email);

                                    GreenDaoHelper.getInstance().insertUser(currentUser);
                                }
                            }
                            initData();
                            ToastUtils.showToast(mContext, R.string.toast_modify_success);
                        } catch (Exception ex) {
                            ToastUtils.showToast(mContext, R.string.toast_modify_failed);
                        }
                        break;
                    default:
                        ToastUtils.showToast(mContext, R.string.toast_modify_failed);
                        break;
                }
            }

            @Override
            public void onErrorResponse(VolleyError volleyError) {
                super.onErrorResponse(volleyError);
                ((MyInfoActivity) mContext).hideProgress();
                ToastUtils.showToast(mContext, R.string.toast_modify_failed);
            }
        });

    }

}
