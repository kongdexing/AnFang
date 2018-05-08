package com.xptschool.parent.common;

public class UpgradeHelper {

    public static UpgradeHelper instance = null;

    public static synchronized UpgradeHelper getInstance() {
        if (instance == null) {
            synchronized (UpgradeHelper.class) {
                if (instance == null) {
                    instance = new UpgradeHelper();
                }
            }
        }
        return instance;
    }

    public void checkUpgrade(){


    }


    public interface UpgradeListener {
        void onUpgrade(String data);
    }

}
