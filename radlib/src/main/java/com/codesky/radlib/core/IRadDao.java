package com.codesky.radlib.core;

import com.codesky.radlib.support.ERadTab;
import com.codesky.radlib.support.RadRecord;

import java.util.List;

/**
 *
 * Created by xueqiulxq on 9/15/16.
 */
public interface IRadDao {

    void insertAsync(ERadTab module, RadRecord record, IRadCallback<List<RadRecord>> callback);
    void insertAsync(ERadTab module, List<RadRecord> records, IRadCallback<List<RadRecord>> callback);

    void deleteAsync(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback);
    void deleteAsync(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback);
    void deleteAsyncById(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback);
    void deleteAsyncById(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback);

    void modifyAsync(ERadTab module, RadRecord record, IRadCallback<List<RadRecord>> callback);
    void modifyAsync(ERadTab module, List<RadRecord> records, IRadCallback<List<RadRecord>> callback);
    void modifyAsyncById(ERadTab module, RadRecord record, IRadCallback<List<RadRecord>> callback);
    void modifyAsyncById(ERadTab module, List<RadRecord> records, IRadCallback<List<RadRecord>> callback);

    void queryAsync(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback);
    void queryAsync(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback);
    void queryAsyncById(ERadTab module, RadRecord key, IRadCallback<List<RadRecord>> callback);
    void queryAsyncById(ERadTab module, List<RadRecord> keys, IRadCallback<List<RadRecord>> callback);
}
