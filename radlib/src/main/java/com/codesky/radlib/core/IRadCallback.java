package com.codesky.radlib.core;


import com.codesky.radlib.support.RadResult;

/**
 * Callback for insert/delete/modify/query
 *
 * Created by xueqiulxq 9/15/16.
 */
public interface IRadCallback<T> {

    public void onResult(RadResult<T> result);
}
