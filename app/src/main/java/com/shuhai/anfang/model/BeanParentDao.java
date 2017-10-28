package com.shuhai.anfang.model;

import android.database.Cursor;
import android.database.sqlite.SQLiteStatement;

import org.greenrobot.greendao.AbstractDao;
import org.greenrobot.greendao.Property;
import org.greenrobot.greendao.database.Database;
import org.greenrobot.greendao.database.DatabaseStatement;
import org.greenrobot.greendao.internal.DaoConfig;

// THIS CODE IS GENERATED BY greenDAO, DO NOT EDIT.

/**
 * DAO for table "BEAN_PARENT".
*/
public class BeanParentDao extends AbstractDao<BeanParent, Void> {

    public static final String TABLENAME = "BEAN_PARENT";

    /**
     * Properties of entity BeanParent.<br/>
     * Can be used for QueryBuilder and for referencing column names.
    */
    public static class Properties {
        public final static Property Login_name = new Property(0, String.class, "login_name", false, "LOGIN_NAME");
        public final static Property Sp_id = new Property(1, String.class, "sp_id", false, "SP_ID");
        public final static Property Parent_name = new Property(2, String.class, "parent_name", false, "PARENT_NAME");
        public final static Property Parent_phone = new Property(3, String.class, "parent_phone", false, "PARENT_PHONE");
        public final static Property Relation = new Property(4, String.class, "relation", false, "RELATION");
        public final static Property Sex = new Property(5, String.class, "sex", false, "SEX");
        public final static Property U_id = new Property(6, String.class, "u_id", false, "U_ID");
        public final static Property Address = new Property(7, String.class, "address", false, "ADDRESS");
        public final static Property Work_unit = new Property(8, String.class, "work_unit", false, "WORK_UNIT");
        public final static Property Family_tel = new Property(9, String.class, "family_tel", false, "FAMILY_TEL");
        public final static Property Email = new Property(10, String.class, "email", false, "EMAIL");
        public final static Property Api_id = new Property(11, String.class, "api_id", false, "API_ID");
        public final static Property Security_key = new Property(12, String.class, "security_key", false, "SECURITY_KEY");
    };


    public BeanParentDao(DaoConfig config) {
        super(config);
    }
    
    public BeanParentDao(DaoConfig config, DaoSession daoSession) {
        super(config, daoSession);
    }

    /** Creates the underlying database table. */
    public static void createTable(Database db, boolean ifNotExists) {
        String constraint = ifNotExists? "IF NOT EXISTS ": "";
        db.execSQL("CREATE TABLE " + constraint + "\"BEAN_PARENT\" (" + //
                "\"LOGIN_NAME\" TEXT," + // 0: login_name
                "\"SP_ID\" TEXT," + // 1: sp_id
                "\"PARENT_NAME\" TEXT," + // 2: parent_name
                "\"PARENT_PHONE\" TEXT," + // 3: parent_phone
                "\"RELATION\" TEXT," + // 4: relation
                "\"SEX\" TEXT," + // 5: sex
                "\"U_ID\" TEXT," + // 6: u_id
                "\"ADDRESS\" TEXT," + // 7: address
                "\"WORK_UNIT\" TEXT," + // 8: work_unit
                "\"FAMILY_TEL\" TEXT," + // 9: family_tel
                "\"EMAIL\" TEXT," + // 10: email
                "\"API_ID\" TEXT," + // 11: api_id
                "\"SECURITY_KEY\" TEXT);"); // 12: security_key
    }

    /** Drops the underlying database table. */
    public static void dropTable(Database db, boolean ifExists) {
        String sql = "DROP TABLE " + (ifExists ? "IF EXISTS " : "") + "\"BEAN_PARENT\"";
        db.execSQL(sql);
    }

    @Override
    protected final void bindValues(DatabaseStatement stmt, BeanParent entity) {
        stmt.clearBindings();
 
        String login_name = entity.getLogin_name();
        if (login_name != null) {
            stmt.bindString(1, login_name);
        }
 
        String sp_id = entity.getSp_id();
        if (sp_id != null) {
            stmt.bindString(2, sp_id);
        }
 
        String parent_name = entity.getParent_name();
        if (parent_name != null) {
            stmt.bindString(3, parent_name);
        }
 
        String parent_phone = entity.getParent_phone();
        if (parent_phone != null) {
            stmt.bindString(4, parent_phone);
        }
 
        String relation = entity.getRelation();
        if (relation != null) {
            stmt.bindString(5, relation);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(6, sex);
        }
 
        String u_id = entity.getU_id();
        if (u_id != null) {
            stmt.bindString(7, u_id);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(8, address);
        }
 
        String work_unit = entity.getWork_unit();
        if (work_unit != null) {
            stmt.bindString(9, work_unit);
        }
 
        String family_tel = entity.getFamily_tel();
        if (family_tel != null) {
            stmt.bindString(10, family_tel);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(11, email);
        }
 
        String api_id = entity.getApi_id();
        if (api_id != null) {
            stmt.bindString(12, api_id);
        }
 
        String security_key = entity.getSecurity_key();
        if (security_key != null) {
            stmt.bindString(13, security_key);
        }
    }

