package com.hyphenate.easeui.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "EASE_LOCAL_USER".
*/
public class EaseLocalUserDao extends AbstractDao<EaseLocalUser, String> {

    public static final String TABLENAME = "EASE_LOCAL_USER";

    /**
     * Properties of entity EaseLocalUser.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property UserId = new Property(0, String.class, "userId", true, "USER_ID");
        public final static Property NickName = new Property(1, String.class, "nickName", false, "NICK_NAME");
        public final static Property Type = new Property(2, String.class, "type", false, "TYPE");
        public final static Property Sex = new Property(3, String.class, "sex", false, "SEX");
    };


    public EaseLocalUserDao(DaoConfig config) {
        super(config);
    }
    
    public EaseLocalUserDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"EASE_LOCAL_USER\" (" + //
                "\"USER_ID\" TEXT PRIMARY KEY NOT NULL ," + // 0: userId
                "\"NICK_NAME\" TEXT," + // 1: nickName
                "\"TYPE\" TEXT," + // 2: type
                "\"SEX\" TEXT);"); // 3: sex
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"EASE_LOCAL_USER\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, EaseLocalUser entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(2, nickName);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(3, type);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(4, sex);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, EaseLocalUser entity) {
        stmt.clearBindings();
 
        String userId = entity.getUserId();
        if (userId != null) {
            stmt.bindString(1, userId);
        }
 
        String nickName = entity.getNickName();
        if (nickName != null) {
            stmt.bindString(2, nickName);
        }
 
        String type = entity.getType();
        if (type != null) {
            stmt.bindString(3, type);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(4, sex);
        }
    }

    @Override
    public String readKey(Cursor cursor, int offset) {
        return cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0);
    }    

    @Override
    public EaseLocalUser readEntity(Cursor cursor, int offset) {
        EaseLocalUser entity = new EaseLocalUser( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // userId
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // nickName
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // type
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // sex
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, EaseLocalUser entity, int offset) {
        entity.setUserId(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setNickName(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setType(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setSex(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final String updateKeyAfterInsert(EaseLocalUser entity, long rowId) {
        return entity.getUserId();
    }
    
    @Override
    public String getKey(EaseLocalUser entity) {
        if(entity != null) {
            return entity.getUserId();
        } else {
            return null;
        }
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}
