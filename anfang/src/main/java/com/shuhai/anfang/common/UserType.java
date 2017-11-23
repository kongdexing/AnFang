package com.shuhai.anfang.common;

/**
 * Created by dexing on 2017-11-8 0008.
 */

public enum UserType {
    TEACHER("3"),
    PARENT("4");

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