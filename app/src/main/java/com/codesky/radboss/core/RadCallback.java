package com.codesky.radboss.core;

/**
 * Callback for insert/delete/modify/query
 *
 * Created by xueqiulxq on 8/21/16.
 */
public interface RadCallback<T> {
    public void onResult(RadResult<T> result);
}
