package com.xptschool.parent.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.hyphenate.easeui.model.EaseLocalUser;
import com.hyphenate.easeui.utils.EaseLocalUserHelper;
import com.xptschool.parent.XPTApplication;
import com.xptschool.parent.common.SharedPreferencesUtil;
import com.xptschool.parent.common.UserType;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by dexing on 2016/11/2.
 */
public class GreenDaoHelper {

    private String TAG = GreenDaoHelper.class.getSimpleName();
    private static GreenDaoHelper mInstance = null;
    private SQLiteDatabase writeDB, readDB;
    private static DaoMaster writeDaoMaster, readDaoMaster;
    private static DaoSession writeDaoSession, readDaoSession;
    private BeanParent currentParent;
    private BeanTeacher currentTeacher;

    private GreenDaoHelper() {
    }

    public static GreenDaoHelper getInstance() {
        synchronized (GreenDaoHelper.class) {
            if (mInstance == null) {
                mInstance = new GreenDaoHelper();
            }
        }
        if (writeDaoMaster != null) {
            writeDaoSession = writeDaoMaster.newSession();
        } else {
            writeDaoSession = null;
        }

        if (readDaoMaster != null) {
            readDaoSession = readDaoMaster.newSession();
        } else {
            readDaoSession = null;
        }
        return mInstance;
    }

    public void initGreenDao(Context context) {
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "xpt_anfang", null);

        writeDB = helper.getWritableDatabase();
        readDB = helper.getReadableDatabase();