    @Override
    protected final void bindValues(SQLiteStatement stmt, BeanParent entity) {
        stmt.clearBindings();
 
        String login_name = entity.getLogin_name();
        if (login_name != null) {
            stmt.bindString(1, login_name);
        }
 
        String sp_id = entity.getSp_id();
        if (sp_id != null) {
            stmt.bindString(2, sp_id);
        }
 
        String parent_name = entity.getParent_name();
        if (parent_name != null) {
            stmt.bindString(3, parent_name);
        }
 
        String parent_phone = entity.getParent_phone();
        if (parent_phone != null) {
            stmt.bindString(4, parent_phone);
        }
 
        String relation = entity.getRelation();
        if (relation != null) {
            stmt.bindString(5, relation);
        }
 
        String sex = entity.getSex();
        if (sex != null) {
            stmt.bindString(6, sex);
        }
 
        String u_id = entity.getU_id();
        if (u_id != null) {
            stmt.bindString(7, u_id);
        }
 
        String address = entity.getAddress();
        if (address != null) {
            stmt.bindString(8, address);
        }
 
        String work_unit = entity.getWork_unit();
        if (work_unit != null) {
            stmt.bindString(9, work_unit);
        }
 
        String family_tel = entity.getFamily_tel();
        if (family_tel != null) {
            stmt.bindString(10, family_tel);
        }
 
        String email = entity.getEmail();
        if (email != null) {
            stmt.bindString(11, email);
        }
 
        String api_id = entity.getApi_id();
        if (api_id != null) {
            stmt.bindString(12, api_id);
        }
 
        String security_key = entity.getSecurity_key();
        if (security_key != null) {
            stmt.bindString(13, security_key);
        }
    }

    @Override
    public Void readKey(Cursor cursor, int offset) {
        return null;
    }    

    @Override
    public BeanParent readEntity(Cursor cursor, int offset) {
        BeanParent entity = new BeanParent( //
            cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0), // login_name
            cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1), // sp_id
            cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2), // parent_name
            cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3), // parent_phone
            cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4), // relation
            cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5), // sex
            cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6), // u_id
            cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7), // address
            cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8), // work_unit
            cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9), // family_tel
            cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10), // email
            cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11), // api_id
            cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12) // security_key
        );
        return entity;
    }
     
    @Override
    public void readEntity(Cursor cursor, BeanParent entity, int offset) {
        entity.setLogin_name(cursor.isNull(offset + 0) ? null : cursor.getString(offset + 0));
        entity.setSp_id(cursor.isNull(offset + 1) ? null : cursor.getString(offset + 1));
        entity.setParent_name(cursor.isNull(offset + 2) ? null : cursor.getString(offset + 2));
        entity.setParent_phone(cursor.isNull(offset + 3) ? null : cursor.getString(offset + 3));
        entity.setRelation(cursor.isNull(offset + 4) ? null : cursor.getString(offset + 4));
        entity.setSex(cursor.isNull(offset + 5) ? null : cursor.getString(offset + 5));
        entity.setU_id(cursor.isNull(offset + 6) ? null : cursor.getString(offset + 6));
        entity.setAddress(cursor.isNull(offset + 7) ? null : cursor.getString(offset + 7));
        entity.setWork_unit(cursor.isNull(offset + 8) ? null : cursor.getString(offset + 8));
        entity.setFamily_tel(cursor.isNull(offset + 9) ? null : cursor.getString(offset + 9));
        entity.setEmail(cursor.isNull(offset + 10) ? null : cursor.getString(offset + 10));
        entity.setApi_id(cursor.isNull(offset + 11) ? null : cursor.getString(offset + 11));
        entity.setSecurity_key(cursor.isNull(offset + 12) ? null : cursor.getString(offset + 12));
     }
    
    @Override
    protected final Void updateKeyAfterInsert(BeanParent entity, long rowId) {
        // Unsupported or missing PK type
        return null;
    }
    
    @Override
    public Void getKey(BeanParent entity) {
        return null;
    }

    @Override
    protected final boolean isEntityUpdateable() {
        return true;
    }
    
}