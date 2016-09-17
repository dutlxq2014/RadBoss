package com.codesky.radboss.example;

/**
 *
 * Created by xueqiulxq on 9/17/16.
 */
public class DataResult<T> {

    private T data;
    private boolean isSuccess;
    private String message;

    public DataResult(boolean isSuccess) {
        this(null, isSuccess, null);
    }

    public DataResult(boolean isSuccess, String msg) {
        this(null, isSuccess, msg);
    }

    public DataResult(T data, boolean isSuccess, String msg) {
        this.data = data;
        this.isSuccess = isSuccess;
        this.message = msg;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public boolean isSuccess() {
        return isSuccess;
    }
}