        writeDaoMaster = new DaoMaster(writeDB);
        readDaoMaster = new DaoMaster(readDB);
    }

    public void clearData() {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanParentDao().deleteAll();
            writeDaoSession.getBeanTeacherDao().deleteAll();
            writeDaoSession.getContactSchoolDao().deleteAll();
            writeDaoSession.getContactTeacherDao().deleteAll();
            writeDaoSession.getBeanStudentDao().deleteAll();
            writeDaoSession.getContactParentDao().deleteAll();
        }
    }

    /**
     * 删除后再插入
     */
    public void insertParent(BeanParent parent) {
        currentParent = parent;
        SharedPreferencesUtil.saveData(XPTApplication.getInstance(), SharedPreferencesUtil.KEY_UID, parent.getU_id());

        if (writeDaoSession != null) {
            writeDaoSession.getBeanParentDao().deleteAll();
            writeDaoSession.getBeanParentDao().insert(parent);
        }
    }

    /**
     * 删除后再插入
     */
    public void insertTeacher(BeanTeacher teacher) {
        currentTeacher = teacher;
        SharedPreferencesUtil.saveData(XPTApplication.getInstance(), SharedPreferencesUtil.KEY_UID, currentTeacher.getU_id());
        Log.i(TAG, "insertTeacher: " + teacher.getLoginName());

        if (writeDaoSession != null) {
            writeDaoSession.getBeanTeacherDao().deleteAll();
            writeDaoSession.getBeanTeacherDao().insert(teacher);
        }
    }

    /**
     * 添加孩子
     *
     * @param students
     */
    public void insertStudent(List<BeanStudent> students) {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanStudentDao().deleteAll();
            writeDaoSession.getBeanStudentDao().insertInTx(students);
        }
    }

    /**
     * 执教班级写入数据库
     */
    public void insertClass(List<BeanClass> listClass) {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanClassDao().deleteAll();
            writeDaoSession.getBeanClassDao().insertInTx(listClass);
        }
    }

    /**
     * 执教课程写入数据库
     */
    public void insertCourse(List<BeanCourse> listCourse) {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanCourseDao().deleteAll();
            writeDaoSession.getBeanCourseDao().insertInTx(listCourse);
        }
    }

    public BeanParent getCurrentParent() {
        if (currentParent == null) {
            if (readDaoSession != null) {
                List<BeanParent> beanParents = readDaoSession.getBeanParentDao().loadAll();
                if (beanParents.size() > 0) {
                    currentParent = beanParents.get(0);
                }
            }
        }
        return currentParent;
    }

    public BeanTeacher getCurrentTeacher() {
        if (currentTeacher == null) {
            if (readDaoSession != null) {
                List<BeanTeacher> listTeachers = readDaoSession.getBeanTeacherDao().loadAll();
                if (listTeachers.size() > 0) {
                    currentTeacher = listTeachers.get(0);
                }
            }
        }
        return currentTeacher;
    }

    public List<BeanStudent> getStudents() {
        if (readDaoSession != null) {
            return readDaoSession.getBeanStudentDao().queryBuilder()
                    .orderAsc(BeanStudentDao.Properties.Stu_name).list();
        }
        return new ArrayList<BeanStudent>();
    }

    public BeanStudent getStudentByStuId(String stuId) {
        return readDaoSession.getBeanStudentDao().queryBuilder().where(BeanStudentDao.Properties.Stu_id.eq(stuId)).limit(1).unique();
    }

    public void updateStudent(BeanStudent student) {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanStudentDao().update(student);
        }
    }

    public void deleteParentData() {
        if (writeDaoSession != null) {
            writeDaoSession.getContactParentDao().deleteAll();
        }
    }

    //保存家长信息
    public void insertContactParent(List<ContactParent> parents) {
        if (writeDaoSession != null) {
            writeDaoSession.getContactParentDao().insertOrReplaceInTx(parents);
        }

        for (int i = 0; i < parents.size(); i++) {
            ContactParent parent = parents.get(i);

            EaseLocalUser localUser = new EaseLocalUser();
            localUser.setUserId(parent.getUser_id());
            localUser.setNickName(parent.getName());
            localUser.setSex(parent.getSex());
            localUser.setType(UserType.PARENT.toString());
            EaseLocalUserHelper.getInstance().insertOrReplaceLocalUser(localUser);
        }

    }

    public void insertContactStudent(List<ContactStudent> students) {
        try {
            if (writeDaoSession != null) {
                writeDaoSession.getContactStudentDao().deleteAll();
                writeDaoSession.getContactStudentDao().insertOrReplaceInTx(students);
            }
        } catch (Exception ex) {
            Log.i(TAG, "insertContactStudent error: " + ex.getMessage());
        }
    }

    //保存联系人(教师)
    public void insertContactTeacher(List<ContactTeacher> teachers) {
        if (writeDaoSession != null) {
            writeDaoSession.getContactTeacherDao().deleteAll();
            writeDaoSession.getContactTeacherDao().insertOrReplaceInTx(teachers);
        }

        for (int i = 0; i < teachers.size(); i++) {
            ContactTeacher teacher = teachers.get(i);

            EaseLocalUser localUser = new EaseLocalUser();
            localUser.setUserId(teacher.getU_id());
            localUser.setNickName(teacher.getName());
            localUser.setSex(teacher.getSex());
            localUser.setType(UserType.TEACHER.toString());
            EaseLocalUserHelper.getInstance().insertOrReplaceLocalUser(localUser);

        }

    }

    public List<ContactTeacher> getContactTeacher() {
        if (readDaoSession != null) {
            return readDaoSession.getContactTeacherDao().loadAll();
        }
        return new ArrayList<ContactTeacher>();
    }

    public List<ContactStudent> getContactStudent() {
        if (readDaoSession != null) {
            return readDaoSession.getContactStudentDao().loadAll();
        }
        return new ArrayList<ContactStudent>();
    }

    public List<ContactParent> getStudentParentBySId(String stu_id) {
        if (readDaoSession != null) {
            return readDaoSession.getContactParentDao().queryBuilder()
                    .where(ContactParentDao.Properties.Stu_id.eq(stu_id)).list();
        }
        return new ArrayList<ContactParent>();
    }

    /**
     * 获取全部班级(包含【全部】选项)
     *
     * @return
     */
    public List<BeanClass> getAllClassNameAppend() {
        List<BeanClass> classes = new ArrayList<>();
        BeanClass all = new BeanClass();
        all.setName("全部");
        all.setC_id("");
        all.setG_id("");
        classes.add(all);
        classes.addAll(getAllClass());
        return classes;
    }

    /**
     * 获取全部班级
     *
     * @return
     */
    public List<BeanClass> getAllClass() {
        List<BeanClass> classes = new ArrayList<>();
        if (readDaoSession != null) {
            classes = readDaoSession.getBeanClassDao().queryBuilder().orderAsc(BeanClassDao.Properties.G_id).list();
        }
        return classes;
    }

    public BeanClass getClassById(String Id) {
        if (readDaoSession != null) {
            return readDaoSession.getBeanClassDao().queryBuilder().where(BeanClassDao.Properties.C_id.eq(Id)).build().unique();
        }
        return null;
    }

    public List<BeanCourse> getCourseByGId(String g_id) {
        List<BeanCourse> courses = new ArrayList<>();
        if (readDaoSession != null) {
            if (g_id.isEmpty() || g_id == null) {
                courses = readDaoSession.getBeanCourseDao().queryBuilder().list();
            } else {
                courses = readDaoSession.getBeanCourseDao().queryBuilder().where(BeanCourseDao.Properties.G_id.eq(g_id)).list();
            }

        }
        return courses;
    }

    /**
     * 获取全部课程(包含【全部】选项)
     *
     * @return
     */
    public List<BeanCourse> getAllCourseNameAppend() {
        List<BeanCourse> courses = new ArrayList<>();
        BeanCourse all = new BeanCourse();
        all.setId("");
        all.setName("全部");
        courses.add(all);
        courses.addAll(getAllCourse());
        return courses;
    }

    /**
     * 获取全部课程
     *
     * @return
     */
    public List<BeanCourse> getAllCourse() {
        List<BeanCourse> courses = new ArrayList<>();
        if (readDaoSession != null) {
            courses = readDaoSession.getBeanCourseDao().loadAll();
        }
        return courses;
    }

    public String getCourseNameById(String id) {
        if (readDaoSession != null) {
            BeanCourse course = readDaoSession.getBeanCourseDao().queryBuilder().where(BeanCourseDao.Properties.Id.eq(id)).unique();
            if (course != null) {
                return course.getName();
            }
        }
        return "";
    }

    public String getClassNameById(String id) {
        if (readDaoSession != null) {
            BeanClass _class = readDaoSession.getBeanClassDao().queryBuilder().where(BeanClassDao.Properties.C_id.eq(id)).unique();
            if (_class != null) {
                return _class.getName();
            }
        }
        return "";
    }

    public ContactTeacher getContactByTeacher(String t_u_id) {
        if (readDaoSession != null) {
            return readDaoSession.getContactTeacherDao().queryBuilder()
                    .where(ContactTeacherDao.Properties.U_id.eq(t_u_id)).limit(1).unique();
        }
        return null;
    }

    public void insertSchoolInfo(List<ContactSchool> schools) {
        if (writeDaoSession != null) {
            writeDaoSession.getContactSchoolDao().deleteAll();
            writeDaoSession.getContactSchoolDao().insertOrReplaceInTx(schools);
        }
    }

    public List<ContactSchool> getSchoolInfo() {
        if (readDaoSession != null) {
            return readDaoSession.getContactSchoolDao().loadAll();
        }
        return new ArrayList<ContactSchool>();
    }

    public void insertBanner(List<BeanBanner> banners) {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanBannerDao().deleteAll();
            writeDaoSession.getBeanBannerDao().insertOrReplaceInTx(banners);
        }
    }

    public List<BeanBanner> getBanners() {
        List<BeanBanner> banners = null;
        if (readDaoSession != null) {
            banners = readDaoSession.getBeanBannerDao().loadAll();
        }
        if (banners == null) {
            banners = new ArrayList<BeanBanner>();
        }
        return banners;
    }

    //保存首页配置项
    public void insertHomeCfg(List<BeanHomeCfg> homeCfgs, String type) {
        if (writeDaoSession != null) {
            readDaoSession.getBeanHomeCfgDao().queryBuilder()
                    .where(BeanHomeCfgDao.Properties.Type.eq(type)).buildDelete().executeDeleteWithoutDetachingEntities();

            writeDaoSession.getBeanHomeCfgDao().insertOrReplaceInTx(homeCfgs);
        }
    }

    public List<BeanHomeCfg> getHomeCfgByType(String type) {
        List<BeanHomeCfg> homeCfgs = null;
        if (readDaoSession != null) {
            homeCfgs = readDaoSession.getBeanHomeCfgDao().queryBuilder()
                    .where(BeanHomeCfgDao.Properties.Type.eq(type)).list();
        }
        if (homeCfgs == null) {
            homeCfgs = new ArrayList<BeanHomeCfg>();
        }
        return homeCfgs;
    }

    //插入省市区数据
    public void insertCounties(List<BeanCounty> counties) {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanCountyDao().deleteAll();
            writeDaoSession.getBeanCountyDao().insertOrReplaceInTx(counties);
        }
    }

    //根据父级 id 获取地区
    public List<BeanCounty> getCountiesByParentId(String parentId) {
        List<BeanCounty> counties = null;
        if (readDaoSession != null) {
            counties = readDaoSession.getBeanCountyDao().queryBuilder()
                    .where(BeanCountyDao.Properties.Parent_id.eq(parentId)).list();
        }
        if (counties == null) {
            counties = new ArrayList<BeanCounty>();
        }
        return counties;
    }

}