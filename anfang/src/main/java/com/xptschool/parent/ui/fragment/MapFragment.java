package com.xptschool.parent.ui.fragment;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.os.Bundle;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.PopupWindow;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.spinner.MaterialSpinner;
import com.android.widget.view.ArrowTextView;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.model.LatLng;
import com.baidu.mapapi.search.share.LocationShareURLOption;
import com.baidu.mapapi.search.share.OnGetShareUrlResultListener;
import com.baidu.mapapi.search.share.ShareUrlResult;
import com.baidu.mapapi.search.share.ShareUrlSearch;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.umeng.socialize.ShareAction;
import com.umeng.socialize.bean.SHARE_MEDIA;
import com.umeng.socialize.media.UMImage;
import com.umeng.socialize.media.UMWeb;
import com.xptschool.parent.R;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.bean.BeanHTLocation;
import com.xptschool.parent.bean.BeanRTLocation;
import com.xptschool.parent.bean.BeanRail;
import com.xptschool.parent.common.CommonUtil;
import com.xptschool.parent.common.UserHelper;
import com.xptschool.parent.common.UserType;
import com.xptschool.parent.http.HttpAction;
import com.xptschool.parent.http.HttpErrorMsg;
import com.xptschool.parent.http.MyVolleyHttpParamsEntity;
import com.xptschool.parent.http.MyVolleyRequestListener;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.GreenDaoHelper;
import com.xptschool.parent.ui.alarm.AlarmActivity;
import com.xptschool.parent.ui.main.MainActivity;
import com.xptschool.parent.ui.mine.StudentAdapter;
import com.xptschool.parent.ui.mine.StudentPopupWindowView;
import com.xptschool.parent.util.ToastUtils;
import com.xptschool.parent.view.CustomMapDialog;
import com.xptschool.parent.view.TimePickerPopupWindow;

import java.text.SimpleDateFormat;
import java.util.List;
import java.util.Timer;
import java.util.TimerTask;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import permissions.dispatcher.NeedsPermission;
import permissions.dispatcher.OnNeverAskAgain;
import permissions.dispatcher.OnPermissionDenied;
import permissions.dispatcher.OnShowRationale;
import permissions.dispatcher.PermissionRequest;
import permissions.dispatcher.RuntimePermissions;

@RuntimePermissions
public class MapFragment extends MapBaseFragment implements OnGetShareUrlResultListener {

    @BindView(R.id.spnStudents)
    MaterialSpinner spnStudents;
    @BindView(R.id.llStudentName)
    LinearLayout llStudentName;
    @BindView(R.id.txtStudentName)
    ArrowTextView txtStudentName;

    @BindView(R.id.txtSDate)
    ArrowTextView txtSDate;
    @BindView(R.id.txtEDate)
    ArrowTextView txtEDate;
    @BindView(R.id.flTransparent)
    FrameLayout flTransparent;
    @BindView(R.id.llLocation)
    LinearLayout llLocation;
    @BindView(R.id.llTrack)
    LinearLayout llTrack;
    @BindView(R.id.llRailings)
    LinearLayout llRailings;

    @BindView(R.id.llBindRoad)
    LinearLayout llBindRoad;
    @BindView(R.id.switchBindRoad)
    Button switchBindRoad;

    //学生选择控制器
    @BindView(R.id.llStudentDrop)
    LinearLayout llStudentDrop;
    //左侧工具栏
    @BindView(R.id.llMyTools)
    LinearLayout llMyTools;
    @BindView(R.id.llShare)
    LinearLayout llShare;

    @BindView(R.id.progress_bar)
    ProgressBar progress_bar;

    private Timer timer = null;
    private TimerTask task;
    PopupWindow studentPopup;
    StudentPopupWindowView studentPopupWindowView;
    TimePickerPopupWindow sDatePopup, eDatePopup;
    private String startTime, endTime;
    private int locationTime = 0;

    /**
     * 共享URL查询接口
     * <p>
     * 共享URL是指代表特定信息(poi详情/位置)的一条经过压缩的URL，
     * 该URL可通过任何途径传播。 当终端用户点击该URL时，
     * 该URL所代表的特定信息会通过百度地图(客户端/web)重新呈现
     */
    private ShareUrlSearch mShareUrlSearch = null;

