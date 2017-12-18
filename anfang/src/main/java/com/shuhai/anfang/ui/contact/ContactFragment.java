package com.shuhai.anfang.ui.contact;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.android.volley.VolleyError;
import com.android.volley.common.VolleyHttpParamsEntity;
import com.android.volley.common.VolleyHttpResult;
import com.android.volley.common.VolleyHttpService;
import com.android.widget.groupexpandable.FloatingGroupExpandableListView;
import com.android.widget.groupexpandable.WrapperExpandableListAdapter;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import com.shuhai.anfang.R;
import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.bean.ContactType;
import com.shuhai.anfang.common.CommonUtil;
import com.shuhai.anfang.common.UserHelper;
import com.shuhai.anfang.common.UserType;
import com.shuhai.anfang.http.HttpAction;
import com.shuhai.anfang.http.MyVolleyRequestListener;
import com.shuhai.anfang.model.BeanStudent;
import com.shuhai.anfang.model.ContactParent;
import com.shuhai.anfang.model.ContactSchool;
import com.shuhai.anfang.model.ContactStudent;
import com.shuhai.anfang.model.ContactTeacher;
import com.shuhai.anfang.model.GreenDaoHelper;
import com.shuhai.anfang.ui.fragment.BaseFragment;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by dexing on 2017-11-23 0023.
 */

public class ContactFragment extends BaseFragment {

    @BindView(R.id.ll_contacts)
    LinearLayout ll_contacts;
    @BindView(R.id.edtContent)
    EditText edtContent;
    @BindView(R.id.progress)
    ProgressBar progress;
    @BindView(R.id.expandableview)
    FloatingGroupExpandableListView expandableview;
    @BindView(R.id.swipe_refresh_widget)
    SwipeRefreshLayout swipe_refresh_widget;
    WrapperExpandableListAdapter wrapperAdapter;
    ContactsAdapter adapter;

    public ContactFragment() {
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        mRootView = inflater.inflate(R.layout.fragment_contacts, container, false);
        ButterKnife.bind(this, mRootView);
        initView();
        return mRootView;
    }

