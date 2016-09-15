package com.codesky.radlib.support;

/**
 * Wrapper of operation result.
 *
 * Created by xueqiulxq on 9/15/16.
 */
public class RadResult<T> {

    private  T data;
    private boolean isSuccess;
    private String message;

    public RadResult() {
        this(true, "");
    }

    public RadResult(boolean isSuccess) {
        this(isSuccess, "");
    }

    public RadResult(boolean isSuccess, String message) {
        this.isSuccess = isSuccess;
        this.message = message;
    }

    public RadResult(boolean isSuccess, T data) {
        this.data = data;
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
