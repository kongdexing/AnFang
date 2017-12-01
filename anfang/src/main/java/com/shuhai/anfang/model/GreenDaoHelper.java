package com.shuhai.anfang.model;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.shuhai.anfang.XPTApplication;
import com.shuhai.anfang.common.SharedPreferencesUtil;

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
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "xpt_parent", null);

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
            writeDaoSession.getContactTeacherForParentDao().deleteAll();
            writeDaoSession.getContactTeacherForTeacherDao().deleteAll();
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
        Log.i(TAG, "insertTeacher: "+teacher.getLoginName());

        if (writeDaoSession != null) {
            writeDaoSession.getBeanTeacherDao().deleteAll();
            writeDaoSession.getBeanTeacherDao().insert(teacher);
        }
    }

    /**
     * 添加班级
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

    public void insertContactParent(List<ContactParent> parents) {
        if (writeDaoSession != null) {
            writeDaoSession.getContactParentDao().insertOrReplaceInTx(parents);
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

    //家长获取联系人
    public void insertContactTeacherForParent(List<ContactTeacherForParent> teachers) {
        if (writeDaoSession != null) {
            writeDaoSession.getContactTeacherForParentDao().deleteAll();
            writeDaoSession.getContactTeacherForParentDao().insertOrReplaceInTx(teachers);
        }
    }

    //教师获取联系人
    public void insertContactTeacherForTeacher(List<ContactTeacherForTeacher> teachers) {
        if (writeDaoSession != null) {
            writeDaoSession.getContactTeacherForTeacherDao().deleteAll();
            writeDaoSession.getContactTeacherForTeacherDao().insertOrReplaceInTx(teachers);
        }
    }

    public void deleteContact() {
        if (writeDaoSession != null) {
            writeDaoSession.getContactTeacherForParentDao().deleteAll();
            writeDaoSession.getContactTeacherForTeacherDao().deleteAll();
            writeDaoSession.getContactSchoolDao().deleteAll();
        }
    }

    public List<ContactTeacherForParent> getContactTeacherForParent() {
        if (readDaoSession != null) {
            return readDaoSession.getContactTeacherForParentDao().loadAll();
        }
        return new ArrayList<ContactTeacherForParent>();
    }

    public List<ContactTeacherForTeacher> getContactTeacherForTeacher() {
        if (readDaoSession != null) {
            return readDaoSession.getContactTeacherForTeacherDao().loadAll();
        }
        return new ArrayList<ContactTeacherForTeacher>();
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

    public ContactTeacherForParent getContactByTeacher(String t_u_id) {
        if (readDaoSession != null) {
            return readDaoSession.getContactTeacherForParentDao().queryBuilder()
                    .where(ContactTeacherForParentDao.Properties.U_id.eq(t_u_id)).limit(1).unique();
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
    public void insertHomeCfg(List<BeanHomeCfg> homeCfgs){
        if (writeDaoSession != null) {
            writeDaoSession.getBeanHomeCfgDao().deleteAll();
            writeDaoSession.getBeanHomeCfgDao().insertOrReplaceInTx(homeCfgs);
        }
    }

    //删除所有子项
    public void deleteHomeChildCfg(){
        if (writeDaoSession != null) {
            writeDaoSession.getBeanHomeCfgChildDao().deleteAll();
        }
    }

    //保存首页配置子项
    public void insertHomeChildCfg(List<BeanHomeCfgChild> homeCfgs){
        if (writeDaoSession != null) {
            writeDaoSession.getBeanHomeCfgChildDao().insertOrReplaceInTx(homeCfgs);
        }
    }

    public List<BeanHomeCfg> getHomeCfg(){
        List<BeanHomeCfg> homeCfgs = null;
        if (readDaoSession != null) {
            homeCfgs = readDaoSession.getBeanHomeCfgDao().loadAll();
        }
        if (homeCfgs == null) {
            homeCfgs = new ArrayList<BeanHomeCfg>();
        }
        return homeCfgs;
    }

    public List<BeanHomeCfgChild> getHomeChildCfgById(String pid){
        List<BeanHomeCfgChild> homeCfgs = null;
        if (readDaoSession != null) {
            homeCfgs = readDaoSession.getBeanHomeCfgChildDao().queryBuilder()
                    .where(BeanHomeCfgChildDao.Properties.Pid.eq(pid)).list();
        }
        if (homeCfgs == null) {
            homeCfgs = new ArrayList<BeanHomeCfgChild>();
        }
        return homeCfgs;
    }

    /*热门商品*/
    public void insertHotGoods(List<BeanHotGood> hotGoods) {
        if (writeDaoSession != null) {
            writeDaoSession.getBeanHotGoodDao().deleteAll();
            writeDaoSession.getBeanHotGoodDao().insertOrReplaceInTx(hotGoods);
        }
    }

    public List<BeanHotGood> getHotGoods() {
        List<BeanHotGood> hotGoods = null;
        if (readDaoSession != null) {
            hotGoods = readDaoSession.getBeanHotGoodDao().loadAll();
        }
        if (hotGoods == null) {
            hotGoods = new ArrayList<BeanHotGood>();
        }
        return hotGoods;
    }

}