    private void initView() {
        adapter = new ContactsAdapter(mContext);

        wrapperAdapter = new WrapperExpandableListAdapter(adapter);
        expandableview.setAdapter(wrapperAdapter);
        swipe_refresh_widget.setColorSchemeColors(getResources().getIntArray(R.array.google_colors));
        swipe_refresh_widget.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                getContacts();
            }
        });

        edtContent.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "beforeTextChanged: ");
            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                Log.i(TAG, "onTextChanged: " + charSequence.toString());
                adapter.reloadByName(charSequence.toString());
                expandContactList();
            }

            @Override
            public void afterTextChanged(Editable editable) {
                Log.i(TAG, "afterTextChanged: ");
            }
        });

        UserHelper.getInstance().addUserChangeListener(new UserHelper.UserChangeListener() {
            @Override
            public void onUserLoginSuccess() {
                //用户切换后，重新获取广告位信息
                Log.i(TAG, "onUserLoginSuccess: ContactFragment");
                initData();
            }

            @Override
            public void onUserExit() {
                if (adapter != null)
                    adapter.clear();
            }
        });
    }

    @Override
    protected void initData() {
        //判断老师家长
        if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            ArrayList<Object> listTeacher = (ArrayList) GreenDaoHelper.getInstance().getContactTeacher();
            ArrayList<Object> listSchool = (ArrayList) GreenDaoHelper.getInstance().getSchoolInfo();

            if (listTeacher.size() > 0 || listSchool.size() > 0) {
                setContactForParent(listTeacher, listSchool);
            }
        } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            ArrayList<Object> listTeacher = (ArrayList) GreenDaoHelper.getInstance().getContactTeacher();
            ArrayList<Object> listStudent = (ArrayList) GreenDaoHelper.getInstance().getContactStudent();
            for (int i = 0; i < listStudent.size(); i++) {
                ContactStudent student = (ContactStudent) listStudent.get(i);
                List<ContactParent> parents = GreenDaoHelper.getInstance().getStudentParentBySId(student.getStu_id());
                student.setParent(parents);
            }

            if (listTeacher.size() > 0 || listStudent.size() > 0) {
                setContactForTeacher(listTeacher, listStudent);
            }
        }

        getContacts();
    }

    private void getContacts() {
        ll_contacts.setVisibility(View.VISIBLE);
        //判断老师家长
        if (UserType.PARENT.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            getContactForParent();
        } else if (UserType.TEACHER.equals(XPTApplication.getInstance().getCurrent_user_type())) {
            getContactForTeacher();
        } else {
            ll_contacts.setVisibility(View.GONE);
        }
    }

    private void getContactForParent() {
        VolleyHttpService.getInstance().sendPostRequest(HttpAction.MyContacts_ForParent,
                new VolleyHttpParamsEntity()
                        .addParam("token", CommonUtil.encryptToken(HttpAction.MyContacts_ForParent)),
                new MyVolleyRequestListener() {
                    @Override
                    public void onStart() {
                        if (swipe_refresh_widget != null) {
                            swipe_refresh_widget.setRefreshing(true);
                        }
                    }

                    @Override
                    public void onResponse(VolleyHttpResult volleyHttpResult) {
                        super.onResponse(volleyHttpResult);
                        if (swipe_refresh_widget != null) {
                            swipe_refresh_widget.setRefreshing(false);
                        }
                        try {
                            JSONObject jsonObject = new JSONObject(volleyHttpResult.getData().toString());
                            Gson gson = new Gson();
                            ArrayList<Object> listTeacher = gson.fromJson(jsonObject.getJSONArray("teacher").toString(),
                                    new TypeToken<List<ContactTeacher>>() {
                                    }.getType());

                            ArrayList<Object> listSchool = gson.fromJson(jsonObject.getJSONArray("school").toString(),
                                    new TypeToken<List<ContactSchool>>() {
                                    }.getType());

                            Log.i(TAG, "onResponse: listTeacher size " + listTeacher.size());
                            Log.i(TAG, "onResponse: listSchool size " + listSchool.size());
                            GreenDaoHelper.getInstance().insertContactTeacher((List) listTeacher);
                            GreenDaoHelper.getInstance().insertSchoolInfo((List) listSchool);
                            setContactForParent(listTeacher, listSchool);
                        } catch (Exception ex) {
                            Log.e(TAG, "onResponse: json " + ex.getMessage());
                        }
                    }

                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        super.onErrorResponse(volleyError);
                        if (swipe_refresh_widget != null) {
                            swipe_refresh_widget.setRefreshing(false);
                        }
                    }
                });
    }

    private void getContactForTeacher() {
        try {
            VolleyHttpService.getInstance().sendPostRequest(HttpAction.MyContacts_ForTeacher, new VolleyHttpParamsEntity()
                            .addParam("s_id", GreenDaoHelper.getInstance().getCurrentTeacher().getS_id())
                            .addParam("a_id", GreenDaoHelper.getInstance().getCurrentTeacher().getA_id())
                            .addParam("token", CommonUtil.encryptToken(HttpAction.MyContacts_ForTeacher)),
                    new MyVolleyRequestListener() {
                        @Override
                        public void onStart() {
                            if (swipe_refresh_widget != null) {
                                swipe_refresh_widget.setRefreshing(true);
                            }
                        }

                        @Override
                        public void onResponse(VolleyHttpResult volleyHttpResult) {
                            super.onResponse(volleyHttpResult);
                            if (swipe_refresh_widget != null) {
                                swipe_refresh_widget.setRefreshing(false);
                            }
                            switch (volleyHttpResult.getStatus()) {
                                case HttpAction.SUCCESS:
                                    try {
                                        JSONObject jsonObject = new JSONObject(volleyHttpResult.getData().toString());
                                        Gson gson = new Gson();
                                        ArrayList<Object> listTeacher = gson.fromJson(jsonObject.getJSONArray("teacher").toString(),
                                                new TypeToken<List<ContactTeacher>>() {
                                                }.getType());

                                        ArrayList<Object> listStudent = gson.fromJson(jsonObject.getJSONArray("student").toString(),
                                                new TypeToken<List<ContactStudent>>() {
                                                }.getType());

                                        Log.i(TAG, "onResponse: listTeacher size " + listTeacher.size());
                                        Log.i(TAG, "onResponse: listStudent size " + listStudent.size());
                                        GreenDaoHelper.getInstance().insertContactTeacher((List) listTeacher);

                                        GreenDaoHelper.getInstance().deleteParentData();
                                        for (int i = 0; i < listStudent.size(); i++) {
                                            GreenDaoHelper.getInstance().insertContactParent(((ContactStudent) listStudent.get(i)).getParent());
                                        }
                                        GreenDaoHelper.getInstance().insertContactStudent((List) listStudent);

                                        setContactForTeacher(listTeacher, listStudent);
                                    } catch (Exception ex) {
                                        Log.e(TAG, "onResponse: json " + ex.getMessage());
                                    }
                                    break;
                                default:
                                    Toast.makeText(mContext, volleyHttpResult.getInfo(), Toast.LENGTH_SHORT).show();
//                                    GreenDaoHelper.getInstance().deleteContact();
                                    break;
                            }

                        }

                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            super.onErrorResponse(volleyError);
                            if (swipe_refresh_widget != null) {
                                swipe_refresh_widget.setRefreshing(false);
                            }
                        }
                    });
        } catch (Exception ex) {
            if (swipe_refresh_widget != null) {
                swipe_refresh_widget.setRefreshing(false);
            }

        }
    }

    private void setContactForParent(ArrayList<Object> listTeacher, ArrayList<Object> listSchool) {
        LinkedHashMap<String, ArrayList<Object>> listContacts = new LinkedHashMap<>();

        List<BeanStudent> students = GreenDaoHelper.getInstance().getStudents();
        for (int j = 0; j < students.size(); j++) {
            ArrayList<Object> teachers = new ArrayList<>();
            BeanStudent student = students.get(j);

            for (int i = 0; i < listSchool.size(); i++) {
                try {
                    ContactSchool school = (ContactSchool) listSchool.get(i);
                    if (student.getS_id().equals(school.getS_id()) &&
                            student.getA_id().equals(school.getA_id())) {
                        teachers.add(school);
                    }
                } catch (Exception ex) {

                }
            }

            for (int i = 0; i < listTeacher.size(); i++) {
                try {
                    ContactTeacher teacher = (ContactTeacher) listTeacher.get(i);
                    //老师指教班级与学生所在班级一致
                    if (teacher.getC_id().contains(student.getC_id())) {
                        teachers.add(teacher);
                    }
                } catch (Exception ex) {

                }
            }
            String title = student.getName() + " (" + student.getG_name() + student.getC_name() + ")";
            listContacts.put(title, teachers);
        }

        adapter.loadContacts(listContacts);
        expandContactList();
    }

    private void setContactForTeacher(ArrayList<Object> listTeacher, ArrayList<Object> listStudent) {
        LinkedHashMap<String, ArrayList<Object>> listContacts = new LinkedHashMap<>();
        listContacts.put(ContactType.TEACHER.toString(), listTeacher);
        listContacts.put(ContactType.STUDENT.toString(), listStudent);
        adapter.loadContacts(listContacts);
        expandContactList();
    }

    private void expandContactList() {
        for (int i = 0; i < wrapperAdapter.getGroupCount(); i++) {
            expandableview.expandGroup(i);
        }
    }


}
