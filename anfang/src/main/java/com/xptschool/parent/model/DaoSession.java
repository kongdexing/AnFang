package com.xptschool.parent.model;

import java.util.Map;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.AbstractDaoSession;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.identityscope.IdentityScopeType;
import org.greenrobot.greendao.internal.DaoConfig;

import com.xptschool.parent.model.BeanLearningModule;
import com.xptschool.parent.model.BeanCounty;
import com.xptschool.parent.model.BeanUser;
import com.xptschool.parent.model.BeanWChat;
import com.xptschool.parent.model.BeanHomeCfg;
import com.xptschool.parent.model.ContactStudent;
import com.xptschool.parent.model.BeanTeacher;
import com.xptschool.parent.model.ContactParent;
import com.xptschool.parent.model.BeanStudent;
import com.xptschool.parent.model.ContactTeacher;
import com.xptschool.parent.model.BeanClass;
import com.xptschool.parent.model.ContactSchool;
import com.xptschool.parent.model.BeanBanner;
import com.xptschool.parent.model.BeanCourse;
import com.xptschool.parent.model.BeanParent;
import com.xptschool.parent.model.BeanMyClass;

import com.xptschool.parent.model.BeanLearningModuleDao;
import com.xptschool.parent.model.BeanCountyDao;
import com.xptschool.parent.model.BeanUserDao;
import com.xptschool.parent.model.BeanWChatDao;
import com.xptschool.parent.model.BeanHomeCfgDao;
import com.xptschool.parent.model.ContactStudentDao;
import com.xptschool.parent.model.BeanTeacherDao;
import com.xptschool.parent.model.ContactParentDao;
import com.xptschool.parent.model.BeanStudentDao;
import com.xptschool.parent.model.ContactTeacherDao;
import com.xptschool.parent.model.BeanClassDao;
import com.xptschool.parent.model.ContactSchoolDao;
import com.xptschool.parent.model.BeanBannerDao;
import com.xptschool.parent.model.BeanCourseDao;
import com.xptschool.parent.model.BeanParentDao;
import com.xptschool.parent.model.BeanMyClassDao;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * {@inheritDoc}
 * 
 * @see org.greenrobot.greendao.AbstractDaoSession
 */
public class DaoSession extends AbstractDaoSession {

    private final DaoConfig beanLearningModuleDaoConfig;
    private final DaoConfig beanCountyDaoConfig;
    private final DaoConfig beanUserDaoConfig;
    private final DaoConfig beanWChatDaoConfig;
    private final DaoConfig beanHomeCfgDaoConfig;
    private final DaoConfig contactStudentDaoConfig;
    private final DaoConfig beanTeacherDaoConfig;
    private final DaoConfig contactParentDaoConfig;
    private final DaoConfig beanStudentDaoConfig;
    private final DaoConfig contactTeacherDaoConfig;
    private final DaoConfig beanClassDaoConfig;
    private final DaoConfig contactSchoolDaoConfig;
    private final DaoConfig beanBannerDaoConfig;
    private final DaoConfig beanCourseDaoConfig;
    private final DaoConfig beanParentDaoConfig;
    private final DaoConfig beanMyClassDaoConfig;

    private final BeanLearningModuleDao beanLearningModuleDao;
    private final BeanCountyDao beanCountyDao;
    private final BeanUserDao beanUserDao;
    private final BeanWChatDao beanWChatDao;
    private final BeanHomeCfgDao beanHomeCfgDao;
    private final ContactStudentDao contactStudentDao;
    private final BeanTeacherDao beanTeacherDao;
    private final ContactParentDao contactParentDao;
    private final BeanStudentDao beanStudentDao;
    private final ContactTeacherDao contactTeacherDao;
    private final BeanClassDao beanClassDao;
    private final ContactSchoolDao contactSchoolDao;
    private final BeanBannerDao beanBannerDao;
    private final BeanCourseDao beanCourseDao;
    private final BeanParentDao beanParentDao;
    private final BeanMyClassDao beanMyClassDao;

