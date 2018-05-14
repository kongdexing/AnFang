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
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.CircularImageView;
import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.model.TResult;
import com.jph.takephoto.uitl.TFileUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.ui.album.TakePhotoActivity;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.util.TakePhotoUtil;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.util.WatchUtil;
import com.xptschool.parent.view.AlbumSourceView;

import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

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
    @BindView(R.id.txtRelation)
    TextView txtRelation;

    private BeanStudent currentStudent;
    private String compressPath = null;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stu_watch_edit);
        setTitle(R.string.title_device_edit);

        try {
            Bundle bundle = getIntent().getExtras();
            if (bundle != null) {
                currentStudent = (BeanStudent) bundle.getSerializable("student");
            }
        } catch (Exception ex) {
            Log.i(TAG, "onCreate: bundle get data error :" + ex.getMessage());
        }

        setBtnRight("保存");
        setBtnRightClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                updateStuInfo();
            }
        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (currentStudent != null) {
            initView();
        } else {
            ToastUtils.showToast(this, "学生资料为空");
            finish();
        }
    }

    private void initView() {
        ImageLoader.getInstance().displayImage(currentStudent.getPhoto(),
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
        setTxtRelationVal();
    }

    private void setTxtRelationVal() {
        txtRelation.setText("我是" + (("1".equals(currentStudent.getSex()) ? "他的" : "她的") +
                WatchUtil.getRelationByKey(currentStudent.getRelation())));
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
                Intent intent = new Intent(this, RelationActivity.class);
                intent.putExtra(ExtraKey.RELATION, currentStudent.getRelation());
                startActivityForResult(intent, 1);
                break;
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (requestCode == 1 && resultCode == 1) {
            String relation = data.getStringExtra(ExtraKey.RELATION);
            currentStudent.setRelation(relation);
            setTxtRelationVal();
        }
        super.onActivityResult(requestCode, resultCode, data);
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
        compressPath = result.getImage().getCompressPath();
        ImageLoader.getInstance().displayImage("file://" + compressPath,
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
                        //拍照后保存图片的绝对路径
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

    /**
     * 修改学生信息
     */
    private void updateStuInfo() {

        currentStudent.setStu_name(edtNickName.getText().toString().trim());
        currentStudent.setCard_phone(edtPhone.getText().toString().trim());

        MyVolleyHttpParamsEntity entity = new MyVolleyHttpParamsEntity()
                .addParam("user_id", XPTApplication.getInstance().getCurrentUserId())
                .addParam("stu_id", currentStudent.getStu_id())
                .addParam("relation", currentStudent.getRelation())
                .addParam("stu_name", currentStudent.getStu_name())
                .addParam("sex", currentStudent.getSex())
                .addParam("stu_phone", currentStudent.getCard_phone());

        if (compressPath != null) {
            List<String> imgs = new ArrayList<>();
            imgs.add(compressPath);
            VolleyHttpService.getInstance().uploadFiles(HttpAction.UPDATE_STU_INFO, entity, imgs, volleyRequestListener);
        } else {
            VolleyHttpService.getInstance().sendPostRequest(HttpAction.UPDATE_STU_INFO, entity, volleyRequestListener);
        }
    }

    MyVolleyRequestListener volleyRequestListener = new MyVolleyRequestListener() {
        @Override
        public void onStart() {
            super.onStart();
            showProgress("正在保存信息...");
        }

        @Override
        public void onResponse(VolleyHttpResult volleyHttpResult) {
            super.onResponse(volleyHttpResult);
            hideProgress();
            switch (volleyHttpResult.getStatus()) {
                case HttpAction.SUCCESS:
                    try {
                        JSONObject jsonObject = new JSONObject(volleyHttpResult.getData().toString());
                        String photo = jsonObject.getString("photo");
                        currentStudent.setPhoto(photo);
                        GreenDaoHelper.getInstance().updateStudent(currentStudent);
                        ToastUtils.showToast(StuWatchEditActivity.this, "修改成功");
                        finish();
                    } catch (Exception ex) {
                        ToastUtils.showToast(StuWatchEditActivity.this, "修改失败");
                    }
                    break;
                default:
                    ToastUtils.showToast(StuWatchEditActivity.this, "修改失败");
                    break;
            }
        }

        @Override
        public void onErrorResponse(VolleyError volleyError) {
            super.onErrorResponse(volleyError);
            hideProgress();
        }
    };

}
