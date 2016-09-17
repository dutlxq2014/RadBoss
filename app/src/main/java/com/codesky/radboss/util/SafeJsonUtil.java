package com.codesky.radboss.util;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.Map;

/**
 *
 * Created by xueqiulxq on 9/17/16.
 */
public class SafeJsonUtil {

    public static String toJsonStr(Map target) {
        if (target != null) {
            try {
                JSONObject obj = new JSONObject(target);
                return obj.toString();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return null;
    }

    public static JSONObject fromJsonObj(byte[] jsonByte) {
        if (jsonByte != null && jsonByte.length > 0) {
            return fromJsonObj(new String(jsonByte));
        }
        return null;
    }

    public static JSONObject fromJsonObj(String jsonStr) {
        try {
            return new JSONObject(jsonStr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

    public static JSONArray fromJsonArr(String jsonArr) {
        try {
            return new JSONArray(jsonArr);
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return null;
    }

}
