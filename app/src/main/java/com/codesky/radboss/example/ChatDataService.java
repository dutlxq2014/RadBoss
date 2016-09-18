package com.codesky.radboss.example;

import android.text.TextUtils;

import com.codesky.radboss.util.SafeJsonUtil;
import com.codesky.radlib.core.IRadCallback;
import com.codesky.radlib.core.IRadDao;
import com.codesky.radlib.core.RadDao;
import com.codesky.radlib.support.ERadTab;
import com.codesky.radlib.support.RadRecord;
import com.codesky.radlib.support.RadResult;

import java.util.List;

/**
 *
 * Created by xueqiulxq on 9/17/16.
 */
public class ChatDataService {

    private IRadDao mRadDao;

    private static class SingletonHolder {
        public static final ChatDataService sInstance = new ChatDataService();
    }

    private ChatDataService() {
        mRadDao = RadDao.getInstance();
    }

    public static ChatDataService getInstance() {
        return SingletonHolder.sInstance;
    }

    public void append(ERadTab store, String key, String data, final IServiceCallback<List<ChatBean>> callback) {
        if (!TextUtils.isEmpty(key) && !TextUtils.isEmpty(data)) {
            final RadRecord record = new RadRecord();
            record.key = key;
            record.data = data.getBytes();
            mRadDao.insertAsync(store, record, new IRadCallback<List<RadRecord>>() {
                @Override
                public void onResult(RadResult<List<RadRecord>> result) {
                    DataResult<List<ChatBean>> cbResult = new DataResult<List<ChatBean>>(
                            result.isSuccess(), result.getMessage());
                    if (result.isSuccess()) {
                        cbResult.setData(ChatTrans.toChatBean(result.getData()));
                    }
                    if (callback != null) {
                        callback.onResult(cbResult);
                    }
                }
            });
        }
    }

    public void append(ERadTab store, String key, ChatBean data, final IServiceCallback<List<ChatBean>> callback) {
        if (!TextUtils.isEmpty(key) && data != null) {
            String jsonStr = SafeJsonUtil.toJsonStr(data.toMap());
            append(store, key, jsonStr, callback);
        }
    }

    public void query(ERadTab store, String key, final IServiceCallback<List<ChatBean>> callback) {
        if (!TextUtils.isEmpty(key)) {
            final RadRecord record = new RadRecord();
            record.key = key;
            mRadDao.queryAsync(store, record, new IRadCallback<List<RadRecord>>() {
                @Override
                public void onResult(RadResult<List<RadRecord>> result) {
                    DataResult<List<ChatBean>> cbResult = new DataResult<List<ChatBean>>(
                            result.isSuccess(), result.getMessage());
                    if (result.isSuccess()) {
                        cbResult.setData(ChatTrans.toChatBean(result.getData()));
                    }
                    if (callback != null) {
                        callback.onResult(cbResult);
                    }
                }
            });
        }
    }

    public void delete(ERadTab store, String key, final IServiceCallback<List<ChatBean>> callback) {
        if (!TextUtils.isEmpty(key)) {
            final RadRecord record = new RadRecord();
            record.key = key;
            mRadDao.deleteAsync(store, record, new IRadCallback<List<RadRecord>>() {
                @Override
                public void onResult(RadResult<List<RadRecord>> result) {
                    DataResult<List<ChatBean>> cbResult = new DataResult<List<ChatBean>>(
                            result.isSuccess(), result.getMessage());
                    if (result.isSuccess()) {
                        cbResult.setData(ChatTrans.toChatBean(result.getData()));
                    }
                    if (callback != null) {
                        callback.onResult(cbResult);
                    }
                }
            });
        }
    }
}
