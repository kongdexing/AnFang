package com.shuhai.anfang.view.autoviewpager;

import android.content.Context;
import android.os.Build;
import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

public class GalleryTransformer implements ViewPager.PageTransformer {

    private Context mContext;

    public GalleryTransformer(Context context){
        this.mContext = context;
    }

    @Override
    public void transformPage(View view, float position) {
//        float scale = 0.5f;
//        float scaleValue = 1 - Math.abs(position) * scale;
//        view.setScaleX(scaleValue);
//        view.setScaleY(scaleValue);
//        view.setAlpha(scaleValue*1.9f);
//        view.setPivotX(view.getWidth() * (1 - position - (position > 0 ? 1 : -1) * 0.75f) * scale);
//        view.setPivotY(view.getHeight()*0.5f);
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            view.setElevation(position > -0.5 && position < 0.5 ? 1 : 0);
//        }else {
//            ViewCompat.setElevation(view,position > -0.5 && position < 0.5 ? 1 : 0);
//        }

        float scale = 0.1f;
        float scaleValue = 1 - Math.abs(position) * scale;
        view.setScaleX(scaleValue);
        view.setScaleY(scaleValue);
        view.setAlpha(scaleValue*1.9f);
        //view.setPivotX(view.getWidth() * (1 - position - (position > 0 ? 1 : -1) * -0.75f) * scale);
        view.setPivotY(view.getHeight()*0.5f);
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            view.setElevation(position > -0.5 && position < 0.5 ? 1 : 0);
        }else {
            ViewCompat.setElevation(view,position > -0.5 && position < 0.5 ? 1 : 0);
        }

    }

//    @Override
//    public void transformPage(View view, float position) {
//
//        float scale = 0.5f;
//        float scaleValue = 1 - Math.abs(position) * scale;
//        view.setScaleX(scaleValue);//设置view水平方向缩放比例
//        view.setScaleY(scaleValue);//设置view垂直方向缩放比例
//        view.setAlpha(scaleValue * 1.9f);
//
//        // 获取SharedPreferences对象
//        SharedPreferences sp = mContext.getSharedPreferences("MYSP", mContext.MODE_PRIVATE);
//        // 获取Editor对象
//        SharedPreferences.Editor mEditor = sp.edit();
//        float SP_PivotY = sp.getFloat("SP_PivotY",0);
//        Log.d("MG", "transformPage: 数据库中SP_PivotY ====" + SP_PivotY);
//
//        if (SP_PivotY == 0.0){
//            float PivotY = view.getPivotY();
//            Log.d("MG", "transformPage: 页面计算PivotY ====" + PivotY);
//            mEditor.putFloat("SP_PivotY",PivotY);
//            mEditor.commit();
//            view.setPivotY(PivotY);//设置垂直方向偏转量
//        }else if (SP_PivotY != 0.0){
//            view.setPivotY(SP_PivotY);//设置垂直方向偏转量
//        }
//
//        /**
//         * 原始代码
//         */
//        float PivotX = view.getWidth() * (1 - position - (position > 0 ? 1 : -1) * 0.5f) * scale;
//        view.setPivotX(PivotX);//设置水平方向偏转量
//
//        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
//            view.setElevation(position > -0.5 && position < 0.5 ? 1 : 0);
//        }else {
//            ViewCompat.setElevation(view,position > -0.5 && position < 0.5 ? 1 : 0);
//        }
//    }
}