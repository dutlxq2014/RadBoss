package com.codesky.radlib.support;

import android.content.ContentValues;
import android.text.TextUtils;

/**
 * Map program object to db record.
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
        return "id=" + id + " key=" + key + " ds=" + ds + " size=" + size + " data" + new String(data);
    }

    public ContentValues toKv() {
        ContentValues ret = new ContentValues();
        if (id > 0) {
            ret.put(RadDBConst.FIELD_ID, id);
        }
        ret.put(RadDBConst.FIELD_KEY, key);
        ret.put(RadDBConst.FIELD_DATA, data);
        ret.put(RadDBConst.FIELD_DS, String.valueOf(System.currentTimeMillis()));
        ret.put(RadDBConst.FIELD_SIZE, data == null ? 0 : data.length);
        return ret;
    }

    public static RadRecord fromKv(ContentValues kv) {
        if (kv == null) {
            return null;
        }
        RadRecord ret = new RadRecord();
        ret.key = kv.getAsString(RadDBConst.FIELD_KEY);
        ret.data = kv.getAsByteArray(RadDBConst.FIELD_DATA);
        ret.ds = kv.getAsString(RadDBConst.FIELD_DS);
        if (kv.containsKey(RadDBConst.FIELD_ID)) {
            ret.id = kv.getAsLong(RadDBConst.FIELD_ID);
        }
        if (kv.containsKey(RadDBConst.FIELD_SIZE)) {
            ret.size = kv.getAsLong(RadDBConst.FIELD_SIZE);
        }
        return ret;
    }
}
