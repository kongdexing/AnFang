package com.xptschool.parent.ui.homework;

import android.content.Intent;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.mygridview.MyGridView;
import com.android.widget.spinner.MaterialSpinner;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.xptschool.parent.R;
import com.xptschool.parent.bean.BeanHomeWork;
import com.xptschool.parent.common.ActivityResultCode;
import com.xptschool.parent.common.BroadcastAction;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.ExtraKey;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanClass;
import com.xptschool.parent.model.BeanCourse;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.album.AlbumGridTeacherAdapter;
import com.xptschool.parent.ui.album.AlbumTeacherViewPager;
import com.xptschool.parent.ui.album.LocalImagePHelper;
import com.xptschool.parent.ui.album.LocalImageTHelper;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomDialog;
import com.xptschool.parent.view.TimePickerPopupWindow;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;

public class HomeWorkDetailTeacherActivity extends VoiceRecordActivity {

    @BindView(R.id.scrollView)
    ScrollView scrollView;

    @BindView(R.id.spnSubject)
    MaterialSpinner spnCourse;

    @BindView(R.id.spnClasses)
    MaterialSpinner spnClasses;

    @BindView(R.id.gridview)
    MyGridView gridView;

    @BindView(R.id.btnSubmit)
    Button btnSubmit;

    @BindView(R.id.llTeacher)
    LinearLayout llTeacher;

    @BindView(R.id.txtTeacher)
    TextView txtTeacher;

    @BindView(R.id.btnDelete)
    Button btnDelete;

    @BindView(R.id.edtName)
    EditText edtName;

    @BindView(R.id.edtContent)
    EditText edtContent;

    @BindView(R.id.txtPushTime)
    TextView txtPushTime;

    @BindView(R.id.llCreateTime)
    LinearLayout llCreateTime;

    @BindView(R.id.txtCompleteTime)
    TextView txtCompleteTime;

    @BindView(R.id.albumviewpager)
    AlbumTeacherViewPager albumviewpager;

    private TimePickerPopupWindow pushDate, completeDate;
    private BeanHomeWork currentHomeWork;
    private boolean canModify = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_publish_home_work);

        Bundle bundle = getIntent().getExtras();
        if (bundle != null) {
            currentHomeWork = bundle.getParcelable(ExtraKey.HOMEWORK_DETAIL);
        }

        if (currentHomeWork == null) {
            setTitle(R.string.homework_push);
        } else {
            setTitle(R.string.homework_detail);
        }

        mScrollView = scrollView;
        initData();
    }

    private void initData() {
        List<BeanClass> allClass = GreenDaoHelper.getInstance().getAllClass();
        if (allClass.size() > 0) {
            spnClasses.setItems(allClass);
            loadCourseByClass(allClass.get(0));
        } else {
            spnClasses.setItems("无执教班级");
        }

        spnClasses.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanClass>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, BeanClass item) {
                loadCourseByClass(item);
            }
        });

        final List<BeanCourse> allCourse = GreenDaoHelper.getInstance().getAllCourse();
