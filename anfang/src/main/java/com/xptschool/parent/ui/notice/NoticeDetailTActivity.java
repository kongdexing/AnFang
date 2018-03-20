package com.xptschool.parent.ui.notice;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.mygridview.MyGridView;
import com.android.widget.spinner.MaterialSpinner;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanNotice;
import com.xptschool.parent.common.ActivityResultCode;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanClass;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.main.BaseActivity;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;

import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class NoticeDetailTActivity extends BaseActivity {

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.spnClasses)
    MaterialSpinner spnClasses;

    @BindView(R.id.grdvAlbum)
    MyGridView grdvAlbum;

    @BindView(R.id.edtTitle)
    EditText edtTitle;

    @BindView(R.id.edtContent)
    EditText edtContent;

    @BindView(R.id.llSendType)
    LinearLayout llSendType;

    @BindView(R.id.llSendTime)
    LinearLayout llSendTime;

    @BindView(R.id.txtSendType)
    TextView txtSendType;

    @BindView(R.id.txtSendTime)
    TextView txtSendTime;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    private BeanNotice currentNotice;
    private PopupWindow datePopup;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_notice_detail_t);

        setTitle(R.string.home_notice);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentNotice = bundle.getParcelable(ExtraKey.NOTICE_DETAIL);
        }
        if (currentNotice != null) {
            //判断是否可删除
            btnDelete.setVisibility(View.VISIBLE);
            btnSubmit.setVisibility(View.GONE);
        } else {
            llSendType.setVisibility(View.GONE);
            llSendTime.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
        }
        initData();
    }

    public void initData() {
        if (currentNotice != null) {
            spnClasses.setItems(currentNotice.getClassInfo());

            if (currentNotice.getUser_id().equals(GreenDaoHelper.getInstance().getCurrentTeacher().getU_id())) {
                //自己发布的公告
                txtSendType.setText("发出");
                btnDelete.setVisibility(View.VISIBLE);
            } else {
                //接收到的公告
                txtSendType.setText("接收");
                btnDelete.setVisibility(View.GONE);
            }
            txtSendTime.setText(currentNotice.getCreate_time());
            edtTitle.setText(currentNotice.getTitle());
            edtTitle.setSelection(currentNotice.getTitle().length());
            edtContent.setText(currentNotice.getContent());
            if (currentNotice.getClassInfo().size() == 1) {
                //只有一个班级，不可选择
                spnClasses.setEnabled(false);
            }
        } else {
            List<BeanClass> classList = GreenDaoHelper.getInstance().getAllClass();
            if (classList.size() == 0) {
                spnClasses.setText("无执教班级");
            } else {
                spnClasses.setItems(classList);
            }
        }

        //绑定相册
//        myPicGridAdapter = new AlbumGridAdapter(this, new AlbumGridAdapter.MyGridViewClickListener() {
//            @Override
//            public void onGridViewItemClick(int position, String imgPath) {
//                if (position == 0) {
//                    if (LocalImageHelper.getInstance().getLocalCheckedImgs().size() >= LocalImageHelper.getInstance().getMaxChoiceSize()) {
//                        Toast.makeText(NoticeDetailActivity.this, getString(R.string.image_upline, LocalImageHelper.getInstance().getMaxChoiceSize()), Toast.LENGTH_SHORT).show();
//                        return;
//                    }
//                    showAlbumSource(albumviewpager);
//                } else {
//                    showViewPager(albumviewpager, position - 1);
//                }
//            }
//        });
//        grdvAlbum.setAdapter(myPicGridAdapter);
    }

    @OnClick({R.id.btnSubmit, R.id.btnDelete})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.btnSubmit:
                String title = edtTitle.getText().toString().trim();
                String content = edtContent.getText().toString().trim();
                if (title.isEmpty() || content.isEmpty()) {
                    Toast.makeText(this, R.string.toast_input_empty, Toast.LENGTH_SHORT).show();
                    return;
                }
                BeanClass currentClass = null;
                try {
                    currentClass = (BeanClass) spnClasses.getSelectedItem();
                } catch (Exception ex) {
                    currentClass = null;
                }
                if (currentClass == null) {
                    Toast.makeText(this, "无执教班级", Toast.LENGTH_SHORT).show();
                    return;
                }

                BeanNotice notice = new BeanNotice();
                notice.setTitle(title);
                notice.setContent(content);
                notice.setC_id(currentClass.getC_id());
                notice.setG_id(currentClass.getG_id());
                createNotice(notice);
                break;
            case R.id.btnDelete:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle(R.string.home_notice);
                dialog.setMessage(R.string.msg_delete_notice);
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        deleteNotice();
                    }
                });
                break;
        }
    }

    public void createNotice(BeanNotice notice) {
        BeanTeacher teacher = GreenDaoHelper.getInstance().getCurrentTeacher();
        if (teacher == null) {
            ToastUtils.showToast(this, R.string.msg_teacher_null);
            return;
        }

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.NOTICE_ADD, new MyVolleyHttpParamsEntity()
                        .addParam("a_id", teacher.getA_id())
                        .addParam("s_id", teacher.getS_id())
                        .addParam("g_id", notice.getG_id())
                        .addParam("c_id", notice.getC_id())
                        .addParam("title", notice.getTitle())
                        .addParam("content", notice.getContent())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.NOTICE_ADD))
                , new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        showProgress(R.string.progress_add_notice);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        hideProgress();
                        Toast.makeText(NoticeDetailTActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        if (httpResult.getStatus() == 1) {
                            finish();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        hideProgress();
                    }
                });
    }

    private void deleteNotice() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.NOTICE_DEL, new MyVolleyHttpParamsEntity()
                        .addParam("m_id", currentNotice.getM_id())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.NOTICE_DEL)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        showProgress(R.string.progress_del_notice);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        hideProgress();
                        Toast.makeText(NoticeDetailTActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        if (httpResult.getStatus() == HttpAction.SUCCESS) {
                            Intent intent = new Intent();
                            intent.putExtra(ExtraKey.NOTICE_DETAIL, currentNotice);
                            setResult(ActivityResultCode.Notice_delete, intent);
                            finish();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        hideProgress();
                    }
                });
    }

}
