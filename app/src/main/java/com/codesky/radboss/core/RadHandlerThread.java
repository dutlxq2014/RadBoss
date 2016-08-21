package com.codesky.radboss.core;

import android.os.Handler;
import android.os.HandlerThread;
import android.os.Message;

/**
 *
 * Created by xueqiulxq on 8/21/16.
 */
public class RadHandlerThread extends HandlerThread implements Handler.Callback {

    private Handler mHandler;

    public RadHandlerThread() {
        this("Thread-RadStore");
    }

    public RadHandlerThread(String name) {
        super(name);
    }

    public RadHandlerThread(String name, int priority) {
        super(name, priority);
    }

    public Handler getHandler() {
        if (mHandler == null) {
            mHandler = new Handler(this.getLooper(), this);
        }
        return mHandler;
    }

    @Override
    public boolean handleMessage(Message message) {
        return false;
    }
}
