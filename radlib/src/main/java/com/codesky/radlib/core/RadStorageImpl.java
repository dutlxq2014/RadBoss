package com.codesky.radlib.core;

import android.content.ContentValues;
import android.database.Cursor;


import com.codesky.radlib.RadBoss;
import com.codesky.radlib.support.ERadTab;
import com.codesky.radlib.support.RadDBConst;
import com.codesky.radlib.support.RadDbUtil;
import com.codesky.radlib.support.RadLog;

import java.util.ArrayList;
import java.util.List;

/**
 * database object {@link android.content.ContentValues}
 *
 * Created by xueqiulxq on 9/15/16.
 */
class RadStorageImpl implements IRadStorage {

    private static final String TAG = RadStorageImpl.class.getSimpleName();

    private RadDbHelper dbHelper;
    private static final String WHERE_KEY_CLAUSE = RadDBConst.RECORD_KEY + "=?";
    private static final String WHERE_ID_CLAUSE = RadDBConst.RECORD_ID + "=?";
    private static final String WHERE_KEY_ID_CLAUSE = WHERE_KEY_CLAUSE + " and " + WHERE_ID_CLAUSE;
    private static final String WHERE_ID_LITTLE_CLAUSE = RadDBConst.RECORD_ID + "<?";
    private static final String ALL_RECORD = "*";
    private static final String LIMIT_PAGE = "80";          // 每页的限制
    private static final String LIMIT_TOTAL = LIMIT_PAGE;   // DB总数的显示

    private static class SingletonHolder {
        public static final RadStorageImpl sInstance = new RadStorageImpl();
    }

    private RadStorageImpl() {
        dbHelper = new RadDbHelper(RadBoss.getAppContext());
    }

//    public static IRadStorage getInstance() {
    public static RadStorageImpl getInstance() {
        return SingletonHolder.sInstance;
    }

    private void ensureTableExist(ERadTab table) {
        dbHelper.ensureTableExists(table);
    }

    private void pruneDbSize(ERadTab module) {
        Cursor cursor = null;
        try {
            cursor = dbHelper.getReadableDatabase().query(module.table(), new String[]{RadDBConst.RECORD_ID},
                    null, null, null, null, null, null);
            long firstId = -1;
            long lastId = -1;
            if (cursor.moveToFirst()) {
                firstId = cursor.getLong(cursor.getColumnIndex(RadDBConst.RECORD_ID));
            }
            if (cursor.moveToLast()) {
                lastId = cursor.getLong(cursor.getColumnIndex(RadDBConst.RECORD_ID));
            }
            long MAX_LIMIT = Long.parseLong(LIMIT_TOTAL);
            if (firstId >= 0 && lastId - firstId > MAX_LIMIT) {
                // 适当多裁剪15%,免得每次都要裁
                long targePrune = (lastId - firstId - MAX_LIMIT) + firstId + (long)(MAX_LIMIT * 0.15);
                int realPrune = dbHelper.getWritableDatabase().delete(module.table(),
                        WHERE_ID_LITTLE_CLAUSE, new String[]{"" + targePrune});
                RadLog.i(TAG, "pruneDbSize ok pruned size=" + realPrune);
            } else {
                RadLog.i(TAG, "pruneDbSize conditions not satisfied (firstId,lastId)=("
                        + firstId + "," + lastId + ")");
            }
        } catch (Exception e) {
            RadLog.e(TAG, "pruneDbSize exception: " + e.getMessage());
        } finally {
            RadDbUtil.closeQuietly(cursor);
        }
    }

    /**
     * 1. insert 插入成功返回插入的记录,包含数据库中新增ID
     * @return True for success or false
     */
    public boolean insert(ERadTab module, ContentValues record, List<ContentValues> retVal) {
        try {
            ensureTableExist(module);
            long id = dbHelper.getWritableDatabase().insert(module.table(), null, record);
            record.put(RadDBConst.RECORD_ID, id);
            if (id > 0) {
                retVal.add(record);
                return true;
            }
        } catch (Exception e) {
            RadLog.e(TAG, "Insert exception: " + e.getMessage());
        }
        return false;
    }

    /**
     * 1. insert 插入成功返回插入的记录,包含数据库中新增ID
     * @return True for success or false
     */
    public boolean insert(ERadTab module, List<ContentValues> records, List<ContentValues> retVal) {
        boolean retCode = false;
        List<ContentValues> retBatch = new ArrayList<ContentValues>();
        for (int i=0; i<records.size(); ++i) {
            retBatch.clear();
            boolean succ = insert(module, records.get(i), retBatch);
            if (succ) {
                retVal.addAll(retBatch);
                retCode = true;
            }
        }
        return retCode;
    }

