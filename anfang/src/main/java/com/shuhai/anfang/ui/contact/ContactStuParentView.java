package com.shuhai.anfang.ui.contact;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.AttributeSet;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.hyphenate.easeui.EaseConstant;
import com.shuhai.anfang.R;
import com.shuhai.anfang.model.ContactParent;
import com.shuhai.anfang.ui.chat.ChatActivity;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by shuhaixinxi on 2017/12/18.
 */

public class ContactStuParentView extends ContactBaseView {

    @BindView(R.id.llParent)
    RelativeLayout llParent;
    @BindView(R.id.txtParentName)
    TextView txtParentName;
    @BindView(R.id.txtParentPhone)
    TextView txtParentPhone;

    public ContactStuParentView(Context context) {
        this(context, null);
    }

    public ContactStuParentView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        View view = LayoutInflater.from(context).inflate(R.layout.contact_parent, this, true);
        ButterKnife.bind(view);
    }

    public void setParentData(final ContactParent parentData) {
        if (parentData == null) {
            return;
        }
        txtParentName.setText(parentData.getName());
        txtParentPhone.setText(parentData.getPhone());
        llParent.setOnClickListener(new OnClickListener() {
            @Override
            public void onClick(View v) {
                showBottomView(v, parentData);
            }
        });
    }

    public void showBottomView(View view, final ContactParent parent) {

        BottomChatView bottomChatView = new BottomChatView(mContext);

        final PopupWindow picPopup = new PopupWindow(bottomChatView,
                LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.WRAP_CONTENT, true);

        bottomChatView.setOnBottomChatClickListener(new BottomChatView.OnBottomChatClickListener() {
            @Override
            public void onCallClick() {
                call(parent.getPhone());
                picPopup.dismiss();
            }

            @Override
            public void onChatClick() {
                Intent intent = new Intent(mContext, ChatActivity.class);
                intent.putExtra(EaseConstant.EXTRA_USER_ID, parent.getUser_id());
                intent.putExtra(EaseConstant.ExtRA_USER_NAME, parent.getName());
                mContext.startActivity(intent);
                ((Activity) mContext).finish();
            }

            @Override
            public void onBack() {
                picPopup.dismiss();
            }
        });

        picPopup.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
        picPopup.setTouchable(true);
        picPopup.setBackgroundDrawable(new ColorDrawable());
        picPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
            @Override
            public void onDismiss() {
                ((ContactsDetailActivity) mContext).backgroundAlpha(1.0f);
            }
        });
        ((ContactsDetailActivity) mContext).backgroundAlpha(0.5f);
        picPopup.showAtLocation(view, Gravity.BOTTOM, 0, 0);
    }

}
