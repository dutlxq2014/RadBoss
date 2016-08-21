package com.codesky.radboss.core;

/**
 *
 * Created by xueqiulxq on 8/21/16.
 */
public class RadEngine {

    private static final String OPT_INSERT = "insert";
    private static final String OPT_DELETE = "delete";
    private static final String OPT_MODIFY = "modify";
    private static final String OPT_QUERY = "query";

    private static class SingleHolder {
        public static final RadEngine sInstance = new RadEngine();
    }

    public static RadEngine getInstance() {
        return SingleHolder.sInstance;
    }

    public <T> RadResult<T> insert(String key, byte[] data) {
        return new RadResult<T>(true, OPT_INSERT);
    }

    public <T> void insertAync(String key, byte[] data, RadCallback<T> callback) {
        // TODO

        RadResult<T> result = new RadResult<T>(true, OPT_INSERT);
        if (callback != null) {
            callback.onResult(result);
        }
    }

    public <T> RadResult<T> delete(String key) {
        return new RadResult<T>(true, OPT_DELETE);
    }

    public <T> void deleteAsync(String key, RadCallback<T> callback) {
        // TODO

        RadResult<T> result = new RadResult<T>(true, OPT_DELETE);
        if (callback != null) {
            callback.onResult(result);
        }
    }

    public <T> RadResult<T> modify(String key, byte[] data, RadCallback<T> callback) {
        return new RadResult<T>(true, OPT_MODIFY);
    }

    public <T> void modifyAsync(String key, byte[] data, RadCallback<T> callback) {
        // TODO

        RadResult<T> result = new RadResult<T>(true, OPT_MODIFY);
        if (callback != null) {
            callback.onResult(result);
        }
    }
}
