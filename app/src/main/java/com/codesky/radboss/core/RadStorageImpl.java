package com.codesky.radboss.core;

import android.content.ContentValues;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Database implementation
 *
 * Created by xueqiulxq on 8/21/16.
 */
public class RadStorageImpl {

    private static class SingleHolder {
        public static RadStorageImpl sInstance = new RadStorageImpl();
    }

    public static RadStorageImpl getInstance() {
        return SingleHolder.sInstance;
    }

    Map<String, ContentValues> db;

    private RadStorageImpl() {
        // TODO: 8/21/16
        db = new HashMap<String, ContentValues>();
    }

    public ContentValues insert(ContentValues kv) {
        db.put(kv.getAsString(TableStruct.KEY), kv);
        return kv;
    }

    public List<ContentValues> insert(List<ContentValues> kvs) {
        for (ContentValues kv : kvs) {
            insert(kv);
        }
        return kvs;
    }

    public List<ContentValues> delete(ContentValues key) {
        ContentValues ret = db.remove(key.getAsString(TableStruct.KEY));
        return ret == null ? Collections.<ContentValues>emptyList() : Collections.singletonList(ret);
    }

    public List<ContentValues> delete(List<ContentValues> keys) {
        List<ContentValues> ret = new ArrayList<ContentValues>();
        for (ContentValues key : keys) {
            ret.addAll(delete(key));
        }
        return ret;
    }

    public List<ContentValues> modify(ContentValues kv) {
        ContentValues old = null;
        if (db.containsKey(kv.getAsString(TableStruct.KEY))) {
            old = db.put(kv.getAsString(TableStruct.KEY), kv);
        }
        return old == null ? Collections.<ContentValues>emptyList() : Collections.singletonList(old);
    }

    public List<ContentValues> modify(List<ContentValues> kvs) {
        List<ContentValues> ret = new ArrayList<ContentValues>();
        for (ContentValues kv : kvs) {
            ret.addAll(modify(kv));
        }
        return ret;
    }

    public List<ContentValues> query(ContentValues key) {
        List<ContentValues> ret = new ArrayList<ContentValues>();
        if ("*".equals(key.getAsString(TableStruct.KEY))) {
            return new ArrayList<ContentValues>(db.values());
        } else {
            for (String k : db.keySet()) {
                if (k.equals(key.getAsString(TableStruct.KEY))) {
                    ret.add(db.get(k));
                }
            }
        }
        return ret;
    }

    public List<ContentValues> query(List<ContentValues> keys) {
        List<ContentValues> ret = new ArrayList<ContentValues>();
        for (ContentValues key : keys) {
            ret.addAll(query(key));
        }
        return ret;
    }
}
