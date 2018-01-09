package com.xptschool.parent.ui.homework;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.animation.AlphaAnimation;
import android.view.animation.AnimationSet;
import android.view.animation.ScaleAnimation;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.android.widget.mygridview.MyGridView;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanHomeWork;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.ui.album.AlbumGridParentAdapter;
import com.xptschool.parent.ui.album.AlbumParentViewPager;
import com.xptschool.parent.ui.album.LocalImagePHelper;
import com.xptschool.parent.util.ToastUtils;

import java.util.List;
import java.util.Set;

import butterknife.BindView;

public class HomeWorkDetailParentActivity extends VoicePlayActivity {

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.txtClassName)
    TextView txtClassName;

    @BindView(R.id.txtSubject)
    TextView txtSubject;

    @BindView(R.id.gridview)
    MyGridView gridView;

    @BindView(R.id.llTeacher)
    LinearLayout llTeacher;

    @BindView(R.id.txtTeacher)
    TextView txtTeacher;

    @BindView(R.id.edtName)
    TextView edtName;

    @BindView(R.id.edtContent)
    TextView edtContent;

    @BindView(R.id.txtPushTime)
    TextView txtPushTime;

    @BindView(R.id.llCreateTime)
    LinearLayout llCreateTime;

    @BindView(R.id.txtCompleteTime)
    TextView txtCompleteTime;

    @BindView(R.id.albumviewpager)
    AlbumParentViewPager albumviewpager;

    private BeanHomeWork currentHomeWork;
    public AlbumGridParentAdapter myPicGridAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home_work_detail);

        setTitle(R.string.homework_detail);
        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentHomeWork = bundle.getParcelable(ExtraKey.HOMEWORK_DETAIL);
        }
        initData();
    }

    @Override
    protected void onResume() {
        super.onResume();
        Bundle bun = getIntent().getExtras();
        if (bun != null) {
            try {
                Set<String> keySet = bun.keySet();
                for (String key : keySet) {
                    String value = bun.getString(key);
                    ToastUtils.showToast(this, "作业id为：" + value);
                }
            } catch (Exception ex) {
                ToastUtils.showToast(this, "");
            }
        } else {
            ToastUtils.showToast(this, "bundle is null：");
        }
    }

    @Override
    protected void onNewIntent(Intent intent) {
        super.onNewIntent(intent);
        setIntent(intent);
    }

    private void initData() {

        myPicGridAdapter = new AlbumGridParentAdapter(this, new AlbumGridParentAdapter.MyGridViewClickListener() {
            @Override
            public void onGridViewItemClick(int position, String imgPath) {
                if (currentHomeWork != null) {
                    showNetImgViewPager(albumviewpager, currentHomeWork.getFile_path(), position);
                } else {
                    if (position == 0) {
//                        if (LocalImagePHelper.getInstance().getCheckedItems().size() >= LocalImagePHelper.getInstance().getMaxChoiceSize()) {
//                            Toast.makeText(HomeWorkDetailParentActivity.this, getString(R.string.image_upline, LocalImagePHelper.getInstance().getMaxChoiceSize()), Toast.LENGTH_SHORT).show();
//                            return;
//                        }
//                        showAlbumSource(albumviewpager);
                    } else {
                        showViewPager(albumviewpager, position - 1);
                    }
                }
            }
        });
        gridView.setAdapter(myPicGridAdapter);

        if (currentHomeWork != null) {
            edtName.setText(currentHomeWork.getName());
            txtPushTime.setText(currentHomeWork.getCreate_time());
            txtCompleteTime.setText(currentHomeWork.getFinish_time());
            txtTeacher.setText(currentHomeWork.getUser_name());
            edtContent.setText(currentHomeWork.getWork_content());
            myPicGridAdapter.initDate(currentHomeWork.getFile_path(), false);

            txtPushTime.setClickable(false);
            txtCompleteTime.setClickable(false);
            txtClassName.setText(currentHomeWork.getG_name() + currentHomeWork.getC_name());
            txtSubject.setText(currentHomeWork.getCrs_name());
        }

        initVoice(currentHomeWork);
    }

    //显示大图pager
    public void showNetImgViewPager(AlbumParentViewPager albumviewpager, List<String> imgUris, int index) {
        if (albumviewpager == null)
            return;
        albumviewpager.setVisibility(View.VISIBLE);
        albumviewpager.setAdapter(albumviewpager.new NetViewPagerAdapter(imgUris));
        albumviewpager.setCurrentItem(index);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1, albumviewpager.getWidth() / 2, albumviewpager.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        albumviewpager.startAnimation(set);
    }

    //关闭大图显示
    public void hideViewPager(AlbumParentViewPager albumviewpager) {
        if (albumviewpager == null)
            return;
        albumviewpager.setVisibility(View.GONE);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation(1, (float) 0.9, 1, (float) 0.9, albumviewpager.getWidth() / 2, albumviewpager.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation(1, 0);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        albumviewpager.startAnimation(set);
    }

    //显示大图pager
    public void showViewPager(AlbumParentViewPager albumviewpager, int index) {
        if (albumviewpager == null)
            return;
        albumviewpager.setVisibility(View.VISIBLE);
        albumviewpager.setAdapter(albumviewpager.new LocalViewPagerAdapter(LocalImagePHelper.getInstance().getCheckedItems()));
        albumviewpager.setCurrentItem(index);
        AnimationSet set = new AnimationSet(true);
        ScaleAnimation scaleAnimation = new ScaleAnimation((float) 0.9, 1, (float) 0.9, 1, albumviewpager.getWidth() / 2, albumviewpager.getHeight() / 2);
        scaleAnimation.setDuration(200);
        set.addAnimation(scaleAnimation);
        AlphaAnimation alphaAnimation = new AlphaAnimation((float) 0.1, 1);
        alphaAnimation.setDuration(200);
        set.addAnimation(alphaAnimation);
        albumviewpager.startAnimation(set);
    }

    @Override
    public void onBackPressed() {
        if (albumviewpager.getVisibility() == View.VISIBLE) {
            hideViewPager(albumviewpager);
        } else {
            super.onBackPressed();
        }
    }

}
