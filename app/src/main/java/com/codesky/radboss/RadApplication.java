package com.codesky.radboss;

import android.app.Application;

import com.codesky.radlib.RadBoss;

/**
 *
 * Created by xueqiulxq on 9/16/16.
 */
public class RadApplication extends Application {

    @Override
    public void onCreate() {
        super.onCreate();
        RadGlobal.setApplication(this);
        RadBoss.init(this);
    }
}
