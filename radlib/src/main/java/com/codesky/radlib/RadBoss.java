package com.codesky.radlib;

import android.app.Application;
import android.content.Context;

/**
 * Boss needs an application context reference.
 *
 * Created by xueqiulxq on 9/15/16.
 */
public class RadBoss {

    private static Context sContext;

    public static void init(Context context) {
        if (context == null) {
            throw new RuntimeException("init content cannot be null!");
        }
        if (context instanceof Application) {
            sContext = context;
        } else {
            sContext = context.getApplicationContext();
        }
    }

    public static Context getAppContext() {
        return sContext;
    }
}
