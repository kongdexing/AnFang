package com.shuhai.anfang.model;

import com.shuhai.anfang.BuildConfig;

import org.greenrobot.greendao.DaoException;
import org.greenrobot.greendao.annotation.Entity;
import org.greenrobot.greendao.annotation.Generated;
import org.greenrobot.greendao.annotation.Id;
import org.greenrobot.greendao.annotation.Keep;
import org.greenrobot.greendao.annotation.ToMany;

import java.util.List;

/**
 * Created by dexing on 2017-11-14 0014.
 */
@Entity
public class BeanHomeCfg {

    @Id
    private String id;
    private String title;
    private String img;
    private String url;
    private String cell;
    private String pid;
    @ToMany(referencedJoinProperty = "pid")
    private List<BeanHomeCfgChild> child;
    /**
     * Used for active entity operations.
     */
    @Generated(hash = 1846598264)
    private transient BeanHomeCfgDao myDao;
    /**
     * Used to resolve relations
     */
    @Generated(hash = 2040040024)
    private transient DaoSession daoSession;

    @Generated(hash = 927340585)
    public BeanHomeCfg(String id, String title, String img, String url,
                       String cell, String pid) {
        this.id = id;
        this.title = title;
        this.img = img;
        this.url = url;
        this.cell = cell;
        this.pid = pid;
    }

    @Generated(hash = 1285855885)
    public BeanHomeCfg() {
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getImg() {
        if (!img.startsWith(BuildConfig.SERVICE_URL)) {
            img = BuildConfig.SERVICE_URL + img;
        }
        return img;
    }

    public void setImg(String img) {
        this.img = img;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getCell() {
        return cell;
    }

    public void setCell(String cell) {
        this.cell = cell;
    }

    public String getPid() {
        return pid;
    }

    public void setPid(String pid) {
        this.pid = pid;
    }

    @Keep
    public List<BeanHomeCfgChild> getChild() {
        return child;
    }

    public void setChild(List<BeanHomeCfgChild> child) {
        this.child = child;
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#refresh(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 1942392019)
    public void refresh() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.refresh(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#update(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 713229351)
    public void update() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.update(this);
    }

    /**
     * Convenient call for {@link org.greenrobot.greendao.AbstractDao#delete(Object)}.
     * Entity must attached to an entity context.
     */
    @Generated(hash = 128553479)
    public void delete() {
        if (myDao == null) {
            throw new DaoException("Entity is detached from DAO context");
        }
        myDao.delete(this);
    }

    /**
     * Resets a to-many relationship, making the next get call to query for a fresh result.
     */
    @Generated(hash = 1024800465)
    public synchronized void resetChild() {
        child = null;
    }

    /**
     * called by internal mechanisms, do not call yourself.
     */
    @Generated(hash = 1016321648)
    public void __setDaoSession(DaoSession daoSession) {
        this.daoSession = daoSession;
        myDao = daoSession != null ? daoSession.getBeanHomeCfgDao() : null;
    }
}
