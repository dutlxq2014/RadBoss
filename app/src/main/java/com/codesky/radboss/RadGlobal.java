package com.codesky.radboss;

import android.app.Application;

/**
 *
 * Created by xueqiulxq on 9/16/16.
 */
public class RadGlobal {

    private static Application sAppContext;

    public static void setApplication(Application app) {
        sAppContext = app;
    }

    public static Application getAppContext() {
        return sAppContext;
    }
}
