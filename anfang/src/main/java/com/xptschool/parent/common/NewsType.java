package com.xptschool.parent.common;

/**
 * Created by dexing on 2017-11-8 0008.
 */

public enum NewsType {
    SYSTEM_NEWS("1"),  //1.系统消息
    NOTICE("2"),  //2.公告
    LETTER("3"),   //3.私信
    SHUHAI_NOTICE("4"),  //4.数海公告
    RECOMMEND("5"),   //5.新品推荐
    RICH_NEWS("6"); //6.致富财经

    private final String text;

    private NewsType(final String text) {
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
