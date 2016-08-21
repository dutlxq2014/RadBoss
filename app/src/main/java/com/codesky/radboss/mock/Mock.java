package com.codesky.radboss.mock;

import android.content.ContentValues;

import com.codesky.radboss.core.TableStruct;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * Created by xueqiulxq on 8/21/16.
 */
public class Mock {

    public static ContentValues singleInsert() {
        ContentValues mock = new ContentValues();
        mock.put(TableStruct.KEY, "key");
        mock.put(TableStruct.VALUE, "single".getBytes());
        return mock;
    }

    public static List<ContentValues> multiInsert() {
        List<ContentValues> mocks = new ArrayList<ContentValues>();
        ContentValues mock = new ContentValues();
        mock.put(TableStruct.KEY, "key1");
        mock.put(TableStruct.VALUE, "multi1".getBytes());
        mocks.add(mock);
        mock = new ContentValues();
        mock.put(TableStruct.KEY, "key2");
        mock.put(TableStruct.VALUE, "multi2".getBytes());
        mocks.add(mock);
        mock = new ContentValues();
        mock.put(TableStruct.KEY, "key3");
        mock.put(TableStruct.VALUE, "multi3".getBytes());
        mocks.add(mock);
        return mocks;
    }

    public static ContentValues singleDelete() {
        return singleQuery();
    }

    public static List<ContentValues> multiDelete() {
        return multiQuery();
    }

    public static ContentValues singleModify() {
        ContentValues mock = new ContentValues();
        mock.put(TableStruct.KEY, "key");
        mock.put(TableStruct.VALUE, "modify".getBytes());
        return mock;
    }

    public static List<ContentValues> multiModify() {
        List<ContentValues> mocks = new ArrayList<ContentValues>();
        ContentValues mock = new ContentValues();
        mock.put(TableStruct.KEY, "key1");
        mock.put(TableStruct.VALUE, "modify1".getBytes());
        mocks.add(mock);
        mock = new ContentValues();
        mock.put(TableStruct.KEY, "key2");
        mock.put(TableStruct.VALUE, "modify2".getBytes());
        mocks.add(mock);
        mock = new ContentValues();
        mock.put(TableStruct.KEY, "key3");
        mock.put(TableStruct.VALUE, "modify3".getBytes());
        mocks.add(mock);
        return mocks;
    }

    public static ContentValues singleQuery() {
        ContentValues mock = new ContentValues();
        mock.put(TableStruct.KEY, "key");
        return mock;
    }

    public static List<ContentValues> multiQuery() {
        List<ContentValues> mocks = new ArrayList<ContentValues>();
        ContentValues mock = new ContentValues();
        mock.put(TableStruct.KEY, "key1");
        mocks.add(mock);
        mock = new ContentValues();
        mock.put(TableStruct.KEY, "key2");
        mocks.add(mock);
        mock = new ContentValues();
        mock.put(TableStruct.KEY, "key3");
        mocks.add(mock);
        return mocks;
    }
}
