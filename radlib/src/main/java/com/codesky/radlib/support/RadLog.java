package com.codesky.radlib.support;

import android.util.Log;

/**
 *
 * Created by xueqiulxq on 9/15/16.
 */
public class RadLog {

    private static final String prefix = "*rad* ";

    public static void d(String tag, String msg, Object... kv) {
        Log.d(prefix + tag, msg);
    }

    public static void i(String tag, String msg, Object... kv) {
        Log.i(prefix + tag, msg);
    }

    public static void w(String tag, String msg, Object... kv) {
        Log.w(prefix + tag, msg);
    }

    public static void e(String tag, String msg, Object... kv) {
        Log.e(prefix + tag, msg);
    }

}
