package com.codesky.radboss.core;

/**
 * Wrapper of operation result.
 *
 * Created by xueqiulxq on 8/21/16.
 */
public class RadResult<T> {

    private  T data;
    private boolean isSuccess;
    private String message;

    public RadResult() {
        this(true, null);
    }

    public RadResult(boolean isSuccess) {
        this(isSuccess, null);
    }

    public RadResult(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public void setData(T data) {
        this.data = data;
    }

    public T getData() {
        return data;
    }

    public void setSuccess(boolean success) {
        isSuccess = success;
    }

    public boolean isSuccess() {
        return isSuccess;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }
}
