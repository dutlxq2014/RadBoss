package com.codesky.radlib.core;

import android.content.ContentValues;
import android.os.Handler;
import android.os.HandlerThread;
import android.os.Looper;
import android.os.Message;


import com.codesky.radlib.support.ERadTab;
import com.codesky.radlib.support.RadDBConst;
import com.codesky.radlib.support.RadLog;
import com.codesky.radlib.support.RadRecord;
import com.codesky.radlib.support.RadResult;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Map program object {@link RadRecord} to database record {@link android.content.ContentValues}.
 * Every operation will be moved to the worker thread. Callback will be invoked after operation done.
 *
 * Created by xueqiulxq on 9/15/16.
 */
public class RadDao implements IRadDao, Handler.Callback {

    private static final String TAG = RadDao.class.getSimpleName();

    private HandlerThread mThread;
    private Handler dispHandler;
    private Handler mainHandler;
    private RadStorageImpl mStorage;

    private static class SingletonHolder {
        public static final RadDao sInstance = new RadDao();
    }

    private RadDao() {
        mStorage = RadStorageImpl.getInstance();
        mThread = new HandlerThread("Thread-rad.db");
        mThread.start();
        dispHandler = new Handler(mThread.getLooper(), this);
        mainHandler = new Handler(Looper.getMainLooper());
    }

    public static IRadDao getInstance() {
        return SingletonHolder.sInstance;
    }

