package com.codesky.radlib.support;

import android.content.ContentValues;
import android.text.TextUtils;

/**
 * 业务对象到数据库对象的映射
 *
 * Created by xueqiulxq on 9/15/16.
 */
public class RadRecord {

    private static final String TAG = RadRecord.class.getSimpleName();

    public long id = -1;
    public String key = "";
    public byte[] data;
    private String ds = "";
    private long size = 0;

    public RadRecord() {

    }

    public RadRecord(long id) {
        this.id = id;
    }

    public RadRecord(String key) {
        this.key = key;
    }

    public RadRecord(String key, byte[] data) {
        this.key = key;
        this.data = data;
    }

    public boolean isValid() {
        return !TextUtils.isEmpty(key);
    }

    @Override
    public String toString() {
        return "id=" + id + " key=" + key + " ds=" + ds + " size=" + size + " data" + String.valueOf(data);
    }

    public ContentValues toKv() {
        ContentValues ret = new ContentValues();
        if (id > 0) {
            ret.put(RadDBConst.RECORD_ID, id);
        }
        ret.put(RadDBConst.RECORD_KEY, key);
        ret.put(RadDBConst.RECORD_DATA, data);
        ret.put(RadDBConst.RECORD_DS, String.valueOf(System.currentTimeMillis()));
        ret.put(RadDBConst.RECORD_SIZE, data == null ? 0 : data.length);
        return ret;
    }

    public static RadRecord fromKv(ContentValues kv) {
        if (kv == null) {
            return null;
        }
        RadRecord ret = new RadRecord();
        ret.key = kv.getAsString(RadDBConst.RECORD_KEY);
        ret.data = kv.getAsByteArray(RadDBConst.RECORD_DATA);
        ret.ds = kv.getAsString(RadDBConst.RECORD_DS);
        if (kv.containsKey(RadDBConst.RECORD_ID)) {
            ret.id = kv.getAsLong(RadDBConst.RECORD_ID);
        }
        if (kv.containsKey(RadDBConst.RECORD_SIZE)) {
            ret.size = kv.getAsLong(RadDBConst.RECORD_SIZE);
        }
        return ret;
    }
}
