package com.xptschool.parent.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.util.AttributeSet;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.view.CircularImageView;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.imageaware.ImageViewAware;
import com.xptschool.parent.R;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.ui.mine.BaseInfoView;
import com.xptschool.parent.ui.watch.MoniterActivity;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MoniterView extends BaseInfoView {

    private String TAG = MoniterView.class.getSimpleName();

    @BindView(R.id.imgHead)
    CircularImageView imgHead;
    @BindView(R.id.edtPhone)
    EditText edtPhone;

    public MoniterView(Context context) {
        this(context, null);
    }

    public MoniterView(final Context context, AttributeSet attrs) {
        super(context, attrs);
        Log.i(TAG, "HomeItemView: attrs");
        View view = LayoutInflater.from(context).inflate(R.layout.watch_pop_moniter, this, true);
        ButterKnife.bind(view);
    }

    public void setStudentData(BeanStudent student) {
        if (student != null) {
            edtPhone.setText("");
            ImageLoader.getInstance().displayImage(student.getPhoto(),
                    new ImageViewAware(imgHead), CommonUtil.getDefaultUserImageLoaderOption());
        }
    }

    @OnClick({R.id.ok})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.ok:
                ((MoniterActivity) mContext).setMonitorPhone(edtPhone.getText().toString().trim());
                break;
        }
    }


}
