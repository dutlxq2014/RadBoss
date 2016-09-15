package com.codesky.radlib.support;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.io.Closeable;


/**
 *
 * Created by xueqiulxq on 9/15/16.
 */
public class RadDbUtil {

    private static final String TAG = RadDbUtil.class.getSimpleName();

    public static void closeQuietly(Cursor io) {
        if (io != null && !io.isClosed()) {
            try {
                io.close();
            } catch (Throwable e) {
                RadLog.e(TAG, "Close cursor exception: " + e.getMessage());
            }
        }
    }

    public static void closeQuietly(SQLiteDatabase db) {
        if (db != null) {
            try {
                db.close();
            } catch (Throwable e) {
                RadLog.e(TAG, "Close db exception: " + e.getMessage());
            }
        }
    }

    public static void closeQuietly(Closeable io) {
        if (io != null) {
            try {
                io.close();
            } catch (Throwable e) {
                RadLog.e(TAG, "Close io exception: " + e.getMessage());
            }
        }
    }
}