    /**
     * 1. Insert @hide
     * @return Records successfully inserted.
     */
    private RadResult<List<RadRecord>> insert(ERadTab module, RadRecord record) {
        RadLog.i(TAG, "insert single key=" + record.key);
        ContentValues target = toRecord(record);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.insert(module, target, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.INSERT.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 1. Insert @hide
     * @return Records successfully inserted.
     */
    private RadResult<List<RadRecord>> insert(ERadTab module, List<RadRecord> records) {
        RadLog.i(TAG, "insert list");
        List<ContentValues> targets = toRecord(records);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.insert(module, targets, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.INSERT.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 2. Delete if {@link RadDBConst#FIELD_ID} matches.
     * @return Records been deleted.
     */
    private RadResult<List<RadRecord>> delete(ERadTab module, RadRecord key) {
        RadLog.i(TAG, "delete by key=" + key.key);
        ContentValues target = toRecord(key);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.delete(module, target, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.DELETE.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 2. Delete if {@link RadDBConst#FIELD_ID} matches.
     * @return Records been deleted.
     */
    private RadResult<List<RadRecord>> delete(ERadTab module, List<RadRecord> keys) {
        RadLog.i(TAG, "delete list");
        List<ContentValues> targets = toRecord(keys);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.delete(module, targets, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.DELETE.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 2. Delete if {@link RadDBConst#FIELD_ID} matches.
     * @return Records been deleted.
     */
    private RadResult<List<RadRecord>> deleteById(ERadTab module, RadRecord key) {
        RadLog.i(TAG, "delete by id=" + key.id);
        ContentValues target = toRecord(key);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.deleteById(module, target, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.DELETE_BYID.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 2. Delete if {@link RadDBConst#FIELD_ID} matches.
     * @return Records been deleted.
     */
    private RadResult<List<RadRecord>> deleteById(ERadTab module, List<RadRecord> keys) {
        RadLog.i(TAG, "delete by id list");
        List<ContentValues> targets = toRecord(keys);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.deleteById(module, targets, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.DELETE_BYID.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 3. Modify if {@link RadDBConst#FIELD_ID} matches.
     * @return Old records been modified.
     */
    private RadResult<List<RadRecord>> modify(ERadTab module, RadRecord value) {
        RadLog.i(TAG, "modify by key=" + value.key);
        ContentValues target = toRecord(value);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.modify(module, target, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.MODIFY.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 3. Modify if {@link RadDBConst#FIELD_ID} matches.
     * @return Old records been modified.
     */
    private RadResult<List<RadRecord>> modify(ERadTab module, List<RadRecord> values) {
        RadLog.i(TAG, "modify list");
        List<ContentValues> targets = toRecord(values);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.modify(module, targets, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.MODIFY.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 3. Modify if {@link RadDBConst#FIELD_ID} matches.
     * @return Old records been modified.
     */
    private RadResult<List<RadRecord>> modifyById(ERadTab module, RadRecord value) {
        RadLog.i(TAG, "modify by id=" + value.id);
        ContentValues target = toRecord(value);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.modifyById(module, target, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.MODIFY_BYID.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 3. Modify if {@link RadDBConst#FIELD_ID} matches.
     * @return Old records been modified.
     */
    private RadResult<List<RadRecord>> modifyById(ERadTab module, List<RadRecord> values) {
        RadLog.i(TAG, "modify by id list");
        List<ContentValues> targets = toRecord(values);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.modifyById(module, targets, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.MODIFY_BYID.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 4. Query that {@link RadDBConst#FIELD_KEY} matches.
     * @return Records meet query condition.
     */
    private RadResult<List<RadRecord>> query(ERadTab module, RadRecord key) {
        RadLog.i(TAG, "query by key=" + key.key);
        ContentValues target = toRecord(key);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.query(module, target, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.QUERY.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 4. Query that {@link RadDBConst#FIELD_KEY} matches.
     * @return Records meet query condition.
     */
    private RadResult<List<RadRecord>> query(ERadTab module, List<RadRecord> keys) {
        RadLog.i(TAG, "query list");
        List<ContentValues> targets = toRecord(keys);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.query(module, targets, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.QUERY.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 4. Query that {@link RadDBConst#FIELD_ID} matches.
     * @return Records meet query condition.
     */
    private RadResult<List<RadRecord>> queryById(ERadTab module, RadRecord key) {
        RadLog.i(TAG, "query by id=" + key.id);
        ContentValues target = toRecord(key);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.queryById(module, target, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.QUERY_BYID.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    /**
     * 4. Query that {@link RadDBConst#FIELD_ID} matches.
     * @return Records meet query condition.
     */
    private RadResult<List<RadRecord>> queryById(ERadTab module, List<RadRecord> keys) {
        RadLog.i(TAG, "query by id list");
        List<ContentValues> targets = toRecord(keys);
        List<ContentValues> retVal = new ArrayList<ContentValues>();
        boolean succ = mStorage.queryById(module, targets, retVal);
        RadResult<List<RadRecord>> result = new RadResult<List<RadRecord>>(succ, ERadOpt.QUERY_BYID.toString());
        result.setData(fromRecord(retVal));
        return result;
    }

    public void insertAsync(ERadTab module, RadRecord record, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.INSERT, module, record, callback);
    }

    public void insertAsync(ERadTab module, List<RadRecord> records, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.INSERT, module, records, callback);
    }

    public void deleteAsync(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.DELETE, module, key, callback);
    }

    public void deleteAsync(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.DELETE, module, keys, callback);
    }

    public void deleteAsyncById(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.DELETE_BYID, module, key, callback);
    }

    public void deleteAsyncById(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.DELETE_BYID, module, keys, callback);
    }

    public void modifyAsync(ERadTab module, RadRecord record, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.MODIFY, module, record, callback);
    }

    public void modifyAsync(ERadTab module, List<RadRecord> records, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.MODIFY, module, records, callback);
    }

    public void modifyAsyncById(ERadTab module, RadRecord record, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.MODIFY_BYID, module, record, callback);
    }

    public void modifyAsyncById(ERadTab module, List<RadRecord> records, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.MODIFY_BYID, module, records, callback);
    }

    public void queryAsync(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.QUERY, module, key, callback);
    }

    public void queryAsync(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.QUERY, module, keys, callback);
    }

    public void queryAsyncById(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.QUERY_BYID, module, key, callback);
    }

    public void queryAsyncById(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback) {
        execCmd(ERadOpt.QUERY_BYID, module, keys, callback);
    }

    private void execCmd(ERadOpt opt, ERadTab module, Object operand, IRadCallback callback) {
        Message msg = Message.obtain(dispHandler);
        OptWrapper obj = new OptWrapper(opt);
        obj.operand = operand;
        obj.module = module;
        obj.callback = callback;
        msg.obj = obj;
        msg.sendToTarget();
    }

    private ContentValues toRecord(RadRecord data) {
        return data.toKv();
    }

    private List<ContentValues> toRecord(List<RadRecord> data) {
        List<ContentValues> ret = new ArrayList<ContentValues>();
        for (int i=0; i<data.size(); ++i) {
            ret.add(data.get(i).toKv());
        }
        return ret;
    }

    private RadRecord fromRecord(ContentValues data) {
        return RadRecord.fromKv(data);
    }

    private List<RadRecord> fromRecord(List<ContentValues> data) {
        List<RadRecord> ret = new ArrayList<RadRecord>();
        for (int i=0; i<data.size(); ++i) {
            ret.add(RadRecord.fromKv(data.get(i)));
        }
        return ret;
    }

    @Override
    public boolean handleMessage(Message msg) {

        final OptWrapper opt = (OptWrapper) msg.obj;
        if (opt == null || opt.operand == null) {
            RadLog.e(TAG, "Operands should not be null.");
            return false;
        }
        List<RadRecord> listParam = null;
        RadRecord singleParam = null;
        boolean isCollection;
        if (opt.operand instanceof List) {
            listParam = (List<RadRecord>) opt.operand;
            isCollection = true;
        } else {
            singleParam = (RadRecord) opt.operand;
            isCollection = false;
        }
        ERadTab module = opt.module;

        RadResult cmdRes;
        switch (opt.opcode) {
            case INSERT:
                if (isCollection) {
                    cmdRes = insert(module, listParam);
                } else {
                    cmdRes = insert(module, singleParam);
                }
                break;
            case DELETE:
                if (isCollection) {
                    cmdRes = delete(module,listParam);
                } else {
                    cmdRes = delete(module, singleParam);
                }
                break;
            case MODIFY:
                if (isCollection) {
                    cmdRes = modify(module, listParam);
                } else {
                    cmdRes = modify(module, singleParam);
                }
                break;
            case QUERY:
                if (isCollection) {
                    cmdRes = query(module, listParam);
                } else {
                    cmdRes = query(module, singleParam);
                }
                break;
            case DELETE_BYID:
                if (isCollection) {
                    cmdRes = deleteById(module, listParam);
                } else {
                    cmdRes = deleteById(module, singleParam);
                }
                break;
            case MODIFY_BYID:
                if (isCollection) {
                    cmdRes = modifyById(module, listParam);
                } else {
                    cmdRes = modifyById(module, singleParam);
                }
                break;
            case QUERY_BYID:
                if (isCollection) {
                    cmdRes = queryById(module, listParam);
                } else {
                    cmdRes = queryById(module, singleParam);
                }
                break;
            default:
                return false;
        }
        if (opt.callback != null) {
            final RadResult postRes = cmdRes;
            mainHandler.post(new Runnable() {
                @Override
                public void run() {
                    opt.callback.onResult(postRes);
                }
            });
        }
        return true;
    }

    private static class OptWrapper {

        public ERadTab module;
        public Object operand;
        public ERadOpt opcode;
        public IRadCallback callback;

        public OptWrapper(ERadOpt opt) {
            opcode = opt;
        }
    }
}
