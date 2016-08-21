package com.codesky.radboss.core;

/**
 * Enumeration of operation
 *
 * Created by xueqiulxq on 8/21/16.
 */
public enum  ERadOpt {

    INSERT("insert"),
    DELETE("delete"),
    MODIFY("modify"),
    QUERY("query");

    private String mOptName;

    ERadOpt(String optName) {
        mOptName = optName;
    }

    @Override
    public String toString() {
        return super.toString() + mOptName;
    }
}
