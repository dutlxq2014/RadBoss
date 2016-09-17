package com.codesky.radboss.example;

/**
 *
 * Created by xueqiulxq on 9/17/16.
 */
public interface IServiceCallback<T> {

    void onResult(DataResult<T> result);
}
