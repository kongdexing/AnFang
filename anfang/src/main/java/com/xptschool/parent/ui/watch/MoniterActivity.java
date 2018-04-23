package com.xptschool.parent.ui.watch;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.Toast;

import com.jph.takephoto.app.TakePhoto;
import com.jph.takephoto.uitl.TFileUtils;
import com.xptschool.parent.R;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.ui.mine.MyInfoActivity;
import com.xptschool.parent.util.TakePhotoUtil;
import com.xptschool.parent.view.AlbumSourceView;
import com.xptschool.parent.view.MoniterView;

import java.io.File;

import butterknife.OnClick;

public class MoniterActivity extends BaseActivity {

    private PopupWindow picPopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_moniter);
        setTitle(R.string.home_moniter);

    }

    @OnClick({R.id.rlItem1})
    void onClick(View view) {
        switch (view.getId()) {
            case R.id.rlItem1:
                choosePic(findViewById(R.id.rlItem1));
                break;
        }
    }

    //弹起头像选择器
    public void choosePic(View view) {
        //选择相片来源
        if (picPopup == null) {
            TFileUtils.setCacheFile(null);

            MoniterView view1 = new MoniterView(this);

//            ImageView imageView = new ImageView(this);
//            LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(LinearLayout.LayoutParams.WRAP_CONTENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT);
//            imageView.setLayoutParams(layoutParams);
//            imageView.setScaleType(ImageView.ScaleType.FIT_XY);
//            imageView.setBackgroundResource(R.drawable.icon_moniter_bg);
//
//            imageView.setOnClickListener(new View.OnClickListener() {
//                @Override
//                public void onClick(View view) {
//                    picPopup.dismiss();
//                }
//            });

            picPopup = new PopupWindow(view1,
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