    public DaoSession(Database db, IdentityScopeType type, Map<Class<? extends AbstractDao<?, ?>>, DaoConfig>
            daoConfigMap) {
        super(db);

        beanLearningModuleDaoConfig = daoConfigMap.get(BeanLearningModuleDao.class).clone();
        beanLearningModuleDaoConfig.initIdentityScope(type);

        beanCountyDaoConfig = daoConfigMap.get(BeanCountyDao.class).clone();
        beanCountyDaoConfig.initIdentityScope(type);

        beanUserDaoConfig = daoConfigMap.get(BeanUserDao.class).clone();
        beanUserDaoConfig.initIdentityScope(type);

        beanWChatDaoConfig = daoConfigMap.get(BeanWChatDao.class).clone();
        beanWChatDaoConfig.initIdentityScope(type);

        beanHomeCfgDaoConfig = daoConfigMap.get(BeanHomeCfgDao.class).clone();
        beanHomeCfgDaoConfig.initIdentityScope(type);

        contactStudentDaoConfig = daoConfigMap.get(ContactStudentDao.class).clone();
        contactStudentDaoConfig.initIdentityScope(type);

        beanTeacherDaoConfig = daoConfigMap.get(BeanTeacherDao.class).clone();
        beanTeacherDaoConfig.initIdentityScope(type);

        contactParentDaoConfig = daoConfigMap.get(ContactParentDao.class).clone();
        contactParentDaoConfig.initIdentityScope(type);

        beanStudentDaoConfig = daoConfigMap.get(BeanStudentDao.class).clone();
        beanStudentDaoConfig.initIdentityScope(type);

        contactTeacherDaoConfig = daoConfigMap.get(ContactTeacherDao.class).clone();
        contactTeacherDaoConfig.initIdentityScope(type);

        beanClassDaoConfig = daoConfigMap.get(BeanClassDao.class).clone();
        beanClassDaoConfig.initIdentityScope(type);

        contactSchoolDaoConfig = daoConfigMap.get(ContactSchoolDao.class).clone();
        contactSchoolDaoConfig.initIdentityScope(type);

        beanBannerDaoConfig = daoConfigMap.get(BeanBannerDao.class).clone();
        beanBannerDaoConfig.initIdentityScope(type);

        beanCourseDaoConfig = daoConfigMap.get(BeanCourseDao.class).clone();
        beanCourseDaoConfig.initIdentityScope(type);

        beanParentDaoConfig = daoConfigMap.get(BeanParentDao.class).clone();
        beanParentDaoConfig.initIdentityScope(type);

        beanMyClassDaoConfig = daoConfigMap.get(BeanMyClassDao.class).clone();
        beanMyClassDaoConfig.initIdentityScope(type);

        beanLearningModuleDao = new BeanLearningModuleDao(beanLearningModuleDaoConfig, this);
        beanCountyDao = new BeanCountyDao(beanCountyDaoConfig, this);
        beanUserDao = new BeanUserDao(beanUserDaoConfig, this);
        beanWChatDao = new BeanWChatDao(beanWChatDaoConfig, this);
        beanHomeCfgDao = new BeanHomeCfgDao(beanHomeCfgDaoConfig, this);
        contactStudentDao = new ContactStudentDao(contactStudentDaoConfig, this);
        beanTeacherDao = new BeanTeacherDao(beanTeacherDaoConfig, this);
        contactParentDao = new ContactParentDao(contactParentDaoConfig, this);
        beanStudentDao = new BeanStudentDao(beanStudentDaoConfig, this);
        contactTeacherDao = new ContactTeacherDao(contactTeacherDaoConfig, this);
        beanClassDao = new BeanClassDao(beanClassDaoConfig, this);
        contactSchoolDao = new ContactSchoolDao(contactSchoolDaoConfig, this);
        beanBannerDao = new BeanBannerDao(beanBannerDaoConfig, this);
        beanCourseDao = new BeanCourseDao(beanCourseDaoConfig, this);
        beanParentDao = new BeanParentDao(beanParentDaoConfig, this);
        beanMyClassDao = new BeanMyClassDao(beanMyClassDaoConfig, this);

        registerDao(BeanLearningModule.class, beanLearningModuleDao);
        registerDao(BeanCounty.class, beanCountyDao);
        registerDao(BeanUser.class, beanUserDao);
        registerDao(BeanWChat.class, beanWChatDao);
        registerDao(BeanHomeCfg.class, beanHomeCfgDao);
        registerDao(ContactStudent.class, contactStudentDao);
        registerDao(BeanTeacher.class, beanTeacherDao);
        registerDao(ContactParent.class, contactParentDao);
        registerDao(BeanStudent.class, beanStudentDao);
        registerDao(ContactTeacher.class, contactTeacherDao);
        registerDao(BeanClass.class, beanClassDao);
        registerDao(ContactSchool.class, contactSchoolDao);
        registerDao(BeanBanner.class, beanBannerDao);
        registerDao(BeanCourse.class, beanCourseDao);
        registerDao(BeanParent.class, beanParentDao);
        registerDao(BeanMyClass.class, beanMyClassDao);
    }
    
