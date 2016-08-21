package com.codesky.radboss.core;

import android.content.ContentValues;
import android.os.Handler;
import android.os.Looper;
import android.os.Message;
import android.util.Log;

import java.util.List;

/**
 * Only manipulate byte[] object
 *
 * Created by xueqiulxq on 8/21/16.
 */
public class RadDispatcher implements Handler.Callback {

    private static final String TAG = "RadStorage";

    private static final String OPT_INSERT = "insert";
    private static final String OPT_DELETE = "delete";
    private static final String OPT_MODIFY = "modify";
    private static final String OPT_QUERY = "query";

    private static class SingleHolder {
        public static final RadDispatcher sInstance = new RadDispatcher();
    }

    public static RadDispatcher getInstance() {
        return SingleHolder.sInstance;
    }

    // SingleThread implementation
    private RadHandlerThread mRadThread;
    private Handler mRadHandler;
    private Handler mMainHandler;

    private RadDispatcher() {
        mRadThread = new RadHandlerThread();
        mRadThread.start();
        mRadHandler = new Handler(mRadThread.getLooper(), this);
        mMainHandler = new Handler(Looper.getMainLooper());
    }

    /**
     * Synchronous operation
     */
    public RadResult<ContentValues> insert(ContentValues kv) {
        Log.i(TAG, "insert single");
        RadResult<ContentValues> res = new RadResult<ContentValues>(true, OPT_INSERT);
        res.setData(RadStorageImpl.getInstance().insert(kv));
        return res;
    }

    public RadResult<List<ContentValues>> insert(List<ContentValues> kvs) {
        Log.i(TAG, "insert list");
        RadResult<List<ContentValues>> res = new RadResult<List<ContentValues>>(true, OPT_INSERT);
        res.setData(RadStorageImpl.getInstance().insert(kvs));
        return res;
    }

    public RadResult<List<ContentValues>> delete(ContentValues key) {
        Log.i(TAG, "delete single");
        RadResult<List<ContentValues>> res = new RadResult<List<ContentValues>>(true, OPT_DELETE);
        res.setData(RadStorageImpl.getInstance().delete(key));
        return res;
    }

    public RadResult<List<ContentValues>> delete(List<ContentValues> keys) {
        Log.i(TAG, "delete list");
        RadResult<List<ContentValues>> res = new RadResult<List<ContentValues>>(true, OPT_DELETE);
        res.setData(RadStorageImpl.getInstance().delete(keys));
        return res;
    }

    public RadResult<List<ContentValues>> modify(ContentValues kv) {
        Log.i(TAG, "modify single");
        RadResult<List<ContentValues>> res = new RadResult<List<ContentValues>>(true, OPT_MODIFY);
        res.setData(RadStorageImpl.getInstance().modify(kv));
        return res;
    }

    public RadResult<List<ContentValues>> modify(List<ContentValues> kvs) {
        Log.i(TAG, "modify list");
        RadResult<List<ContentValues>> res = new RadResult<List<ContentValues>>(true, OPT_MODIFY);
        res.setData(RadStorageImpl.getInstance().modify(kvs));
        return res;
    }

    public RadResult<List<ContentValues>> query(ContentValues key) {
        Log.i(TAG, "query single");
        RadResult<List<ContentValues>> res = new RadResult<List<ContentValues>>(true, OPT_QUERY);
        res.setData(RadStorageImpl.getInstance().query(key));
        return res;
    }

    public RadResult<List<ContentValues>> query(List<ContentValues> keys) {
        Log.i(TAG, "query list");
        RadResult<List<ContentValues>> res = new RadResult<List<ContentValues>>(true, OPT_QUERY);
        res.setData(RadStorageImpl.getInstance().query(keys));
        return res;
    }

    /**
     * Asynchronous operation
     */
    public void insertAsync(ContentValues kv, RadCallback<ContentValues> callback) {
        execCmd(ERadOpt.INSERT, kv, callback);
    }

    public void insertAsync(List<ContentValues> kvs, RadCallback<List<ContentValues>> callback) {
        execCmd(ERadOpt.INSERT, kvs, callback);
    }

    public void deleteAsync(ContentValues key, RadCallback<List<ContentValues>> callback) {
        execCmd(ERadOpt.DELETE, key, callback);
    }

    public void deleteAsync(List<ContentValues> keys, RadCallback<List<ContentValues>> callback) {
        execCmd(ERadOpt.DELETE, keys, callback);
    }

    public void modifyAsync(ContentValues kv, RadCallback<List<ContentValues>> callback) {
        execCmd(ERadOpt.MODIFY, kv, callback);
    }

    public void modifyAsync(List<ContentValues> kvs, RadCallback<List<ContentValues>> callback) {
        execCmd(ERadOpt.MODIFY, kvs, callback);
    }

    public void queryAsync(ContentValues key, RadCallback<List<ContentValues>> callback) {
        execCmd(ERadOpt.QUERY, key, callback);
    }

    public void queryAsync(List<ContentValues> keys, RadCallback<List<ContentValues>> callback) {
        execCmd(ERadOpt.QUERY, keys, callback);
    }

    public void execCmd(ERadOpt opt, Object operand, RadCallback callback) {
        Message msg = Message.obtain(mRadHandler);
        RadParams obj = new RadParams(opt);
        obj.operand = operand;
        obj.callback = callback;
        msg.obj = obj;
        msg.sendToTarget();
    }

    @Override
    public boolean handleMessage(Message message) {
        final RadParams rad = (RadParams) message.obj;
        if (rad == null || rad.operand == null) {
            Log.e(TAG, "Operands should not be null.");
            return false;
        }
        List<ContentValues> listParam = null;
        ContentValues singleParam = null;
        boolean isCollection;
        if (rad.operand instanceof List) {
            listParam = (List<ContentValues>) rad.operand;
            isCollection = true;
        } else {
            singleParam = (ContentValues) rad.operand;
            isCollection = false;
        }

        RadResult cmdRes = null;
        switch (rad.opcode) {
            case INSERT:
                if (isCollection) {
                    cmdRes = insert(listParam);
                } else {
                    cmdRes = insert(singleParam);
                }
                break;
            case DELETE:
                if (isCollection) {
                    cmdRes = delete(listParam);
                } else {
                    cmdRes = delete(singleParam);
                }
                break;
            case MODIFY:
                if (isCollection) {
                    cmdRes = modify(listParam);
                } else {
                    cmdRes = modify(singleParam);
                }
                break;
            case QUERY:
                if (isCollection) {
                    cmdRes = query(listParam);
                } else {
                    cmdRes = query(singleParam);
                }
                break;
            default:
                return false;
        }
        if (rad.callback != null) {
            final RadResult postRes = cmdRes;
            mMainHandler.post(new Runnable() {
                @Override
                public void run() {
                    rad.callback.onResult(postRes);
                }
            });
        }
        return true;
    }
}
