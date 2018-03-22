package com.xptschool.parent.common;

/**
 * Created by dexing on 2017-11-8 0008.
 */

public enum UserType {
    VISITOR("0"),  //会员
    TEACHER("3"),  //老师
    PARENT("4"),   //家长
    COMPANY("9"),  //第三方代理公司
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

    public String getRoleName() {
        String roleName = "";
        if (text.equals(VISITOR.toString())) {
            roleName = "会员";
        } else if (text.equals(TEACHER.toString())) {
            roleName = "老师";
        } else if (text.equals(PARENT.toString())) {
            roleName = "家长";
        } else if (text.equals(COMPANY.toString())) {
            roleName = "第三方代理公司";
        } else if (text.equals(PROXY.toString())) {
            roleName = "代理商";
        } else if (text.equals(CITYPROXY.toString())) {
            roleName = "区县代理商";
        }
        return roleName;
    }

    public static UserType getUserTypeByStr(String str) {
        if (VISITOR.toString().equals(str)) {
            return VISITOR;
        } else if (TEACHER.toString().equals(str)) {
            return TEACHER;
        } else if (PARENT.toString().equals(str)) {
            return PARENT;
        } else if (COMPANY.toString().equals(str)) {
            return COMPANY;
        } else if (PROXY.toString().equals(str)) {
            return PROXY;
        } else if (CITYPROXY.toString().equals(str)) {
            return CITYPROXY;
        } else {
            return null;
        }
    }

}