//        if (allCourse.size() > 0) {
//            spnCourse.setItems(allCourse);
//        } else {
//            spnCourse.setItems("无执教课程");
//        }
        spnCourse.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanCourse>() {

            @Override
            public void onItemSelected(MaterialSpinner view, int position, long id, BeanCourse item) {
                edtName.setText(item.getName() + "作业");
                edtName.setSelection(edtName.getText().length());
            }
        });

        myPicGridAdapter = new AlbumGridTeacherAdapter(this, new AlbumGridTeacherAdapter.MyGridViewClickListener() {
            @Override
            public void onGridViewItemClick(int position, String imgPath) {
                Log.i(TAG, "onGridViewItemClick: " + imgPath);
                if (currentHomeWork != null && !canModify) {
                    Log.i(TAG, "onGridViewItemClick: " + canModify);
                    showNetImgViewPager(albumviewpager, currentHomeWork.getFile_path(), position);
                } else {
                    if (position == 0) {
                        if (myPicGridAdapter.getImgPaths().size() >= LocalImagePHelper.getInstance().getMaxChoiceSize()) {
                            Toast.makeText(HomeWorkDetailTeacherActivity.this, getString(R.string.image_upline, LocalImagePHelper.getInstance().getMaxChoiceSize()), Toast.LENGTH_SHORT).show();
                            return;
                        }
                        showAlbumSource(albumviewpager);
                    } else {
                        showViewPager(albumviewpager, position - 1);
                    }
                }
            }
        });
        gridView.setAdapter(myPicGridAdapter);

        if (currentHomeWork != null) {
            setViewEnable(false);
            if (currentHomeWork.getUser_id().equals(GreenDaoHelper.getInstance().getCurrentTeacher().getU_id())) {
                btnDelete.setVisibility(View.VISIBLE);
                btnSubmit.setVisibility(View.GONE);
                llTeacher.setVisibility(View.GONE);
                LocalImageTHelper.getInstance().setCurrentEnableMaxChoiceSize(
                        LocalImageTHelper.getInstance().getMaxChoiceSize() - currentHomeWork.getFile_path().size());
                final List<BeanClass> beanClasses = GreenDaoHelper.getInstance().getAllClass();
                if (beanClasses.size() > 0 && allCourse.size() > 0) {
                    setTxtRight("编辑");
                } else {
                    setTxtRight("");
                }
                setTextRightClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View view) {
                        availableEdit(beanClasses, allCourse);
                    }
                });
            } else {
                //他人布置的作业
                llTeacher.setVisibility(View.VISIBLE);
                btnDelete.setVisibility(View.GONE);
                btnSubmit.setVisibility(View.GONE);
                txtTeacher.setText(currentHomeWork.getUser_name());
            }
        } else {
            //发布作业
            llTeacher.setVisibility(View.GONE);
            btnDelete.setVisibility(View.GONE);
            btnSubmit.setVisibility(View.VISIBLE);
            txtCompleteTime.setText(CommonUtil.getDate2StrAfter(1));
        }

        //
        if (currentHomeWork != null) {
            edtName.setText(currentHomeWork.getName());
            txtPushTime.setText(currentHomeWork.getCreate_time());
            txtCompleteTime.setText(currentHomeWork.getFinish_time());
            edtContent.setText(currentHomeWork.getWork_content());
            spnClasses.setItems(currentHomeWork.getG_name() + currentHomeWork.getC_name());
//            spnClasses.setSelectedIndex(GreenDaoHelper.getInstance().getClassIndexByCId(currentHomeWork.getC_id()));
            String courseName = GreenDaoHelper.getInstance().getCourseNameById(currentHomeWork.getCrs_id());
            if (courseName == null || courseName.isEmpty()) {
                setTxtRight("");
                spnCourse.setItems(currentHomeWork.getCrs_name());
            } else {
                spnCourse.setItems(courseName);
            }
        } else {
            llCreateTime.setVisibility(View.GONE);
            edtName.setText(spnCourse.getText() + "作业");
            edtName.setSelection(edtName.getText().length());
        }

        initVoice(currentHomeWork);
        hideVoiceDel();
    }

    private void availableEdit(List<BeanClass> beanClasses, List<BeanCourse> allCourse) {
        setViewEnable(true);

        spnClasses.setItems(beanClasses);
        for (int i = 0; i < beanClasses.size(); i++) {
            BeanClass beanClass = beanClasses.get(i);
            if (beanClass.getG_id().equals(currentHomeWork.getG_id())
                    && beanClass.getC_id().equals(currentHomeWork.getC_id())) {
                spnClasses.setSelectedIndex(i);
                break;
            }
        }

        spnCourse.setItems(allCourse);
        for (int i = 0; i < allCourse.size(); i++) {
            if (allCourse.get(i).getId().equals(currentHomeWork.getCrs_id())) {
                spnClasses.setSelectedIndex(i);
                break;
            }
        }

        hideViewPager(albumviewpager);
        btnSubmit.setVisibility(View.VISIBLE);
        btnSubmit.setText("重新发布");
        setTxtRight("");
        edtName.setSelection(edtName.getText().toString().length());

        showVoiceDel();
    }

    private void loadCourseByClass(BeanClass item) {
        if (item == null) {
            spnCourse.setItems("无课程");
            return;
        }
        List<BeanCourse> courses = new ArrayList<BeanCourse>();
        if (item != null && item.getG_id() != null) {
            courses = GreenDaoHelper.getInstance().getCourseByGId(item.getG_id());
        }
        if (courses.size() == 0) {
            spnCourse.setItems("无课程");
        } else {
            spnCourse.setItems(courses);
        }
    }

    private void setViewEnable(boolean enable) {
        canModify = enable;
        spnClasses.setEnabled(enable);
        spnCourse.setEnabled(enable);
        edtName.setEnabled(enable);
        txtPushTime.setEnabled(enable);
        txtCompleteTime.setEnabled(enable);
        edtContent.setEnabled(enable);
        myPicGridAdapter.initDate(currentHomeWork.getFile_path(), enable);
    }

    @OnClick({R.id.imgMic, R.id.imgDelete, R.id.txtCompleteTime, R.id.btnSubmit, R.id.btnDelete})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.imgMic:
                recorderOrPlayVoice();
                break;
            case R.id.imgDelete:
                deleteOldVoice();
                break;
            case R.id.btnSubmit:
                if (VoiceStatus == Voice_Recording) {
                    ToastUtils.showToast(this, R.string.toast_record_unstop);
                    return;
                }

                String name = edtName.getText().toString().trim();
                String content = edtContent.getText().toString().trim();
                String finishTime = txtCompleteTime.getText().toString();
                if (name.isEmpty() || content.isEmpty() || finishTime.isEmpty()) {
                    ToastUtils.showToast(this, R.string.toast_homework_empty);
                    return;
                }

                BeanHomeWork homeWork = new BeanHomeWork();
                BeanClass currentClass = (BeanClass) spnClasses.getSelectedItem();
                if (currentClass == null) {
                    ToastUtils.showToast(this, "请选择班级");
                    return;
                }
                BeanCourse course = (BeanCourse) spnCourse.getSelectedItem();
                if (course == null) {
                    ToastUtils.showToast(this, "请选择科目");
                    return;
                }
                homeWork.setCrs_id(course.getId());
                homeWork.setName(name);
                homeWork.setWork_content(content);
                homeWork.setFinish_time(finishTime);
                homeWork.setC_id(currentClass.getC_id());
                homeWork.setC_name(currentClass.getC_name());
                homeWork.setG_id(currentClass.getG_id());
                homeWork.setG_name(currentClass.getG_name());

                homeWork.setCrs_name(spnCourse.getText().toString().trim());
                createHomework(homeWork);
                break;
            case R.id.btnDelete:
                CustomDialog dialog = new CustomDialog(this);
                dialog.setTitle(R.string.home_homework);
                dialog.setMessage(R.string.msg_delete_homework);
                dialog.setAlertDialogClickListener(new CustomDialog.DialogClickListener() {
                    @Override
                    public void onPositiveClick() {
                        deleteHomework();
                    }
                });
                break;
            case R.id.txtCompleteTime:
                if (completeDate == null) {
                    completeDate = new TimePickerPopupWindow(HomeWorkDetailTeacherActivity.this, txtCompleteTime.getText().toString(),
                            new TimePickerPopupWindow.OnTimePickerClickListener() {

                                @Override
                                public void onTimePickerResult(String result) {
                                    if (!result.isEmpty())
                                        txtCompleteTime.setText(result);
                                }
                            });
                    completeDate.setTouchable(true);
                    completeDate.setSoftInputMode(WindowManager.LayoutParams.SOFT_INPUT_ADJUST_RESIZE);
                    completeDate.setBackgroundDrawable(new ColorDrawable());
                    completeDate.setOnDismissListener(new PopupWindow.OnDismissListener() {
                        @Override
                        public void onDismiss() {
                            backgroundAlpha(1.0f);
                        }
                    });
                }
                backgroundAlpha(0.5f);
                completeDate.showAtLocation(txtCompleteTime, Gravity.BOTTOM, 0, 0);
                break;
        }
    }

    private void createHomework(final BeanHomeWork homeWork) {
        VolleyHttpParamsEntity entity = new MyVolleyHttpParamsEntity()
                .addParam("h_id", currentHomeWork == null ? "0" : currentHomeWork.getH_id())
                .addParam("name", homeWork.getName())
                .addParam("a_id", GreenDaoHelper.getInstance().getCurrentTeacher().getA_id())
                .addParam("s_id", GreenDaoHelper.getInstance().getCurrentTeacher().getS_id())
                .addParam("g_id", homeWork.getG_id())
                .addParam("c_id", homeWork.getC_id())
                .addParam("crs_id", homeWork.getCrs_id())
                .addParam("work_content", homeWork.getWork_content())
                .addParam("finish_time", homeWork.getFinish_time())
                .addParam("token", CommonUtil.encryptToken(HttpAction.HOMEWORK_ADD));

        List<String> imgPath = myPicGridAdapter.getImgPaths();

        List<String> uploadFile = new ArrayList<>();
        File file = ImageLoader.getInstance().getDiskCache().getDirectory();
        for (int i = 0; i < imgPath.size(); i++) {
            Log.i(TAG, "createHomework: " + imgPath.get(i));
            if (imgPath.get(i).contains("http://")) {
                uploadFile.add(file.getAbsolutePath() + "/" + imgPath.get(i).hashCode());
            } else if (imgPath.get(i).contains("file://")) {
                uploadFile.add(imgPath.get(i).replace("file://", ""));
            }
        }

        String amrPath = localAmrFile;
        if (amrPath == null) {
            Log.i(TAG, "createHomework amrPath: " + amrPath);
            amrPath = mAudioManager.getCurrentFilePath();
        }
        if (amrPath != null) {
            uploadFile.add(amrPath);
        }
        Log.i(TAG, "createHomework amrPath: " + amrPath);

        for (int i = uploadFile.size() - 1; i >= 0; i--) {
            File file1 = new File(uploadFile.get(i));
            Log.i(TAG, "uploadFile: " + file1.getPath() + " size:" + file1.length());
            if (file1.length() > 2 * 1024 * 1024) {
                Toast.makeText(this, "存在大于2MB的文件，请删除后重新添加", Toast.LENGTH_SHORT).show();
                return;
            } else if (file1.length() == 0) {
                uploadFile.remove(i);
            }
        }

        VolleyHttpService.getInstance().uploadFiles(HttpAction.HOMEWORK_ADD, entity, uploadFile,
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        Log.i(TAG, "onStart: ");
                        showProgress(R.string.progress_add_homework);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        hideProgress();
                        if (httpResult.getStatus() == 1) {
                            try {
                                JSONObject json = new JSONObject(httpResult.getData().toString());
                                String hid = json.getString("h_id");
                                String create_time = json.getString("create_time");
                                JSONArray jsonArray = json.getJSONArray("path");
                                List<String> listPaths = new ArrayList<String>();

                                for (int i = 0; i < jsonArray.length(); i++) {
                                    listPaths.add(jsonArray.getString(i));
                                }

                                homeWork.setUser_id(GreenDaoHelper.getInstance().getCurrentTeacher().getU_id());
                                homeWork.setUser_name(GreenDaoHelper.getInstance().getCurrentTeacher().getName());
                                homeWork.setCreate_time(create_time);
                                homeWork.setH_id(hid);
                                homeWork.setFile_path(listPaths);

                                if (currentHomeWork != null) {
                                    currentHomeWork = homeWork;
                                    initData();
                                }

                                //通知其他观察者，作业详情已更改
                                Intent intent = new Intent();
                                intent.setAction(BroadcastAction.HOMEWORK_AMEND);
                                intent.putExtra(ExtraKey.HOMEWORK_DETAIL, currentHomeWork);
                                sendBroadcast(intent);

                            } catch (Exception ex) {
                                Log.i(TAG, "onResponse: " + ex.getMessage());
                            } finally {
                                ToastUtils.showToast(HomeWorkDetailTeacherActivity.this, "发布成功！");
                                finish();
                            }
                        } else {
                            Toast.makeText(HomeWorkDetailTeacherActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError error) {
                        super.onErrorResponse(error);
                        ToastUtils.showToast(HomeWorkDetailTeacherActivity.this, "发布失败！");
                        hideProgress();
                    }
                });
    }

    private void deleteHomework() {
        if (currentHomeWork == null) {
            return;
        }
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.HOMEWORK_DEL, new MyVolleyHttpParamsEntity()
                        .addParam("h_id", currentHomeWork.getH_id())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.HOMEWORK_DEL)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        showProgress(R.string.progress_del_homework);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult httpResult) {
                        super.onResponse(httpResult);
                        hideProgress();
                        Toast.makeText(HomeWorkDetailTeacherActivity.this, httpResult.getInfo(), Toast.LENGTH_SHORT).show();
                        if (httpResult.getStatus() == HttpAction.SUCCESS) {
                            Intent intent = new Intent();
                            intent.putExtra(ExtraKey.HOMEWORK_DETAIL, currentHomeWork);
                            setResult(ActivityResultCode.HomeWork_delete, intent);
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

    @Override
    public void onBackPressed() {
        if (albumviewpager.getVisibility() == View.VISIBLE) {
            hideViewPager(albumviewpager);
        } else {
            super.onBackPressed();
        }
    }

}
