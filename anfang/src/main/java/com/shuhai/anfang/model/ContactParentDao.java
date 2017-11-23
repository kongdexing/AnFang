package com.shuhai.anfang.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.internal.DaoConfig;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.
/** 
 * DAO for table "CONTACT_PARENT".
*/
public class ContactParentDao extends AbstractDao<ContactParent, Void> {

    public static final String TABLENAME = "CONTACT_PARENT";

    /**
     * Properties of entity ContactParent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Sp_id = new Property(0, String.class, "sp_id", false, "SP_ID");
        public final static Property Stu_id = new Property(1, String.class, "stu_id", false, "STU_ID");
        public final static Property Name = new Property(2, String.class, "name", false, "NAME");
        public final static Property Phone = new Property(3, String.class, "phone", false, "PHONE");
    };


    public ContactParentDao(DaoConfig config) {
        super(config);
    }
    
    public ContactParentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"CONTACT_PARENT\" (" + //
                "\"SP_ID\" TEXT," + // 0: sp_id
                "\"STU_ID\" TEXT," + // 1: stu_id
                "\"NAME\" TEXT," + // 2: name
                "\"PHONE\" TEXT);"); // 3: phone
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"CONTACT_PARENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, ContactParent entity) {
        stmt.clearBindings();
 
        String sp_id = entity.getSp_id();
        if (sp_id != null) {
            stmt.bindString(1, sp_id);
        }
 
        String stu_id = entity.getStu_id();
        if (stu_id != null) {
            stmt.bindString(2, stu_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, ContactParent entity) {
        stmt.clearBindings();
 
        String sp_id = entity.getSp_id();
        if (sp_id != null) {
            stmt.bindString(1, sp_id);
        }
 
        String stu_id = entity.getStu_id();
        if (stu_id != null) {
            stmt.bindString(2, stu_id);
        }
 
        String name = entity.getName();
        if (name != null) {
            stmt.bindString(3, name);
        }
 
        String phone = entity.getPhone();
        if (phone != null) {
            stmt.bindString(4, phone);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public ContactParent readEntity(Cursor cursor, int offset) {
        ContactParent entity = new ContactParent( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // sp_id
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // stu_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3) // phone
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, ContactParent entity, int offset) {
        entity.setSp_id(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setStu_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setName(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setPhone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(ContactParent entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(ContactParent entity) {
        return null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}