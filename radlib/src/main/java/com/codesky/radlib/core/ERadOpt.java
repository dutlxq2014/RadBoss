package com.codesky.radlib.core;

/**
 *
 * Created by xueqiulxq on 9/15/16.
 */
enum ERadOpt {

    INSERT("insert"),
    DELETE("delete"),
    MODIFY("modify"),
    QUERY("query"),
    DELETE_BYID("delete_byid"),
    MODIFY_BYID("modify_byid"),
    QUERY_BYID("query_byid");

    private String mOptName;

    ERadOpt(String optName) {
        mOptName = optName;
    }

    @Override
    public String toString() {
        return super.toString();
    }
}
