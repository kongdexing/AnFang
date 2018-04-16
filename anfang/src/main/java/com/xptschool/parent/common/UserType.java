package com.xptschool.parent.common;

/**
 * Created by dexing on 2017-11-8 0008.
 */

public enum UserType {
    VISITOR("0"),  //会员
    SCH_ADMIN("1"), //学校管理员
    SYS_USER("2"),  //系统用户
    TEACHER("3"),  //老师
    PARENT("4"),   //家长
    PROVINCE_ADMIN("5"),  //省委
    CITY_ADMIN("6"),//市委
    COUNTY_ADMIN("7"), //县委
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
        } else if (text.equals(SCH_ADMIN.toString())) {
            roleName = "学校管理员";
        } else if (text.equals(SYS_USER.toString())) {
            roleName = "系统用户";
        } else if (text.equals(TEACHER.toString())) {
            roleName = "老师";
        } else if (text.equals(PARENT.toString())) {
            roleName = "家长";
        } else if (text.equals(PROVINCE_ADMIN.toString())) {
            roleName = "省委";
        } else if (text.equals(CITY_ADMIN.toString())) {
            roleName = "市委";
        } else if (text.equals(COUNTY_ADMIN.toString())) {
            roleName = "县委";
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
        } else if (SCH_ADMIN.toString().equals(str)) {
            return SCH_ADMIN;
        } else if (SYS_USER.toString().equals(str)) {
            return SYS_USER;
        } else if (TEACHER.toString().equals(str)) {
            return TEACHER;
        } else if (PARENT.toString().equals(str)) {
            return PARENT;
        } else if (PROVINCE_ADMIN.toString().equals(str)) {
            return PROVINCE_ADMIN;
        } else if (CITY_ADMIN.toString().equals(str)) {
            return CITY_ADMIN;
        } else if (COUNTY_ADMIN.toString().equals(str)) {
            return COUNTY_ADMIN;
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
