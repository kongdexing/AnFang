package com.xptschool.parent.common;

/**
 * Created by dexing on 2017-11-8 0008.
 */

public enum UserType {
    VISITOR("0"),  //会员
    TEACHER("3"),  //老师
    PARENT("4"),   //家长
    COMPANY("9"),  //第三方公司
    PROXY("10"),   //代理商
    CITYPROXY("11"); //区县代理商

    private final String text;

    private UserType(final String text) {
        this.text = text;
    }

    /* (non-Javadoc)
     * @see java.lang.Enum#toString()
     */
    @Override
    public String toString() {
        return text;
    }
}