    public void clear() {
        beanLearningModuleDaoConfig.getIdentityScope().clear();
        beanCountyDaoConfig.getIdentityScope().clear();
        beanUserDaoConfig.getIdentityScope().clear();
        beanWChatDaoConfig.getIdentityScope().clear();
        beanHomeCfgDaoConfig.getIdentityScope().clear();
        contactStudentDaoConfig.getIdentityScope().clear();
        beanTeacherDaoConfig.getIdentityScope().clear();
        contactParentDaoConfig.getIdentityScope().clear();
        beanStudentDaoConfig.getIdentityScope().clear();
        contactTeacherDaoConfig.getIdentityScope().clear();
        beanClassDaoConfig.getIdentityScope().clear();
        contactSchoolDaoConfig.getIdentityScope().clear();
        beanBannerDaoConfig.getIdentityScope().clear();
        beanCourseDaoConfig.getIdentityScope().clear();
        beanParentDaoConfig.getIdentityScope().clear();
        beanMyClassDaoConfig.getIdentityScope().clear();
    }

    public BeanLearningModuleDao getBeanLearningModuleDao() {
        return beanLearningModuleDao;
    }

    public BeanCountyDao getBeanCountyDao() {
        return beanCountyDao;
    }

    public BeanUserDao getBeanUserDao() {
        return beanUserDao;
    }

    public BeanWChatDao getBeanWChatDao() {
        return beanWChatDao;
    }

    public BeanHomeCfgDao getBeanHomeCfgDao() {
        return beanHomeCfgDao;
    }

    public ContactStudentDao getContactStudentDao() {
        return contactStudentDao;
    }

    public BeanTeacherDao getBeanTeacherDao() {
        return beanTeacherDao;
    }

    public ContactParentDao getContactParentDao() {
        return contactParentDao;
    }

    public BeanStudentDao getBeanStudentDao() {
        return beanStudentDao;
    }

    public ContactTeacherDao getContactTeacherDao() {
        return contactTeacherDao;
    }

    public BeanClassDao getBeanClassDao() {
        return beanClassDao;
    }

    public ContactSchoolDao getContactSchoolDao() {
        return contactSchoolDao;
    }

    public BeanBannerDao getBeanBannerDao() {
        return beanBannerDao;
    }

    public BeanCourseDao getBeanCourseDao() {
        return beanCourseDao;
    }

    public BeanParentDao getBeanParentDao() {
        return beanParentDao;
    }

    public BeanMyClassDao getBeanMyClassDao() {
        return beanMyClassDao;
    }

}
