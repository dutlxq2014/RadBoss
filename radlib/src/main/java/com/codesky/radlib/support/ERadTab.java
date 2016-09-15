package com.codesky.radlib.support;

/**
 *
 * Created by xueqiulxq on 9/15/16.
 */
public enum ERadTab {

    MAIN("main_table"),
    BUFF("buff_table");

    private String table = "default";
    ERadTab(String table) {
        this.table = table;
    }

    @Override
    public String toString() {
        return table;
    }

    public String table() {
        return table;
    }
}