    /**
     * 2. delete 删除成功返回被删除的记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean delete(ERadTab module, ContentValues key, List<ContentValues> retVal) {
        try {
            String wk = key.getAsString(RadDBConst.RECORD_KEY);
            List<ContentValues> toDel = new ArrayList<ContentValues>();
            boolean succ = query(module, key, toDel);
            if (succ && toDel.size() > 0) {
                int ret = -1;
                if (ALL_RECORD.equals(wk)) {
                    ret = dbHelper.getWritableDatabase().delete(module.table(), null, null);
                } else {
                    ret = dbHelper.getWritableDatabase().delete(module.table(), WHERE_KEY_CLAUSE, new String[]{wk});
                }
                if (ret > 0) {
                    retVal.addAll(toDel);
                }
            }
            return succ;
        } catch (Exception e) {
            RadLog.e(TAG, "Delete exception: " + e.getMessage());
        }
        return false;
    }

    /**
     * 2. delete 删除成功返回被删除的记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean delete(ERadTab module, List<ContentValues> keys, List<ContentValues> retVal) {
        boolean retCode = false;
        List<ContentValues> retBatch = new ArrayList<ContentValues>();
        for (int i=0; i<keys.size(); ++i) {
            retBatch.clear();
            boolean succ = delete(module, keys.get(i), retBatch);
            if (succ) {
                retCode = true;
                retVal.addAll(retBatch);
            }
        }
        return retCode;
    }

    /**
     * 2. delete 删除成功返回被删除的记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean deleteById(ERadTab module, ContentValues key, List<ContentValues> retVal) {
        try {
            ensureTableExist(module);
            Long id = key.getAsLong(RadDBConst.RECORD_ID);
            if (id != null && id > 0) {
                List<ContentValues> toDelete = new ArrayList<ContentValues>();
                boolean succ = queryById(module, key, toDelete);
                if (succ && toDelete.size() > 0) {
                    int ret = dbHelper.getWritableDatabase().delete(module.table(), WHERE_ID_CLAUSE, new String[]{id.toString()});
                    if (ret > 0) {
                        retVal.addAll(toDelete);
                        return true;
                    }
                } else if (succ) {
                    return true;
                }
            }
        } catch (Exception e) {
            RadLog.e(TAG, "deleteById exception: " + e.getMessage());
        }
        return false;
    }

    /**
     * 2. delete 删除成功返回被删除的记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean deleteById(ERadTab module, List<ContentValues> keys, List<ContentValues> retVal) {
        boolean retCode = false;
        List<ContentValues> retBatch = new ArrayList<ContentValues>();
        for (int i=0; i<keys.size(); ++i) {
            retBatch.clear();
            boolean succ = deleteById(module, keys.get(i), retBatch);
            if (succ) {
                retCode = true;
                retVal.addAll(retBatch);
            }
        }
        return retCode;
    }

    /**
     * 3. modify 修改成功返回被修改的旧记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean modify(ERadTab module, ContentValues record, List<ContentValues> retVal) {
        try {
            String wk = record.getAsString(RadDBConst.RECORD_KEY);
            List<ContentValues> toModify = new ArrayList<ContentValues>();
            boolean succ = query(module, record, toModify);
            if (succ && toModify.size() > 0) {
                int ret = dbHelper.getWritableDatabase().update(module.table(), record, WHERE_KEY_CLAUSE, new String[]{wk});
                if (ret > 0) {
                    retVal.addAll(toModify);
                    return true;
                }
            } else if (succ) {
                boolean isucc = insert(module, record, new ArrayList<ContentValues>());
                if (isucc) {
                    return true;
                }
            }
        } catch (Exception e) {
            RadLog.e(TAG, "Modify exception: " + e.getMessage());
        }
        return false;
    }

    /**
     * 3. modify 修改成功返回被修改的旧记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean modify(ERadTab module, List<ContentValues> records, List<ContentValues> retVal) {
        boolean retCode = false;
        List<ContentValues> retBatch = new ArrayList<ContentValues>();
        for (int i=0; i<records.size(); ++i) {
            retBatch.clear();
            boolean succ = modify(module, records.get(i), retBatch);
            if (succ) {
                retCode = true;
                retVal.addAll(retBatch);
            }
        }
        return retCode;
    }

    /**
     * 3. modify 根据ID修改对应的记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean modifyById(ERadTab module, ContentValues record, List<ContentValues> retVal) {
        try {
            Long id = record.getAsLong(RadDBConst.RECORD_ID);
            if (id != null && id > 0) {
                List<ContentValues> toModify = new ArrayList<ContentValues>();
                boolean succ = queryById(module, record, toModify);
                if (succ && toModify.size() > 0) {
                    int ret = dbHelper.getWritableDatabase().update(module.table(), record,
                            WHERE_ID_CLAUSE, new String[]{id.toString()});
                    if (ret > 0) {
                        retVal.addAll(toModify);
                        return true;
                    }
                }
            }
        } catch (Exception e) {
            RadLog.e(TAG, "modifyById exception: " + e.getMessage());
        }
        return false;
    }

    /**
     * 3. modify 根据ID修改对应的记录,没有记录或者失败返回空数组
     * @return True for success or false
     */
    public boolean modifyById(ERadTab module, List<ContentValues> records, List<ContentValues> retVal) {
        boolean retCode = false;
        List<ContentValues> retBatch = new ArrayList<ContentValues>();
        for (int i=0; i<records.size(); ++i) {
            retBatch.clear();
            boolean succ = modifyById(module, records.get(i), retBatch);
            if (succ) {
                retCode = true;
                retVal.addAll(retBatch);
            }
        }
        return retCode;
    }


