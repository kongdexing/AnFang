package com.hyphenate.easeui.utils;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.hyphenate.easeui.model.DaoMaster;
import com.hyphenate.easeui.model.DaoSession;
import com.hyphenate.easeui.model.EaseLocalUser;
import com.hyphenate.easeui.model.EaseLocalUserDao;

import java.util.List;

/**
 * Created by shuhaixinxi on 2017/12/19.
 */

public class EaseLocalUserHelper {

    private String TAG = EaseLocalUserHelper.class.getSimpleName();
    private static EaseLocalUserHelper mInstance = null;
    private SQLiteDatabase writeDB, readDB;
    private static DaoMaster writeDaoMaster, readDaoMaster;
    private static DaoSession writeDaoSession, readDaoSession;

    private EaseLocalUserHelper() {
    }

    public static EaseLocalUserHelper getInstance() {
        synchronized (EaseLocalUserHelper.class) {
            if (mInstance == null) {
                mInstance = new EaseLocalUserHelper();
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
        DaoMaster.DevOpenHelper helper = new DaoMaster.DevOpenHelper(context, "xpt_ease", null);

        writeDB = helper.getWritableDatabase();
        readDB = helper.getReadableDatabase();

        writeDaoMaster = new DaoMaster(writeDB);
        readDaoMaster = new DaoMaster(readDB);
    }

    /**
     * 更新用户信息
     */
    public void insertOrReplaceLocalUser(EaseLocalUser localUser) {
        if (writeDaoSession != null) {
            writeDaoSession.getEaseLocalUserDao().insertOrReplace(localUser);
        }
    }

    /**
     * 获取用户信息
     */
    public String getLocalUserNickName(String userId) {
        try {
            if (readDaoSession != null) {
                return readDaoSession.getEaseLocalUserDao().queryBuilder().where(EaseLocalUserDao.Properties.UserId.eq(userId)).limit(1).unique().getNickName();
            }
            return "";
        } catch (Exception ex) {
            return "";
        }
    }


}