    public MapFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, mRootView);
        //初始化传感器
        mSensorManager = (SensorManager) getContext().getSystemService(Context.SENSOR_SERVICE);
        mSensor = mSensorManager.getDefaultSensor(Sensor.TYPE_ORIENTATION);
        return mRootView;
    }

    @Override
    protected void initData() {
        super.initData();
        Log.i(TAG, "initData: ");
        // 开启定位图层
        mBaiduMap.getUiSettings().setRotateGesturesEnabled(true);
        mBaiduMap.setMyLocationEnabled(true);
        // 定位初始化
        mLocClient = new LocationClient(getActivity());
        mLocClient.registerLocationListener(this);

        LocationClientOption option = new LocationClientOption();
        option.setOpenGps(true); // 打开gps
        option.setCoorType("bd09ll"); // 设置坐标类型
        option.setScanSpan(3000);
        mLocClient.setLocOption(option);
        mLocClient.start();
        Log.i(TAG, "initData: start loc");

        UserHelper.getInstance().addUserChangeListener(new UserHelper.UserChangeListener() {
            @Override
            public void onUserLoginSuccess() {
                Log.i(TAG, "onUserLoginSuccess: ");
                initSpinnerData();
            }

            @Override
            public void onUserExit() {
                initSpinnerData();
            }
        });
        initSpinnerData();
    }

    private void initSpinnerData() {
        llMyTools.setVisibility(View.VISIBLE);
        llStudentDrop.setVisibility(View.VISIBLE);
        llShare.setVisibility(View.GONE);

        UserType type = XPTApplication.getInstance().getCurrent_user_type();
        Log.i(TAG, "initSpinnerData: " + type);

        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        if (students.size() > 0) {
            startTime = (new SimpleDateFormat("yyyy-MM-dd HH:mm")).format(CommonUtil.getDateBefore(1));
            endTime = CommonUtil.getCurrentDateTime();
            txtSDate.setText(startTime);
            txtEDate.setText(endTime);

            if (UserType.TEACHER.equals(type)) {
                llStudentName.setVisibility(View.GONE);
                txtStudentName.setVisibility(View.VISIBLE);
                getStudents();
            } else {
                llStudentName.setVisibility(View.VISIBLE);
                txtStudentName.setVisibility(View.GONE);

                if (GreenDaoHelper.getInstance().getStudents().size() == 0) {
                    spnStudents.setText(R.string.title_no_student);
                    spnStudents.setEnabled(false);
                    return;
                }

                spnStudents.setEnabled(true);
                spnStudents.setItems(GreenDaoHelper.getInstance().getStudents());
                currentStudent = (BeanStudent) spnStudents.getSelectedItem();

                spnStudents.setOnItemSelectedListener(new MaterialSpinner.OnItemSelectedListener<BeanStudent>() {
                    @Override
                    public void onItemSelected(MaterialSpinner view, int position, long id, BeanStudent item) {
                        flTransparent.setVisibility(View.GONE);
                        currentStudent = item;
                        locationTime = 0;
                        //获取不同数据
                        reloadDataByStatus();
                    }
                });

                spnStudents.setOnNothingSelectedListener(new MaterialSpinner.OnNothingSelectedListener() {
                    @Override
                    public void onNothingSelected(MaterialSpinner spinner) {
                        flTransparent.setVisibility(View.GONE);
                    }
                });
            }

        } else {
            //隐藏学生功能
            llMyTools.setVisibility(View.GONE);
            llStudentDrop.setVisibility(View.GONE);
            llShare.setVisibility(View.VISIBLE);

            mShareUrlSearch = ShareUrlSearch.newInstance();
            mShareUrlSearch.setOnGetShareUrlResultListener(this);

            llStudentName.setVisibility(View.VISIBLE);
            txtStudentName.setVisibility(View.GONE);

            spnStudents.setText(R.string.title_no_student);
            spnStudents.setEnabled(false);
            return;
        }

        llTrack.setTag(false);
        llLocation.setTag(false);
        llRailings.setTag(false);

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (permissions != null && permissions.length > 0) {
            Log.i(TAG, "onRequestPermissionsResult: " + permissions[0]);
        }
        MapFragmentPermissionsDispatcher.onRequestPermissionsResult(this, requestCode, grantResults);
    }

    @OnClick({R.id.txtSDate, R.id.txtEDate, R.id.llLocation, R.id.llTrack, R.id.llRailings,
            R.id.switchBindRoad, R.id.spnStudents, R.id.txtStudentName, R.id.llAlarm, R.id.llMyLocation,
            R.id.txtShare, R.id.txtMap})
    void viewClick(View view) {
        switch (view.getId()) {
            case R.id.txtSDate:
                showSDatePop();
                break;
            case R.id.txtEDate:
                showEDatePop();
                break;
            case R.id.txtStudentName:
                showStudent();
                break;
            case R.id.spnStudents:
                if (spnStudents.getItems().size() != 1) {
                    flTransparent.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.llLocation:
                if (view.getTag() == null || !(Boolean) view.getTag()) {
                    if (currentStudent == null) {
                        Toast.makeText(getActivity(), R.string.toast_choose_student, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    startRTLocationTimer();
                } else {
                    cancelRTLocationTimer();
                }
                llBindRoad.setVisibility(View.GONE);
                resetDefaultBg(view);
                llTrack.setTag(false);
                llRailings.setTag(false);
                break;
            case R.id.llTrack:
                if (view.getTag() == null || !(Boolean) view.getTag()) {
                    if (currentStudent == null) {
                        Toast.makeText(getActivity(), R.string.toast_choose_student, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    cancelRTLocationTimer();
                    getHistoryTrackByStu();
                    llBindRoad.setVisibility(View.VISIBLE);
                } else {
                    mHandler.removeCallbacksAndMessages(null);
                    llBindRoad.setVisibility(View.GONE);
                }

                resetDefaultBg(view);
                llLocation.setTag(false);
                llRailings.setTag(false);
                break;
            case R.id.switchBindRoad:
                isBindRoadForHistoryTrack = !isBindRoadForHistoryTrack;
                switchBindRoad.setBackgroundResource(isBindRoadForHistoryTrack ? R.drawable.layer_switch_on : R.drawable.layer_switch_off);
                getHistoryTrackByStu();
                break;
            case R.id.llRailings:
                if (view.getTag() == null || !(Boolean) view.getTag()) {
                    if (currentStudent == null) {
                        Toast.makeText(getActivity(), R.string.toast_choose_student, Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Log.i(TAG, "viewClick: llRailings " + false);
                    mHandler.removeCallbacksAndMessages(null);
                    cancelRTLocationTimer();
                    getStudentRail();
                }
                llBindRoad.setVisibility(View.GONE);
                resetDefaultBg(view);
                llTrack.setTag(false);
                llLocation.setTag(false);
                break;
            case R.id.llAlarm:
                startActivity(new Intent(getContext(), AlarmActivity.class));
                break;
            case R.id.llMyLocation:
                MapFragmentPermissionsDispatcher.onLocationPermitWithCheck(this);
                break;
            case R.id.txtShare:
                if (currentLocation == null) {
                    ToastUtils.showToast(mContext, "无定位信息");
                    return;
                }
                ((MainActivity) mContext).showProgress("正在获取定位信息");

                LatLng ll = new LatLng(currentLocation.getLatitude(),
                        currentLocation.getLongitude());
                Log.i(TAG, "viewClick: " + currentLocation.getAddrStr());

                //请求位置信息分享URL
                mShareUrlSearch.requestLocationShareUrl(new LocationShareURLOption()
                        .location(ll).name("共享位置").snippet(""));
                // 共享点位置 --- 共享点名称 --- 通过短URL调起客户端时作为附加信息显示在名称下面
                break;
            case R.id.txtMap:
                //判断手机地图
                if (CommonUtil.isMapAppInstalled()) {
                    CustomMapDialog dialog = new CustomMapDialog(mContext);
                } else {
                    ToastUtils.showToast(mContext, "没有检测到其他地图");
                }
                break;

        }
    }

    @NeedsPermission(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationPermit() {
        Log.i(TAG, "onLocationPermit: ");
        isFirstLoc = true;
        mapStatusChange = false;
    }

    @OnPermissionDenied(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationDenied() {
        Log.i(TAG, "onLocationDenied: ");
        // NOTE: Deal with a denied permission, e.g. by showing specific UI
        // or disabling certain functionality
        Toast.makeText(this.getContext(), R.string.permission_location_denied, Toast.LENGTH_SHORT).show();
    }

    @OnShowRationale(Manifest.permission.ACCESS_FINE_LOCATION)
    void showRationaleForLocation(PermissionRequest request) {
        // NOTE: Show a rationale to explain why the permission is needed, e.g. with a dialog.
        // Call proceed() or cancel() on the provided PermissionRequest to continue or abort
        Log.i(TAG, "showRationaleForLocation: ");
        request.proceed();
    }

    @OnNeverAskAgain(Manifest.permission.ACCESS_FINE_LOCATION)
    void onLocationNeverAskAgain() {
        Log.i(TAG, "onLocationNeverAskAgain: ");
        Toast.makeText(this.getContext(), R.string.permission_location_never_askagain, Toast.LENGTH_SHORT).show();
    }

    private void resetDefaultBg(View currentView) {
        llLocation.setBackgroundDrawable(getResources().getDrawable(R.drawable.map_item_selector));
        llTrack.setBackgroundDrawable(getResources().getDrawable(R.drawable.map_item_selector));
        llRailings.setBackgroundDrawable(getResources().getDrawable(R.drawable.map_item_selector));

        if (currentView.getTag() == null || !(Boolean) currentView.getTag()) {
            currentView.setBackgroundDrawable(getResources().getDrawable(R.drawable.map_item_selected));
            currentView.setTag(true);
        } else {
            currentView.setBackgroundDrawable(getResources().getDrawable(R.drawable.map_item_normal));
            currentView.setTag(false);
        }
        //设置其他按钮tag 为 false
    }

    public void getStudents() {
        Log.i(TAG, "getStudentByClass: ");
        if (studentPopupWindowView == null) {
            studentPopupWindowView = new StudentPopupWindowView(getContext());
            studentPopupWindowView.initData();
            studentPopupWindowView.setMyGridViewClickListener(new StudentAdapter.MyGridViewClickListener() {
                @Override
                public void onGridViewItemClick(BeanStudent student) {
                    studentPopup.dismiss();
                    if (student == null) {
                        return;
                    }
                    txtStudentName.setText(student.getStu_name());
                    //根据学生id获取学生位置
                    if (student != currentStudent) {
                        currentStudent = student;
                        mapStatusChange = false;
                        locationTime = 0;
                    } else {
                        return;
                    }

                    Log.i(TAG, "onGridViewItemClick: " + student.getStu_name());
                    //获取实时位置or历史轨迹or电子围栏
                    if (llLocation.getTag() != null && (Boolean) llLocation.getTag()) {
                        getRealTimeLocationByStu();
                    }
                    if (llTrack.getTag() != null && (Boolean) llTrack.getTag()) {
                        getHistoryTrackByStu();
                    }
                    if (llRailings.getTag() != null && (Boolean) llRailings.getTag()) {
                        getStudentRail();
                    }
                }
            });
        }
    }

    private void showStudent() {
        if (studentPopup == null) {
            studentPopup = new PopupWindow(LinearLayout.LayoutParams.MATCH_PARENT,
                    XPTApplication.getInstance().getWindowHeight() / 2);
            studentPopup.setFocusable(true);
            studentPopup.setTouchable(true);
            studentPopup.setBackgroundDrawable(new BitmapDrawable());
            studentPopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    flTransparent.setVisibility(View.GONE);
                    txtStudentName.collapse();
                }
            });
        }

        studentPopup.setContentView(studentPopupWindowView);

        flTransparent.setVisibility(View.VISIBLE);
        studentPopup.showAsDropDown(txtStudentName, 0, 5);
    }

    private void reloadDataByStatus() {
        if ((Boolean) llLocation.getTag()) {
            getRealTimeLocationByStu();
        }

        if ((Boolean) llTrack.getTag()) {
            getHistoryTrackByStu();
        }

        if ((Boolean) llRailings.getTag()) {
            getStudentRail();
        }
    }

    private void showSDatePop() {
        if (sDatePopup == null) {
            sDatePopup = new TimePickerPopupWindow(this.getContext(), txtSDate.getText().toString(),
                    new TimePickerPopupWindow.OnTimePickerClickListener() {

                        @Override
                        public void onTimePickerResult(String result) {
                            if (!result.isEmpty()) {
                                txtSDate.setText(result);
                                startTime = result;
                            }
                            reloadDataByStatus();
                        }
                    });
            sDatePopup.setTouchable(true);
            sDatePopup.setBackgroundDrawable(new ColorDrawable());
            sDatePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    flTransparent.setVisibility(View.GONE);
                }
            });
        }
        flTransparent.setVisibility(View.VISIBLE);
        sDatePopup.showAsDropDown(txtSDate, 0, 3);
    }

    private void showEDatePop() {
        if (eDatePopup == null) {
            eDatePopup = new TimePickerPopupWindow(this.getContext(), txtEDate.getText().toString(),
                    new TimePickerPopupWindow.OnTimePickerClickListener() {

                        @Override
                        public void onTimePickerResult(String result) {
                            if (!result.isEmpty()) {
                                txtEDate.setText(result);
                                endTime = result;
                            }
                            reloadDataByStatus();
                        }
                    });
            eDatePopup.setTouchable(true);
            eDatePopup.setBackgroundDrawable(new ColorDrawable());
            eDatePopup.setOnDismissListener(new PopupWindow.OnDismissListener() {
                @Override
                public void onDismiss() {
                    flTransparent.setVisibility(View.GONE);
                }
            });
        }
        flTransparent.setVisibility(View.VISIBLE);
        eDatePopup.showAsDropDown(txtEDate, 0, 3);
    }

    private void startRTLocationTimer() {
        locationTime = 0;
        task = new TimerTask() {
            @Override
            public void run() {
                getRealTimeLocationByStu();
            }
        };
        if (timer == null) {
            timer = new Timer(true);
        }
        //start the timer
        timer.schedule(task, 1000, 60 * 1000);
    }

    @Override
    public void onPause() {
        super.onPause();
        cancelRTLocationTimer();
    }

    @Override
    public void onGetPoiDetailShareUrlResult(ShareUrlResult shareUrlResult) {
        Log.i(TAG, "onGetPoiDetailShareUrlResult: " + shareUrlResult.getUrl());
    }

    @Override
    public void onGetLocationShareUrlResult(final ShareUrlResult shareUrlResult) {
        Log.i(TAG, "onGetLocationShareUrlResult: " + shareUrlResult.getUrl());

        ((MainActivity) mContext).hideProgress();

        UMImage image = new UMImage((MainActivity) mContext, R.mipmap.ic_launcher);//资源文件
        UMWeb web = new UMWeb(shareUrlResult.getUrl());
        web.setTitle("信平台");//标题
        web.setThumb(image);  //缩略图
        web.setDescription("点击查看此点位置");//描述

        new ShareAction((MainActivity) mContext)
                .setDisplayList(SHARE_MEDIA.WEIXIN, SHARE_MEDIA.WEIXIN_CIRCLE,
                        SHARE_MEDIA.QQ, SHARE_MEDIA.QZONE)//传入平台
                .withMedia(web)//分享内容
                .setCallback(null)//回调监听器
                .open();
    }

    @Override
    public void onGetRouteShareUrlResult(ShareUrlResult shareUrlResult) {
        Log.i(TAG, "onGetRouteShareUrlResult: " + shareUrlResult.getUrl());
    }

    private void cancelRTLocationTimer() {
        // Stop Timer
        if (timer != null) {
            timer.cancel();
            timer = null;
        }
        if (task != null) {
            task.cancel();
        }
    }

    private void getRealTimeLocationByStu() {
        locationTime++;
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Track_RealTime, new MyVolleyHttpParamsEntity()
                        .addParam("stu_id", currentStudent.getStu_id())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Track_RealTime)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        mHandler.sendEmptyMessage(SHOW_PROGRESS);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        mHandler.sendEmptyMessage(HIDE_PROGRESS);
                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    Gson gson = new Gson();
                                    BeanRTLocation rtLocation = gson.fromJson(volleyHttpResult.getData().toString(), BeanRTLocation.class);

                                    Message message = new Message();
                                    message.what = DrawLocation;
                                    message.arg1 = locationTime;
                                    message.obj = rtLocation;
                                    mHandler.sendMessage(message);
                                } catch (Exception ex) {
                                    Toast.makeText(getActivity(), HttpErrorMsg.ERROR_JSON, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(getActivity(), volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                                cancelRTLocationTimer();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        mHandler.sendEmptyMessage(HIDE_PROGRESS);
                        cancelRTLocationTimer();
                    }
                });
    }

    /**
     * 历史轨迹
     * state绑路状态  0 不绑路  1 绑路
     */
    private void getHistoryTrackByStu() {
        String state_bind_road = "0";
        if (isBindRoadForHistoryTrack) {
            state_bind_road = "1";
        }

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Track_HistoryTrack, new MyVolleyHttpParamsEntity()
                        .addParam("sdate", startTime + ":00")
                        .addParam("edate", endTime + ":00")
                        .addParam("state", state_bind_road)
                        .addParam("stu_id", currentStudent.getStu_id())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Track_HistoryTrack)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        if (progress_bar != null)
                            progress_bar.setVisibility(View.VISIBLE);
                        if (switchBindRoad != null)
                            switchBindRoad.setEnabled(false);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (progress_bar != null)
                            progress_bar.setVisibility(View.GONE);
                        if (switchBindRoad != null)
                            switchBindRoad.setEnabled(true);

                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    Gson gson = new Gson();
                                    List<BeanHTLocation> listLocations = gson.fromJson(volleyHttpResult.getData().toString(),
                                            new TypeToken<List<BeanHTLocation>>() {
                                            }.getType());
                                    drawTrack(listLocations);
                                } catch (Exception ex) {
                                    Toast.makeText(getActivity(), HttpErrorMsg.ERROR_JSON, Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(getActivity(), volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        if (progress_bar != null)
                            progress_bar.setVisibility(View.GONE);
                        if (switchBindRoad != null)
                            switchBindRoad.setEnabled(true);
                    }
                });
    }

    private void getStudentRail() {

        VolleyHttpService.getInstance().sendPostRequest(HttpAction.Track_StudentFence, new MyVolleyHttpParamsEntity()
                        .addParam("stu_id", currentStudent.getStu_id())
                        .addParam("token", CommonUtil.encryptToken(HttpAction.Track_StudentFence)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        if (progress_bar != null)
                            progress_bar.setVisibility(View.VISIBLE);
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (progress_bar != null)
                            progress_bar.setVisibility(View.GONE);

                        switch (volleyHttpResult.getStatus()) {
                            case HttpAction.SUCCESS:
                                try {
                                    Gson gson = new Gson();
                                    List<BeanRail> listFence = gson.fromJson(volleyHttpResult.getData().toString(),
                                            new TypeToken<List<BeanRail>>() {
                                            }.getType());
                                    if (listFence.size() == 0) {
                                        Toast.makeText(getActivity(), R.string.toast_railing_empty, Toast.LENGTH_SHORT).show();
                                    }
                                    drawRail(listFence);
                                } catch (Exception ex) {
                                    Toast.makeText(getActivity(), "解析错误" + ex.getMessage(), Toast.LENGTH_SHORT).show();
                                }
                                break;
                            default:
                                Toast.makeText(getActivity(), volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
                                break;
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        if (progress_bar != null)
                            progress_bar.setVisibility(View.GONE);
                        Toast.makeText(getActivity(), "获取失败", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