    /**
     * 4. query 查询到的记录,没有记录返回空数组
     * @return True for success or false
     */
    public boolean query(ERadTab module, ContentValues key, List<ContentValues> retVal) {
        Cursor cursor = null;
        try {
            ensureTableExist(module);
            String wk = key.getAsString(RadDBConst.RECORD_KEY);
            if (ALL_RECORD.equals(wk)) {
                pruneDbSize(module);
                cursor = dbHelper.getReadableDatabase().query(module.table(), null, null, null, null, null, null, LIMIT_PAGE);
            } else {
                cursor = dbHelper.getReadableDatabase().query(
                        module.table(), null, WHERE_KEY_CLAUSE, new String[]{wk}, null, null, null, LIMIT_PAGE);
            }
            while (cursor.moveToNext()) {
                ContentValues v = new ContentValues();
                v.put(RadDBConst.RECORD_ID, cursor.getString(cursor.getColumnIndex(RadDBConst.RECORD_ID)));
                v.put(RadDBConst.RECORD_KEY, cursor.getString(cursor.getColumnIndex(RadDBConst.RECORD_KEY)));
                v.put(RadDBConst.RECORD_DATA, cursor.getBlob(cursor.getColumnIndex(RadDBConst.RECORD_DATA)));
                v.put(RadDBConst.RECORD_SIZE, cursor.getLong(cursor.getColumnIndex(RadDBConst.RECORD_SIZE)));
                v.put(RadDBConst.RECORD_DS, cursor.getString(cursor.getColumnIndex(RadDBConst.RECORD_DS)));
                retVal.add(v);
            }
            return true;
        } catch (Exception e) {
            RadLog.e(TAG, "Query exception: " + e.getMessage());
        } finally {
            RadDbUtil.closeQuietly(cursor);
        }
        return false;
    }

    /**
     * 4. query 查询到的记录,没有记录返回空数组
     * @return True for success or false
     */
    public boolean query(ERadTab module, List<ContentValues> keys, List<ContentValues> retVal) {
        boolean retCode = false;
        List<ContentValues> retBatch = new ArrayList<ContentValues>();
        for (int i=0; i<keys.size(); ++i) {
            retBatch.clear();
            boolean succ = query(module, keys.get(i), retBatch);
            if (succ) {
                retCode = true;
                retVal.addAll(retBatch);
            }
        }
        return retCode;
    }

    /**
     * 4. query 根据ID查询记录,没有记录返回空数组
     * @return True for success or false
     */
    public boolean queryById(ERadTab module, ContentValues key, List<ContentValues> retVal) {
        Cursor cursor = null;
        try {
            ensureTableExist(module);
            Long id = key.getAsLong(RadDBConst.RECORD_ID);
            if (id != null && id > 0) {
                cursor = dbHelper.getReadableDatabase().query(module.table(), null, WHERE_ID_CLAUSE,
                        new String[]{id.toString()}, null, null, null, LIMIT_PAGE);
                while (cursor.moveToNext()) {
                    ContentValues v = new ContentValues();
                    v.put(RadDBConst.RECORD_ID, cursor.getString(cursor.getColumnIndex(RadDBConst.RECORD_ID)));
                    v.put(RadDBConst.RECORD_KEY, cursor.getString(cursor.getColumnIndex(RadDBConst.RECORD_KEY)));
                    v.put(RadDBConst.RECORD_DATA, cursor.getBlob(cursor.getColumnIndex(RadDBConst.RECORD_DATA)));
                    v.put(RadDBConst.RECORD_SIZE, cursor.getLong(cursor.getColumnIndex(RadDBConst.RECORD_SIZE)));
                    v.put(RadDBConst.RECORD_DS, cursor.getString(cursor.getColumnIndex(RadDBConst.RECORD_DS)));
                    retVal.add(v);
                }
                return true;
            }
        } catch (Exception e) {
            RadLog.e(TAG, "queryById exception: " + e.getMessage());
        } finally {
            RadDbUtil.closeQuietly(cursor);
        }
        return false;
    }

    /**
     * 4. query 根据ID查询记录,没有记录返回空数组
     * @return True for success or false
     */
    public boolean queryById(ERadTab module, List<ContentValues> keys, List<ContentValues> retVal) {
        boolean retCode = false;
        List<ContentValues> retBatch = new ArrayList<ContentValues>();
        for (int i=0; i<keys.size(); ++i) {
            retBatch.clear();
            boolean succ = queryById(module, keys.get(i), retBatch);
            if (succ) {
                retCode = true;
                retVal.addAll(retBatch);
            }
        }
        return retCode;
    }
}
